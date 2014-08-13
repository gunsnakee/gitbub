package com.meiliwan.emall.stock.service;


import static com.meiliwan.emall.icetool.JSONTool.addToResult;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.Lock;

import com.meiliwan.emall.base.client.BaseMailAndMobileClient;
import com.meiliwan.emall.base.dto.MailEntity;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.plugin.zk.ZKClient;
import com.meiliwan.emall.commons.util.IPUtil;
import com.meiliwan.emall.commons.util.RedisSearchInfoUtil;
import com.meiliwan.emall.commons.util.SpringContextUtil;
import com.meiliwan.emall.stock.bean.*;
import com.meiliwan.emall.stock.constant.StockErrorCode;
import com.meiliwan.emall.commons.bean.SellStockStatus;
import com.meiliwan.emall.stock.dao.StockImportLogDao;
import com.meiliwan.emall.stock.dao.StockImportResultDao;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;
import com.meiliwan.emall.stock.dao.ProStockDao;
import com.meiliwan.emall.stock.dao.ProStockLogDao;
import com.meiliwan.emall.stock.util.StockLockUtil;

/**
 * 商品库存业务层实现
 */
@Service("proStockService")
public class ProStockService extends DefaultBaseServiceImpl {

    private static final MLWLogger logger = MLWLoggerFactory.getLogger(ProStockService.class);

    private final static String PROPERTIES_ZK = "/mlwconf/commons/mail.properties";

    private static final String SYSTEM_EN = "/conf/commons.properties";

    private final static String STOCKKEY = "sell.stock.mail";

    @Autowired
    private ProStockDao proStockDao;
    @Autowired
    private ProStockLogDao proStockLogDao;
    @Autowired
    private StockImportLogDao stockImportLogDao;
    @Autowired
    private StockImportResultDao stockImportResultDao;

    /**
     * 增加一条商品库存数据
     *
     * @param resultObj
     * @param stock
     * @param userId    前台用户ID或者后台管理员ID
     * @param userType  用户类型，用admin或者user表示
     */
    public void insertStock(JsonObject resultObj, ProStock stock, int userId, String userType) {
        int count = proStockDao.insert(stock);
        String batchNo = "set_" + getRandBatchNo();
        if (count > 0) {
            //增加商品可使用库存缓存
            setSellStockCache(stock.getProId(), stock.getSellStock());
            //增加业务日志
            ProStockLog log = new ProStockLog(stock.getProId(), stock.getStock(), (short) 1, "stock", batchNo, userId, userType, "0", "0", stock.getSellStock(), stock.getStock());
            proStockLogDao.insert(log);
        }
        addToResult(count > 0 ? count : 0, resultObj);
    }

    /**
     * 修改商品库存
     *
     * @param resultObj
     * @param stock
     */
    public synchronized void updateStock(JsonObject resultObj, ProStock stock) {
        int count = proStockDao.update(stock);
        boolean suc = false;
        if (count > 0) {
            setSellStockCache(stock.getProId(), stock.getSellStock());
            RedisSearchInfoUtil.addSearchInfo(stock.getProId());
            suc = true;
        }
        addToResult(suc, resultObj);
    }

    /**
     * 增加商品的可用库存量
     *
     * @param proId
     * @param addNum
     * @param userId   前台用户ID或者后台管理员ID
     * @param userType 用户类型，用admin或者user表示
     */
    public void addSellStock(JsonObject resultObj, int proId, int addNum, int userId, String userType) {
        int count = 0;
        String batchNo = "addSellStock_" + getRandBatchNo();
        List<ProStockLog> logs = new ArrayList<ProStockLog>();
        Lock lock = StockLockUtil.get(proId);
        boolean success = false;
        try {
            lock.lock();
            int num = getSellStockCache(proId);
            int result = num + addNum;
            //先修改缓存
            boolean suc = setSellStockCache(proId, result);
            //修改数据库
            if (suc) {
                count = proStockDao.addStock(proId, addNum);
                if (count > 0) {
                    success = true;
                    //增加可使用库存的改变日志
                    logs.add(new ProStockLog(proId, addNum, (short) 1, "sell_stock", batchNo, userId, userType, "0", "0", num, result));
                    //增加总库存的改变信息
                    logs.add(new ProStockLog(proId, addNum, (short) 1, "stock", batchNo, userId, userType, "0", "0", num, result));
                } else {
                    //修改数据库失败，回退修改缓存
                    setSellStockCache(proId, num);
                }
            }
        } catch (ServiceException e) {
            logger.error(new ServiceException("StockService-addSellStock-增加商品可用库存失败", e), "proId:" + proId + ",addNum:" + addNum + ",userId:" + userId, "");
        } finally {
            lock.unlock();
        }
        if (success) {
            //修改搜索列表的索引
            RedisSearchInfoUtil.addSearchInfo(proId);
            try {
                proStockLogDao.addStockLogOnBatch(logs);
            } catch (ServiceException e) {
                logger.error(new ServiceException("StockService-addProStockLog-增加商品库存日志失败", e), logs, "");
            }
        }
        addToResult(success, resultObj);
    }

    /**
     * 管理员设置用户的损坏库存
     * 业务场景：更新商品损坏库存为在原有的基础上增加stock
     *
     * @param proId    商品ID
     * @param addNum   新损坏库存的值
     * @param userId   前台用户ID或者后台管理员ID
     * @param userType 用户类型，用admin或者user表示
     */
    public void addUnsellStock(JsonObject resultObj, int proId, int addNum, int userId, String userType) {
        int count = 0;
        boolean success = false;
        String batchNo = "addUnsellStock_" + getRandBatchNo();
        List<ProStockLog> logs = new ArrayList<ProStockLog>();
        Lock lock = StockLockUtil.get(proId);
        try {
            lock.lock();
            int num = getSellStockCache(proId);
            //先修改缓存
            int result = num - addNum;
            int stockValue = result >= 0 ? result : 0;
            boolean suc = setSellStockCache(proId, stockValue);
            //修改数据库
            if (suc) {
                int addStock = result >= 0 ? addNum : num;
                count = proStockDao.addUnsellStock(proId, addStock);
                if (count > 0) {
                    success = true;
                    logs.add(new ProStockLog(proId, addNum, (short) -1, "sell_stock", batchNo, userId, userType, "0", "0", num, stockValue));
                    logs.add(new ProStockLog(proId, addNum, (short) 1, "unsell_stock", batchNo, userId, userType, "0", "0", num, stockValue));
                } else {
                    //数据库修改失败，回退
                    setSellStockCache(proId, num);
                }
            }
        } catch (ServiceException e) {
            logger.error(new ServiceException("StockService-addUnsellStock-增加商品不可用库存失败", e), "proId:" + proId + ",addNum:" + addNum + ",userId:" + userId, "");
        } finally {
            lock.unlock();
        }
        if (success) {
            //修改搜索列表的索引
            RedisSearchInfoUtil.addSearchInfo(proId);
            try {
                proStockLogDao.addStockLogOnBatch(logs);
            } catch (ServiceException e) {
                logger.error(new ServiceException("StockService-addProStockLog-增加商品库存日志失败", e), logs, "");
            }
        }

        addToResult(success, resultObj);
    }

    /**
     * 减少商品的可用库存量
     *
     * @param proId    商品ID
     * @param subNum   数量
     * @param userId   前台用户ID或者后台管理员ID
     * @param userType 用户类型，用admin或者user表示
     */
    public void subSellStock(JsonObject resultObj, int proId, int subNum, int userId, String userType) {
        Lock lock = StockLockUtil.get(proId);
        int count = 0;
        boolean success = false;
        String batchNo = "subSellStock_" + getRandBatchNo();
        List<ProStockLog> logs = new ArrayList<ProStockLog>();
        try {
            lock.lock();
            int num = getSellStockCache(proId);
            int result = num - subNum;
            int stockValue = result >= 0 ? result : 0;
            //先修改缓存,在修改数据库
            boolean suc = setSellStockCache(proId, stockValue);
            if (suc) {
                //修改数据库
                int subStock = result >= 0 ? subNum : num;
                count = proStockDao.subStock(proId, subStock);
                if (count > 0) {
                    success = true;
                    logs.add(new ProStockLog(proId, subNum, (short) -1, "sell_stock", batchNo, userId, userType, "0", "0", num, stockValue));
                    logs.add(new ProStockLog(proId, subNum, (short) -1, "stock", batchNo, userId, userType, "0", "0", num, stockValue));
                } else {
                    //更新数据库失败，缓存回退
                    setSellStockCache(proId, num);
                }
            }
        } catch (ServiceException e) {
            logger.error(new ServiceException("StockService-subSellStock-减少库存失败", e), "proId:" + proId + ",subNum:" + subNum + ",userId:" + userId, "");
        } finally {
            lock.unlock();
        }
        if (success) {
            //修改搜素列表的索引
            RedisSearchInfoUtil.addSearchInfo(proId);
            try {
                proStockLogDao.addStockLogOnBatch(logs);
            } catch (Exception e) {
                logger.error(new ServiceException("StockService-addProStockLog-增加商品库存日志失败", e), logs, "");
            }
        }
        addToResult(success, resultObj);
    }

    /**
     * 获取库存所有信息，获取实体
     *
     * @param proId
     */
    public void getStockById(JsonObject resultObj, int proId) {
        addToResult(proStockDao.getEntityById(proId), resultObj);
    }

    /**
     * 获取商品总库存
     *
     * @param proId
     */
    public void getStock(JsonObject resultObj, int proId) {
        if (proId > 0) {
            ProStock stock = proStockDao.getEntityById(proId);
            addToResult(stock == null ? 0 : stock.getStock(), resultObj);
        } else {
            addToResult(0, resultObj);
        }
    }

    /**
     * 获取商品可销售库存
     *
     * @param proId
     */
    public void getSellStock(JsonObject resultObj, int proId) {
        if (proId > 0) {
            //首先从缓存里面取
            int num = getSellStockCache(proId);
            addToResult(num, resultObj);
        } else {
            addToResult(0, resultObj);
        }
    }

    /**
     * 获取商品订单库存
     *
     * @param proId
     */
    public void getOrderStock(JsonObject resultObj, int proId) {
        if (proId > 0) {
            ProStock stock = proStockDao.getEntityById(proId);
            addToResult(stock == null ? 0 : stock.getOrderStock(), resultObj);
        } else {
            addToResult(0, resultObj);
        }
    }

    /**
     * 根据商品id检查对应商品可用库存的状态；
     * 业务场景：用户访问该商品商品详情页得到详情页展示时，需要检查该商品库存是否充足；
     *
     * @param : pro_id 商品id
     * @return : SellStockStatus 可用库存枚举：StockOut : 缺货; SufficientStock：充足 StressStock 库存紧张
     */
    public void checkSellStockStatus(JsonObject resultObj, int proId) {
        if (proId > 0) {
            //先查找缓存
            int num = getSellStockCache(proId);
            if (num >= 10) {
                addToResult(SellStockStatus.SufficientStock, resultObj);
            } else if (num > 0 && num < 10) {
                addToResult(SellStockStatus.StressStock, resultObj);
            } else {
                addToResult(SellStockStatus.StockOut, resultObj);
            }
        } else {
            addToResult(SellStockStatus.StockOut, resultObj);
        }

    }

    /**
     * 对商品库存进行校验，是否有可以支持的可用库存支持下订单;
     * 业务场景：
     * 将一定数量n的商品A，检查该商品库存是否该数量的商品可用;
     *
     * @param : prod 商品id
     * @param : num 放入购物车的商品数
     * @return : 是否可以放到购物车中
     */
    public void checkSellStockByNum(JsonObject resultObj, int proId, int num) {
        boolean suc = checkStock(proId, num);
        addToResult(suc, resultObj);
    }

    /**
     * 对购物车里的每一个库存项商品库存进行校验，返回每一个购物项是否可以支持的购物的状态；
     * 业务场景：
     * 用户查看购物车(或者返回购物车时；或去查看购物车时)，当进行数量加减的每一个动作时，进行库存验证
     *
     * @param items List<items> : 库存列表;
     * @return StockItemStatus, 其中包括StockItem，包括proId, 校验num,和校验结果;
     */
    public void checkSellStockIfSub(JsonObject resultObj, StockItem[] items) {
        List<StockItemStatus> cisList = new ArrayList<StockItemStatus>();
        if (items != null && items.length > 0) {
            // 遍历购物车里的每一个购物车
            for (StockItem item : items) {
                StockItemStatus cis = new StockItemStatus(item);
                // 购物项商品的库存校验结果存放到状态列表;
                cis.setStatus(checkStock(item.getProId(), item.getBuyNum()));
                cisList.add(cis);
            }
        }
        addToResult(cisList, resultObj);
    }

    /**
     * 提交订单（在线支付情况），减库存
     * 对提交订单的订单行商品进行库存扣减，并返回每一个订单行库存扣减的状态,及总扣减状态;
     * 业务场景：用户提交订单选择在线支付时，线上支付完成并下完订单后，对订单中的每一个订单项进行库存扣减；
     *
     * @param items    订单行列表
     * @param userId   前台用户ID或者后台管理员ID
     * @param userType 用户类型，用admin或者user表示
     * @return StockItemsUpdateResult  订单行库存处理的结果
     */
    public void updateStockOnOrderSubmit(JsonObject resultObj, StockItem[] items, int userId, String userType) {
        StockItemsUpdateResult result = new StockItemsUpdateResult();
        if (items != null && items.length > 0) {
            List<StockItemStatus> itemStatuses = new ArrayList<StockItemStatus>();
            String batchNo = "orderSubmit_" + getRandBatchNo();
            List<ProStockLog> logs = new ArrayList<ProStockLog>();
            //定义一个标量，来设置总订单的状态
            int flag = 0;
            //首先遍历每一个库存项的状态
            for (StockItem item : items) {
                StockItemStatus status = new StockItemStatus(item);
                Lock lock = StockLockUtil.get(item.getProId());
                try {
                    lock.lock();
                    if (checkStock(item.getProId(), item.getBuyNum())) {
                        int num = getSellStockCache(item.getProId());
                        int nowStock = num - item.getBuyNum();
                        //先修改缓存
                        boolean suc = setSellStockCache(item.getProId(), nowStock);
                        //缓存修改成功，修改数据库
                        if (suc) {
                            int count = proStockDao.subStockByOrder(item.getProId(), item.getBuyNum());
                            if (count > 0) {
                                status.setStatus(true);
                                status.setCode(StockErrorCode.successStockCode);
                                //增加库存变化的业务日志
                                logs.add(new ProStockLog(item.getProId(), item.getBuyNum(), (short) -1, "sell_stock", batchNo, userId, userType,
                                        item.getOrderItem() == null ? "0" : item.getOrderItem().getOrderId(), item.getOrderItem() == null ? "0" : item.getOrderItem().getOrderItemId(), num, nowStock));
                                logs.add(new ProStockLog(item.getProId(), item.getBuyNum(), (short) 1, "order_stock", batchNo, userId, userType,
                                        item.getOrderItem() == null ? "0" : item.getOrderItem().getOrderId(), item.getOrderItem() == null ? "0" : item.getOrderItem().getOrderItemId(), num, nowStock));
                            } else {
                                status.setStatus(false);
                                status.setCode(StockErrorCode.dbStockCode);
                                flag += 1;
                                //修改数据库失败，回退
                                setSellStockCache(item.getProId(), num);
                                logger.warn("提交订单-修改库存数据库失败", item, "");
                            }
                        } else {
                            status.setCode(StockErrorCode.cacheStockCode);
                            logger.warn("提交订单-修改库存缓存", item, "");
                        }
                    } else {
                        status.setStatus(false);
                        status.setCode(StockErrorCode.stressStockCode);
                        flag += 1;
                        logger.warn("提交订单-库存短缺", item, "");
                    }
                } catch (ServiceException e) {
                    logger.error(new ServiceException("StockService-updateStockOnOrderSubmit-提交订单商品库存修改失败", e), item, "");
                    status.setStatus(false);
                    status.setCode(StockErrorCode.dbStockCode);
                    flag += 1;
                } finally {
                    lock.unlock();
                }
                itemStatuses.add(status);
                //修改搜素列表的索引
                RedisSearchInfoUtil.addSearchInfo(item.getProId());
            }
            result.setStockItemStatusList(itemStatuses);
            if (flag > 0) {
                result.setStatus(false);
            } else {
                result.setStatus(true);
                //增加库存变化的业务日志
                try {
                    proStockLogDao.addStockLogOnBatch(logs);
                } catch (Exception e) {
                    logger.error(new ServiceException("StockService-addProStockLog-增加商品库存日志失败", e), logs, "");
                }

            }
        }
        addToResult(result, resultObj);
    }

    /**
     * 提交订单（货到付款情况），减库存
     * 对提交订单的订单行商品进行库存扣减，并返回每一个订单行库存扣减的状态,及总扣减状态;
     * 业务场景：用户提交订单选择在线支付时，线上支付完成并下完订单后，对订单中的每一个订单项进行库存扣减；
     *
     * @param items    订单行列表
     * @param userId   前台用户ID或者后台管理员ID
     * @param userType 用户类型，用admin或者user表示
     * @return StockItemsUpdateResult  订单行库存处理的结果
     */
    public void updateStockOnOrderCODSubmit(JsonObject resultObj, StockItem[] items, int userId, String userType) {
        StockItemsUpdateResult result = new StockItemsUpdateResult();
        if (items != null && items.length > 0) {
            List<StockItemStatus> itemStatuses = new ArrayList<StockItemStatus>();
            String batchNo = "orderSubmit_" + getRandBatchNo();
            List<ProStockLog> logs = new ArrayList<ProStockLog>();
            //定义一个标量，来设置总订单的状态
            int flag = 0;
            //首先遍历每一个库存项的状态
            for (StockItem item : items) {
                StockItemStatus status = new StockItemStatus(item);
                Lock lock = StockLockUtil.get(item.getProId());
                try {
                    lock.lock();
                    if (checkStock(item.getProId(), item.getBuyNum())) {
                        int num = getSellStockCache(item.getProId());
                        int nowStock = num - item.getBuyNum();
                        //先修改缓存
                        boolean suc = setSellStockCache(item.getProId(), nowStock);
                        //缓存修改成功，修改数据库
                        if (suc) {
                            int count = proStockDao.subStockByOrder(item.getProId(), item.getBuyNum());
                            if (count > 0) {
                                status.setStatus(true);
                                status.setCode(StockErrorCode.successStockCode);
                                //增加库存变化的业务日志
                                logs.add(new ProStockLog(item.getProId(), item.getBuyNum(), (short) -1, "sell_stock", batchNo, userId, userType,
                                        item.getOrderItem() == null ? "0" : item.getOrderItem().getOrderId(), item.getOrderItem() == null ? "0" : item.getOrderItem().getOrderItemId(), num, nowStock));
                                logs.add(new ProStockLog(item.getProId(), item.getBuyNum(), (short) 1, "order_stock", batchNo, userId, userType,
                                        item.getOrderItem() == null ? "0" : item.getOrderItem().getOrderId(), item.getOrderItem() == null ? "0" : item.getOrderItem().getOrderItemId(), num, nowStock));
                            } else {
                                status.setStatus(false);
                                status.setCode(StockErrorCode.dbStockCode);
                                flag += 1;
                                //修改数据库失败，回退
                                setSellStockCache(item.getProId(), num);
                                logger.warn("提交订单-修改库存数据库失败", item, "");
                            }
                        } else {
                            status.setCode(StockErrorCode.cacheStockCode);
                            logger.warn("提交订单-修改库存缓存", item, "");
                        }
                    } else {
                        status.setStatus(false);
                        status.setCode(StockErrorCode.stressStockCode);
                        flag += 1;
                        logger.warn("提交订单-库存短缺", item, "");
                    }
                } catch (ServiceException e) {
                    logger.error(new ServiceException("StockService-updateStockOnOrderSubmit-提交订单商品库存修改失败", e), item, "");
                    status.setStatus(false);
                    status.setCode(StockErrorCode.dbStockCode);
                    flag += 1;
                } finally {
                    lock.unlock();
                }
                itemStatuses.add(status);
                //修改搜素列表的索引
                RedisSearchInfoUtil.addSearchInfo(item.getProId());
            }
            result.setStockItemStatusList(itemStatuses);
            if (flag > 0) {
                result.setStatus(false);
            } else {
                result.setStatus(true);
                //增加库存变化的业务日志
                try {
                    proStockLogDao.addStockLogOnBatch(logs);
                } catch (Exception e) {
                    logger.error(new ServiceException("StockService-addProStockLog-增加商品库存日志失败", e), logs, "");
                }

            }
        }
        addToResult(result, resultObj);
    }

    /**
     * 扣减该订单行的订单库存
     * 业务场景： 管理员订单发货（线上支付订单的发货类型）
     *
     * @param items    订单库存项列表
     * @param userId   前台用户ID或者后台管理员ID
     * @param userType 用户类型，用admin或者user表示
     */
    public void stockUpdateOnSendOutPay(JsonObject resultObj, StockItem[] items, int userId, String userType) {
        boolean suc = false;
        String batchNo = "orderSentOutPay_" + getRandBatchNo();
        List<ProStockLog> logs = new ArrayList<ProStockLog>();
        if (items != null && items.length > 0) {
            try {
                for (StockItem item : items) {
                    Lock lock = StockLockUtil.get(item.getProId());
                    try {
                        lock.lock();
                        if (item.getOrderItem().getStatus()) {
                            int num = getSellStockCache(item.getProId());
                            proStockDao.subStockByAdminSend(item.getProId(), item.getBuyNum());
                            logs.add(new ProStockLog(item.getProId(), item.getBuyNum(), (short) -1, "stock", batchNo, userId, userType,
                                    item.getOrderItem() == null ? "0" : item.getOrderItem().getOrderId(), item.getOrderItem() == null ? "0" : item.getOrderItem().getOrderItemId(), num, num));
                            logs.add(new ProStockLog(item.getProId(), item.getBuyNum(), (short) -1, "order_stock", batchNo, userId, userType,
                                    item.getOrderItem() == null ? "0" : item.getOrderItem().getOrderId(), item.getOrderItem() == null ? "0" : item.getOrderItem().getOrderItemId(), num, num));
                        } else {
                            logger.info("管理员发货-订单库存扣减失败", item, "");
                        }
                    } catch (ServiceException e) {
                        logger.error(new ServiceException("StockService-stockUpdateOnSendOutPay-管理员订单发货（线上支付订单的发货类型失败", e), item, "");
                    } finally {
                        lock.unlock();
                    }
                }
                suc = true;
            } catch (Exception e) {
                suc = false;
                logger.error(new ServiceException("StockService-stockUpdateOnSendOutPay-管理员订单发货（线上支付订单的发货类型失败", e), items, "");
            }
        }
        if (suc) {
            try {
                proStockLogDao.addStockLogOnBatch(logs);
            } catch (Exception e) {
                logger.error(new ServiceException("StockService-addProStockLog-增加商品库存日志失败", e), logs, "");
            }
        }
        addToResult(suc, resultObj);
    }

    /**
     * 首先得校验库存，如果有一个订单行的库存大于数据库可销售的库存，那么整个发货动作失败，扣减库存失败，否则将逐行扣减库存
     * 业务场景：管理员订单发货（货到付款订单的发货类型）
     * [先线上更新；再线下发货] 程序保证原子性
     *
     * @param items    订单库存列表
     * @param userId   前台用户ID或者后台管理员ID
     * @param userType 用户类型，用admin或者user表示
     * @return 返回对应订单列表的库存状态以及整个订单状态情况
     */
    public void stockUpdateOnSendOutCOD(JsonObject resultObj, StockItem[] items, int userId, String userType) {
        StockItemsUpdateResult result = new StockItemsUpdateResult();
        if (items != null && items.length > 0) {
            List<StockItemStatus> itemStatuses = new ArrayList<StockItemStatus>();
            String batchNo = "orderSentOutCOD_" + getRandBatchNo();
            List<ProStockLog> logs = new ArrayList<ProStockLog>();
            //定义一个标量，来设置总订单的状态
            int flag = 0;
            //首先遍历每一个库存项的状态
            for (StockItem item : items) {
                StockItemStatus status = new StockItemStatus(item);
                Lock lock = StockLockUtil.get(item.getProId());
                try {
                    lock.lock();
                    if (checkStock(item.getProId(), item.getBuyNum())) {
                        //从缓存获取数据
                        int num = getSellStockCache(item.getProId());
                        int nowStock = num - item.getBuyNum();
                        //修改缓存
                        boolean suc = setSellStockCache(item.getProId(), nowStock);
                        if (suc) {
                            int rs = proStockDao.subStock(item.getProId(), item.getBuyNum());
                            if (rs > 0) {
                                status.setStatus(true);
                                status.setCode(StockErrorCode.successStockCode);
                                //增加库存改变的业务日志
                                logs.add(new ProStockLog(item.getProId(), item.getBuyNum(), (short) -1, "stock", batchNo, userId, userType,
                                        item.getOrderItem() == null ? "0" : item.getOrderItem().getOrderId(), item.getOrderItem() == null ? "0" : item.getOrderItem().getOrderItemId(), num, nowStock));
                                logs.add(new ProStockLog(item.getProId(), item.getBuyNum(), (short) -1, "sell_stock", batchNo, userId, userType,
                                        item.getOrderItem() == null ? "0" : item.getOrderItem().getOrderId(), item.getOrderItem() == null ? "0" : item.getOrderItem().getOrderItemId(), num, nowStock));
                            } else {
                                //数据库修改失败，回退
                                setSellStockCache(item.getProId(), num);
                                status.setStatus(false);
                                status.setCode(StockErrorCode.dbStockCode);
                                flag++;
                                logger.warn("货到付款-管理员发货-修改库存数据库失败", item, "");
                            }
                        } else {
                            status.setStatus(false);
                            status.setCode(StockErrorCode.cacheStockCode);
                            flag++;
                            logger.warn("货到付款-管理员发货-修改库存缓存失败", item, "");
                        }
                    } else {
                        status.setStatus(false);
                        status.setCode(StockErrorCode.stressStockCode);
                        flag++;
                        logger.warn("货到付款-管理员发货-库存短缺", item, "");
                    }
                    //返回订单列表状态
                    status.getStockItem().setOrderItem(item.getOrderItem());
                    itemStatuses.add(status);
                    //修改搜素列表的索引
                    RedisSearchInfoUtil.addSearchInfo(item.getProId());
                } catch (ServiceException e) {
                    logger.error(new ServiceException("StockService-stockUpdateOnSendOutCOD-管理员订单发货(货到付款订单的发货类型)失败", e), item, "");
                    status.setStatus(false);
                    status.setCode(StockErrorCode.dbStockCode);
                    flag += 1;
                } finally {
                    lock.unlock();
                }
            }
            result.setStockItemStatusList(itemStatuses);
            if (flag > 0) {
                result.setStatus(false);
                //回退，增加商品库存
                for (StockItemStatus sitem : result.getStockItemStatusList()) {
                    Lock lock = StockLockUtil.get(sitem.getStockItem().getProId());
                    try {
                        lock.lock();
                        if (sitem.getStatus()) {
                            //查找缓存
                            int num = getSellStockCache(sitem.getStockItem().getProId());
                            //缓存增加数量不为负数，也就是没有超卖
                            boolean suc = setSellStockCache(sitem.getStockItem().getProId(), num + sitem.getStockItem().getBuyNum());
                            //缓存更新成功，修改数据库
                            if (suc) {
                                proStockDao.addStock(sitem.getStockItem().getProId(), sitem.getStockItem().getBuyNum());
                            }
                        }
                    } catch (ServiceException e) {
                        logger.error(new ServiceException("StockService-stockUpdateOnSendOutCOD-管理员订单发货(货到付款订单的发货类型)-商品回退失败", e), sitem, "");
                    } finally {
                        lock.unlock();
                    }
                    //修改搜素列表的索引
                    RedisSearchInfoUtil.addSearchInfo(sitem.getStockItem().getProId());
                }
            } else {
                result.setStatus(true);
                try {
                    //增加库存改变的业务日志
                    proStockLogDao.addStockLogOnBatch(logs);
                } catch (Exception e) {
                    logger.error(new ServiceException("StockService-addProStockLog-增加商品库存日志失败", e), logs, "");
                }
            }
        }
        addToResult(result, resultObj);
    }

    /**
     * 更新订单的订单行状态，取消该订单中占有的库存，可用库存增加，订单库存减少
     * 业务场景：取消订单（已支付未发货）
     *
     * @param items    订单库存列表
     * @param userId   前台用户ID或者后台管理员ID
     * @param userType 用户类型，用admin或者user表示
     * @return 返回更新库存的状态
     */
    public void stockUpdateOnOrderCancel(JsonObject resultObj, StockItem[] items, int userId, String userType) {
        boolean suc = false;
        if (items != null && items.length > 0) {
            String batchNo = "orderCancle_" + getRandBatchNo();
            List<ProStockLog> logs = new ArrayList<ProStockLog>();
            try {
                for (StockItem item : items) {
                    Lock lock = StockLockUtil.get(item.getProId());
                    try {
                        lock.lock();
                        //首先判断是否这个商品在提交订单时候减库存失败，这样在回滚库存时候就不需要在增加
                        if (item.getOrderItem().getStatus()) {
                            //获取可使用库存缓存，进行更新
                            int num = getSellStockCache(item.getProId());
                            int nowStock = num + item.getBuyNum();
                            boolean rs = setSellStockCache(item.getProId(), nowStock);
                            //緩存更新成功，更新数据库
                            if (rs) {
                                int count = proStockDao.addStockByOrderCancel(item.getProId(), item.getBuyNum());
                                if (count > 0) {
                                    //增加库存改变的业务日志
                                    logs.add(new ProStockLog(item.getProId(), item.getBuyNum(), (short) -1, "order_stock", batchNo, userId, userType,
                                            item.getOrderItem() == null ? "0" : item.getOrderItem().getOrderId(), item.getOrderItem() == null ? "0" : item.getOrderItem().getOrderItemId(), num, nowStock));
                                    logs.add(new ProStockLog(item.getProId(), item.getBuyNum(), (short) 1, "sell_stock", batchNo, userId, userType,
                                            item.getOrderItem() == null ? "0" : item.getOrderItem().getOrderId(), item.getOrderItem() == null ? "0" : item.getOrderItem().getOrderItemId(), num, nowStock));
                                } else {
                                    //数据库修改失败，回退
                                    setSellStockCache(item.getProId(), num);
                                    logger.warn("取消订单-库存回滚-修改库存数据库失败", item, "");
                                }
                            } else {
                                logger.warn("取消订单-库存回滚-修改库存缓存失败", item, "");
                            }
                        }
                    } catch (ServiceException e) {
                        logger.error(new ServiceException("StockService-stockUpdateOnOrderCancel-取消订单（已支付未发货)失败", e), item, "");
                    } finally {
                        lock.unlock();
                    }
                    //修改搜素列表的索引
                    RedisSearchInfoUtil.addSearchInfo(item.getProId());
                }
                suc = true;
                try {
                    //增加库存改变的业务日志
                    proStockLogDao.addStockLogOnBatch(logs);
                } catch (Exception e) {
                    logger.error(new ServiceException("StockService-addProStockLog-增加商品库存日志失败", e), logs, "");
                }
            } catch (Exception e) {
                logger.error(new ServiceException("StockService-stockUpdateOnOrderCancel-取消订单（已支付未发货)失败", e), items, "");
            }
        }
        addToResult(suc, resultObj);
    }

    /**
     * 通过商品ids获取商品库存列表
     *
     * @param resultObj
     * @param ids
     */
    public void getListByIds(JsonObject resultObj, int[] ids) {
        List<ProStock> list = proStockDao.getListByIds(ids);
        addToResult(list, resultObj);
    }

    private boolean checkStock(int proId, int stock) {
        boolean suc = false;
        //先查找缓存
        int num = getSellStockCache(proId);
        if (num >= stock) {
            suc = true;
        }
        return suc;
    }

    private int getSellNum(int proId) {
        ProStock stock = proStockDao.getEntityById(proId, true);
        return stock != null ? stock.getSellStock() : 0;
    }

    /**
     * setSellStockCache 商品可用库存缓存
     *
     * @param proId
     * @param value
     * @return
     */
    private boolean setSellStockCache(int proId, int value) {
        try {
            ShardJedisTool.getInstance().set(JedisKey.stock$sell, proId, value);
            return true;
        } catch (JedisClientException e) {
            logger.error(new ServiceException("PMS-Stock-SellStockCache-设置商品可用库缓存存失败", e), "(proId:" + proId + ",stockNum:" + value + ")", "");
            return false;
        }
    }

    /**
     * 获取缓存
     *
     * @param proId
     * @return
     */
    private int getSellStockCache(int proId) {
        String num = null;
        try {
            num = ShardJedisTool.getInstance().get(JedisKey.stock$sell, proId);
        } catch (Exception e) {
            logger.error(new ServiceException("PMS-Stock-getStockCache-获取商品可用库缓存存失败", e), "(proId:" + proId + ")", "");
        }
        if (StringUtils.isEmpty(num)) {
            int count = getSellNum(proId);
            Lock lock = StockLockUtil.get(proId);
            try {
                lock.lock();
                setSellStockCache(proId, count);
            } catch (Exception e) {
                logger.error(new ServiceException("PMS-Stock-setSellStockCache-设置商品可用库缓存存失败", e), "(proId:" + proId + ")", "");
            } finally {
                lock.unlock();
            }
            return count;
        } else {
            return Integer.parseInt(num);
        }


    }

    //获取日志的流水编号
    public String getRandBatchNo() {
        return DateUtil.formatDate(new Date(), "yyyyMMddHHmmss") + RandomStringUtils.randomAlphanumeric(4);
    }

    /**
     * 更新可使用库存缓存用
     */

    private static boolean initSellStockCache() {
        List<ProStock> list = ((ProStockDao) SpringContextUtil.getBean("proStockDaoImpl")).getListByObj(null);
        boolean suc = false;
        try {
            for (ProStock stock : list) {

                try {
                    ShardJedisTool.getInstance().set(JedisKey.stock$sell, stock.getProId(), stock.getSellStock());
                } catch (Exception e) {
                    logger.error(new ServiceException("PMS-Stock-SellStockCache-设置商品可用库缓存存失败", e), stock, "");
                }

            }
            suc = true;
        } catch (Exception e) {
            logger.error(new ServiceException("PMS-Stock-SellStockCache-设置商品可用库缓存存失败", e), list, "");
        }
        return suc;
    }

    public static void main(String[] args) {
        String[] xmls = {"conf/spring/commons-core.xml", "conf/spring/stock-core.xml", "conf/spring/stock-service-integration.xml", "conf/spring/stock-service-normal-impl.xml"};
        new ClassPathXmlApplicationContext(xmls);
        boolean suc = initSellStockCache();
        if (suc) {
            System.out.println("###########:success");
        } else {
            System.out.println("###########:failure");
        }
    }

    /**
     * 数据库商品可使用库存与缓存比对，如果不等，重新设置缓存
     */
    public void checkDBWithCacheByStock(){
        boolean suc = true;
        int first = 1;
        int endIndex = 1000;
        Map<Integer, String> map = new HashMap<Integer, String>();
        while (suc) {
            PagerControl<ProStock> pc = proStockDao.getPagerByObj(null, new PageInfo(first, endIndex), null, null);
            if (pc.getEntityList() != null && pc.getEntityList().size() > 0) {
                for (ProStock stock : pc.getEntityList()) {
                    Lock lock = StockLockUtil.get(stock.getProId());
                    try {
                        lock.lock();
                        //获取数据库的商品可使用库存
                        int dbNum = getSellNum(stock.getProId());
                        //获取缓存可使用库存
                        String cacheNum = null;
                        try {
                            cacheNum = ShardJedisTool.getInstance().get(JedisKey.stock$sell, stock.getProId());
                        } catch (Exception e) {
                            logger.error(new ServiceException("PMS-Stock-checkDBWithCacheByStock-获取商品可用库缓存存失败", e), "(proId:" + stock.getProId() + ")", "");
                        }
                        //校验数据库与缓存可使用库存是否相等情况
                        if (!StringUtils.isEmpty(cacheNum) && dbNum == Integer.parseInt(cacheNum)) {
                        } else {
                            map.put(stock.getProId(), "数据库值-" + dbNum + ":缓存值-" + cacheNum);
                        }
                    } catch (Exception e) {
                        logger.error(new ServiceException("PMS-Stock-checkDBWithCacheByStock-比对缓存可使用库存失败", e), "(proId:" + stock.getProId() + ")", "");
                    } finally {
                        lock.unlock();
                    }
                }
                first += 1;
                endIndex += 1000;
            } else {
                suc = false;
            }
        }
        if (map != null && map.size() > 0) {
            String mailUrl = getSellStockMail();
            if (StringUtils.isNotEmpty(mailUrl)) {
                String sendContent = "您好！商品可销售库存数据库与缓存值不等,对应环境为:"+IPUtil.getLocalIp()+",对应的数据为:" + map.toString();
                MailEntity mail = new MailEntity();
                mail.setSender("admin@meiliwan.com");
                mail.setSenderName("美丽湾管理员");
                mail.setReceivers(mailUrl);
                mail.setTitle("商品可销售库存-数据库与缓存不相等");
                mail.setContent(sendContent);
                BaseMailAndMobileClient.sendMail(mail);
            }
            logger.warn("PMS-Stock-checkDBWithCacheByStock-比对缓存可使用库存，数据库与缓存不等", "对应的数据为:" + map.toString(), "");
        }
    }

    /**
     * 获取商品安全库存情况，用于库存报警
     *
     * @return
     */
    public void getSafeStockList(JsonObject resultObj) {
        addToResult(proStockDao.getSafeStockList(), resultObj);
    }

    /**
     * 修改安全库存int
     *
     * @param resultObj
     * @param id
     * @param num
     */
    public void updateSafeStock(JsonObject resultObj, int id, int num) {
        int count = proStockDao.updateSafeStock(id, num);
        boolean suc = false;
        if (count > 0) {
            suc = true;
        }
        addToResult(suc, resultObj);
    }

    /**
     * 导入商品库存业务逻辑
     *
     * @param resultObj
     * @param stocks
     */
    public void importExcel(JsonObject resultObj, StockImportLog[] stocks, String fileName, int adminId, String adminName) {
        //不匹配数量
        int mismatch = 0;
        //库存不足数量
        int shortNum = 0;
        //错误数
        int errNum = 0;
        //导入总数
        int totalNum = stocks.length;
        //表示导入库存操作
        String batchNo = "importStock_" + getRandBatchNo();
        //库存业务日志记录
        List<ProStockLog> logs = new ArrayList<ProStockLog>();

        String logBatchNo = fileName.split("\\.")[0];
        StockImportResult result = new StockImportResult();
        for (StockImportLog log : stocks) {
            SafeStockItem item = proStockDao.getProIdByBarCode(log.getBarCode());
            if (item == null) {
                //获取不到对应的商品，log
                mismatch++;
                log.setDescp("未找到匹配商品");
                log.setState((short) -1);
            } else {
                //获取商品相关的锁 && op
                Lock lock = StockLockUtil.get(item.getProId());
                try {
                    lock.lock();
                    int stock = getSellStockCache(item.getProId());
                    int finalSellStock = stock + log.getChangeStock();
                    boolean dbop = true;
                    int count = 0;
                    //判断库存是否短缺情况
                    if (finalSellStock >= 0) {
                        boolean suc = setSellStockCache(item.getProId(), finalSellStock);
                        if (suc) {
                            try {
                                if (log.getChangeStock() < 0) {
                                    // 增加商品的不可使用库存情况
                                    count = proStockDao.addUnsellStock(item.getProId(), Math.abs(log.getChangeStock()));
                                    if (count > 0) {
                                        logs.add(new ProStockLog(item.getProId(), Math.abs(log.getChangeStock()), (short) -1, "sell_stock", batchNo, adminId, "admin", "0", "0", stock, finalSellStock));
                                        logs.add(new ProStockLog(item.getProId(), Math.abs(log.getChangeStock()), (short) 1, "unsell_stock", batchNo, adminId, "admin", "0", "0", stock, finalSellStock));
                                    }
                                } else if (log.getChangeStock() > 0) {
                                    // 增加商品的总库存情况
                                    count = proStockDao.addStock(item.getProId(), log.getChangeStock());
                                    if (count > 0) {
                                        logs.add(new ProStockLog(item.getProId(), log.getChangeStock(), (short) 1, "sell_stock", batchNo, adminId, "admin", "0", "0", stock, finalSellStock));
                                        logs.add(new ProStockLog(item.getProId(), log.getChangeStock(), (short) 1, "stock", batchNo, adminId, "admin", "0", "0", stock, finalSellStock));
                                    }
                                }
                            } catch (ServiceException e) {
                                dbop = false;
                                logger.error(new ServiceException("StockService-importStock-修改数据库失败", e), log, "");
                            }
                            if (count <= 0) {
                                //库存回滚
                                setSellStockCache(item.getProId(), stock);
                                dbop = false;
                            }
                        } else {
                            dbop = false;
                            //修改缓存失败情况
                            logger.warn("StockService-importStock-修改缓存失败", log, "");
                        }
                        if (dbop) {
                            log.setDescp("导入成功");
                            log.setNowStock(finalSellStock);
                            log.setState((short) 1);
                        }
                    } else {
                        // 商品可使用库存不足情况如下
                        boolean suc = setSellStockCache(item.getProId(), 0);
                        if (suc) {
                            try {
                                // 增加商品的不可使用库存情况
                                count = proStockDao.addUnsellStock(item.getProId(), stock);
                                if (count > 0) {
                                    logs.add(new ProStockLog(item.getProId(), stock, (short) -1, "sell_stock", batchNo, adminId, "admin", "0", "0", stock, 0));
                                    logs.add(new ProStockLog(item.getProId(), stock, (short) 1, "unsell_stock", batchNo, adminId, "admin", "0", "0", stock, 0));
                                }
                            } catch (Exception e) {
                                dbop = false;
                                logger.error(new ServiceException("StockService-importStock-修改数据库失败", e), log, "");
                            }
                            if (count <= 0) {
                                //库存回滚
                                setSellStockCache(item.getProId(), stock);
                                dbop = false;
                            }
                        } else {
                            dbop = false;
                            //修改缓存失败情况
                            logger.warn("StockService-importStock-修改缓存失败", log, "");
                        }
                        if (dbop) {
                            log.setDescp("库存不足");
                            log.setNowStock(0);
                            log.setState((short) 0);
                            //库存不足增加1
                            shortNum++;
                        }
                    }
                    if (!dbop) {
                        log.setDescp("导入失败");
                        log.setNowStock(stock);
                        log.setState((short) 2);
                        //导入失败增加1
                        errNum++;
                    }
                    log.setOriginalStock(stock);
                } catch (Exception e) {
                    logger.error(new ServiceException("StockService-importStock-导入数据失败", e), log, "");
                } finally {
                    lock.unlock();
                }
            }
            log.setBatchNo(logBatchNo);
        }
        //导入结果如下,记入日志
        result.setAdminId(adminId);
        result.setAdminName(adminName);
        result.setBatchNo(logBatchNo);
        result.setFileName(fileName);
        result.setTotalNum(totalNum);
        result.setMismatchNum(mismatch);
        result.setStockShortNum(shortNum);
        result.setErrorNum(errNum);
        result.setImportTime(new Date());
        if (shortNum > 0 || mismatch > 0) {
            result.setState((short) 0);
            result.setDescp("导入商品" + totalNum + "条，成功条数" + (totalNum - mismatch - shortNum - errNum) + "条，未能匹配商品" + mismatch + "条，库存不足商品" + shortNum + "条，错误导入失败" + errNum + "条");
        } else {
            result.setState((short) 1);
            result.setDescp("导入成功");
        }
        try {
            stockImportResultDao.insert(result);
            proStockLogDao.addStockLogOnBatch(logs);
            stockImportLogDao.addStockImportLogOnBatch(stocks);
        } catch (ServiceException e) {
            logger.error(new ServiceException("StockService-importStock-导入商品库存-增加相关的业务日志失败", e), stocks, "");
        }

        addToResult(result, resultObj);
    }

    /**
     * 查看导入记录
     *
     * @param resultObj
     * @param result
     * @param pageInfo
     */
    public void getPageImportResult(JsonObject resultObj, StockImportResult result, PageInfo pageInfo) {
        PagerControl<StockImportResult> pc = stockImportResultDao.getPagerByObj(result, pageInfo, null, "order by import_time desc");
        addToResult(pc, resultObj);
    }

    /**
     * 通过批次号，获取导入结果
     *
     * @param resultObj
     * @param batchNo
     */
    public void getResultByBatchNo(JsonObject resultObj, String batchNo) {
        StockImportResult result = stockImportResultDao.getResultByBatchNo(batchNo);
        addToResult(result, resultObj);
    }

    /**
     * 获取对应批次导入明细，包括每条商品的导入情况
     *
     * @param resultObj
     * @param log
     */
    public void getImportLogsByEntity(JsonObject resultObj, StockImportLog log) {
        List<StockImportLog> list = stockImportLogDao.getListByObj(log);
        addToResult(list, resultObj);
    }

    public String getSellStockMail() {
        String value = null;
        try {
            byte[] values = new byte[0];
            values = ZKClient.get().getData(PROPERTIES_ZK);
            ByteArrayInputStream stream = new ByteArrayInputStream(values);
            Properties properties = new Properties();
            properties.load(stream);
            value = properties.getProperty(STOCKKEY);
        } catch (KeeperException e) {
            logger.warn("PMS-Stock-checkDBWithCacheByStock-比对缓存可使用库存，获取发送邮件地址失败", value, "");
        } catch (InterruptedException e) {
            logger.warn("PMS-Stock-checkDBWithCacheByStock-比对缓存可使用库存，获取发送邮件地址失败", value, "");
        } catch (IOException e) {
            logger.warn("PMS-Stock-checkDBWithCacheByStock-比对缓存可使用库存，获取发送邮件地址失败", value, "");
        }
        return value;
    }

}

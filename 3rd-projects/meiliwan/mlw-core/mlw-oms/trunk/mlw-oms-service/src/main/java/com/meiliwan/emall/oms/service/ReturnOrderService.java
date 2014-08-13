package com.meiliwan.emall.oms.service;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.meiliwan.emall.commons.util.IPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.meiliwan.emall.annotation.IceServiceMethod;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.bean.PayCode;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.util.IDGenerator;
import com.meiliwan.emall.commons.util.NumberUtil;
import com.meiliwan.emall.oms.bean.Ord;
import com.meiliwan.emall.oms.bean.OrdLogistics;
import com.meiliwan.emall.oms.bean.Ordi;
import com.meiliwan.emall.oms.bean.Retord;
import com.meiliwan.emall.oms.bean.RetordLogs;
import com.meiliwan.emall.oms.constant.Constant;
import com.meiliwan.emall.oms.constant.OrdIRetStatus;
import com.meiliwan.emall.oms.constant.OrdITotalStatus;
import com.meiliwan.emall.oms.constant.RetPayType;
import com.meiliwan.emall.oms.constant.RetType;
import com.meiliwan.emall.oms.constant.RetordCreateType;
import com.meiliwan.emall.oms.constant.TransportCompany;
import com.meiliwan.emall.oms.dao.OrdDao;
import com.meiliwan.emall.oms.dao.OrdLogisticsDao;
import com.meiliwan.emall.oms.dao.OrdiDao;
import com.meiliwan.emall.oms.dao.RetordDao;
import com.meiliwan.emall.oms.dao.RetordLogsDao;
import com.meiliwan.emall.oms.dto.export.RetOrdReportRow;
import com.meiliwan.emall.pay.bean.PayHead;
import com.meiliwan.emall.pay.bean.PayItem;
import com.meiliwan.emall.pay.client.PayClient;
import com.meiliwan.emall.pay.constant.PayType;
import com.meiliwan.emall.pms.bean.SimpleProduct;
import com.meiliwan.emall.pms.client.ProProductClient;

/**
 * Created by Sean on 13-10-2.
 */
@Service("returnOrderService")
public class ReturnOrderService extends BaseOrderService {
    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());
    private final static Multimap<Integer, Integer> flowStateMap = ArrayListMultimap.create();

    @Autowired
    private RetordLogsDao retordLogsDao;
    @Autowired
    private RetordDao retordDao;
    @Autowired
    private OrdiDao ordiDao;
    @Autowired
    private OrdDao ordDao;
    @Autowired
    private OrdLogisticsDao logisticsDao;

    /**
     * 获取退换货信息
     * @param resultObj
     */
    @IceServiceMethod
    public void getRetordPagerByRetord(JsonObject resultObj, Retord retord, PageInfo pageInfo, String whereSql) {
        addToResult(retordDao.getPagerByObj(retord, pageInfo, whereSql), resultObj);
    }

    private String genOrderId(){
        int id = ordDao.nextOrdSeq();
        return IDGenerator.getId(id, ORDERID_LENGTH);
    }

    /**
     * 添加退换货信息
     * @param resultObj
     * @param retord
     * @param ordiStr   商品Id_
     */
    @IceServiceMethod
    public void addRetord(JsonObject resultObj, Retord retord, String[] ordiStr, String addMan) {
        try {
            //验证退换货的 商品种类
            if (ordiStr.length == 0) {
                //没有选择退换货的商品行
                addToResult(-2, resultObj);
            }
            //获取正向订单
            Ord ord = ordDao.getEntityById(retord.getOldOrderId(), true);
            //验证 订单，和用户对象操作是否合法
            if (ord != null && retord.getUid() != null && ord.getUid().equals(retord.getUid())) {
                //生成退换货逆向订单ID
                ord.setOrderId(genOrderId());
                retord.setRetordOrderId(ord.getOrderId());
                //设置状态为退换货订单状态
                ord.setBillType(Constant.ORDER_BILL_TYPE_REVERSE);
            } else {
                throw new ServiceException("ordiStr传入订单与用户ID不匹配,returnOrderService-->addRetord()");
            }
            //订单行索引

            Map<String, Integer> map = new HashMap<String, Integer>();
            List<Ordi> ordiList = ordiDao.getOrdIListByOrdId(retord.getOldOrderId(), true);
            for (String ordi : ordiStr) {
                if (!Strings.isNullOrEmpty(ordi)) {
                    //str[0] 订单行id  str[1]数量
                    String[] str = ordi.split("_");
                    //判断申请数量的合法性
                    for (Ordi o : ordiList) {
                        if (str[0].equals(o.getOrderItemId())) {
                            if (Integer.parseInt(str[1]) < 1 || Integer.parseInt(str[1]) > o.getSaleNum()) {
                                addToResult(-1, resultObj);
                                return;
                            }
                        }
                    }
                    if (str.length == 2) {
                        map.put(str[0], Integer.parseInt(str[1]));
                    }
                }
            }
            int i = 0;
            for (Ordi ordi : ordiList) {
                i++;
                Ordi ordiObj = ordi;
                if (ordiObj != null) {
                    if (map.containsKey(ordi.getOrderItemId())) {
                        ordiObj.setState((short) 1);
                        ordiObj.setSaleNum(map.get(ordi.getOrderItemId()));
                    } else {
                        ordiObj.setState((short) 0);
                        ordiObj.setSaleNum(0);
                    }
                    //订单行 id生成     调用统一的生成订单行ID方法 modify by yuxiong 2013-10-15
                    ordiObj.setOrderItemId(getOrderItemId(ord.getOrderId(), i));
                    //表示 逆向订单状态
                    ordiObj.setBillType(Constant.ORDER_BILL_TYPE_REVERSE);
                    //申请退换货数量
                    ordiObj.setCreateTime(new Date());
                    ordiObj.setOrderId(ord.getOrderId());
                }
                //插入逆向订单行
                ordiDao.insert(ordiObj);
            }

            //退款申请状态
            synchronizedState(retord, ord, retord.getCreateType().equals(RetordCreateType.CREATE_TYPE_USER.getCode()) ? OrdIRetStatus.RET_APPLY : OrdIRetStatus.RET_BUYER_WAIT_CONSIGNMENT);
            //插入逆向订单
            ordDao.insert(ord);

            //插入退换货申请
            Timestamp time = DateUtil.getCurrentTimestamp();
            retord.setCreateTime(time);
            //如果是用户发起的申请
            if (retord.getCreateType() == 0) {
                retord.setRetPayAmount(0d);
                retord.setRetPayCompensate(0d);
                retord.setRetPayFare(0d);
                retord.setRetTotalAmount(0d);
            }
            retordDao.insert(retord);
            //插入日志
            if(retord.getCreateType().equals(RetordCreateType.CREATE_TYPE_ADMIN.getCode())){
            	insertRetLogs(ord.getOrderId(), retord.getOldOrderId(), time, retord.getApplyComments(), retord.getRetStatus(), addMan, "发起申请");
            }
            insertRetLogs(ord.getOrderId(), retord.getOldOrderId(), time, retord.getCreateType().equals(RetordCreateType.CREATE_TYPE_USER.getCode()) ? "您的退/换货申请已提交，正在等待相关售后人员审核" : "美丽湾客服为您提交退换货申请，请按下方的退货地址将商品寄回", retord.getRetStatus(), retord.getCreateType().equals(RetordCreateType.CREATE_TYPE_USER.getCode()) ? "客户" : addMan, "发起申请");
            addToResult(1, resultObj);
        } catch (Exception e) {
            addToResult(-1, resultObj);
            throw new ServiceException("用户发起退换货申请时出现服务异常，returnOrderService-->addRetord()", e.getMessage(),e);
        }
    }

    /**
     * 修改等待审核的退换货详情
     * @param resultObj
     * @param retord
     * @param ordisArray
     * @throws Exception
     */
    @IceServiceMethod
    public void updateRetordByUser(JsonObject resultObj, Retord retord, String[] ordisArray) throws Exception {
        try {
            if (retord != null && ordisArray != null && retord.getRetordOrderId() != null) {
                List<Ordi> ordiList = ordiDao.getOrdIListByOrdId(retord.getRetordOrderId(), true);
                List<Ordi> oldList = ordiDao.getOrdIListByOrdId(retord.getOldOrderId(), true);
                Map<String, Integer> map = new HashMap<String, Integer>();
                for (String ordi : ordisArray) {
                    //str[0] 订单行id  str[1]数量
                    String[] str = ordi.split("_");
                    if (str.length == 2 && ordiList != null && ordiList.size() > 0) {
                        //判断申请数量是否小于原订单购买数量
                        for (Ordi o : ordiList) {
                            //将新订单行赋值为原订单行的销售数量，用于一下做判断比较
                            for (Ordi oo : oldList) {
                                if (oo.getProId().equals(o.getProId())) {
                                    o.setSaleNum(oo.getSaleNum());
                                }
                            }
                            if (str[0].equals(o.getOrderItemId())) {
                                if (Integer.parseInt(str[1]) < 1 || Integer.parseInt(str[1]) > o.getSaleNum()) {
                                    logger.warn("用户提交的退换货商品数量非法", "uid(" + retord.getUid() + "), proId(" + o.getProId() + "), saleCount(" + o.getSaleNum() + "), applyCount(" + str[1] + ")", null);
                                    addToResult(false, resultObj);
                                    return;
                                }
                            }
                        }
                        map.put(str[0], Integer.parseInt(str[1]));
                    } else {
                        throw new Exception("传入订单行数据错误");
                    }
                }
                for (Ordi oi : ordiList) {
                    if (oi != null) {
                        if (map.containsKey(oi.getOrderItemId())) {
                            oi.setState((short) 1);
                            oi.setSaleNum(map.get(oi.getOrderItemId()));
                        } else {
                            oi.setState((short) 0);
                            oi.setSaleNum(0);
                        }
                        //修改状态
                        ordiDao.update(oi);
                    }
                }
                retord.setRetStatus(OrdIRetStatus.RET_APPLY.getCode());
                int result = retordDao.update(retord);
                addToResult(result > 0 ? true : false, resultObj);
            }
        } catch (Exception e) {
            addToResult(false, resultObj);
            throw new ServiceException("用户修改等待审核的退换货详情时出现服务异常，returnOrderService-->updateRetordByUser()", e.getMessage());
        }
    }

    /**
     * 写入退换货日志
     * @param retOrderId
     * @param oldOrderId
     * @param optTime
     * @param optContent
     * @param optId
     * @param optMan
     * @param optResult
     */
    private void insertRetLogs(String retOrderId, String oldOrderId, Timestamp optTime, String optContent, String optId, String optMan, String optResult) {
        RetordLogs logs = new RetordLogs();
        logs.setRetOrderId(retOrderId);
        logs.setOldOrderId(oldOrderId);
        logs.setCreateTime(optTime);
        logs.setOptContent(optContent);
        logs.setOptId(optId);
        logs.setOptMan(optMan);
        logs.setOptResult(optResult);
        logs.setOptStatusCode(OrdIRetStatus.RET_APPLY.getCode());
        retordLogsDao.insert(logs);
    }

    /**
     * 同步 退换货状态
     * @param retord
     * @param ord
     * @param status
     */
    private void synchronizedState(Retord retord, Ord ord, OrdIRetStatus status) {
        ord.setOrderStatus(status.getCode());
        retord.setRetStatus(status.getCode());
    }

    /**
     * 修改退换货信息 如状态等：初始化需要完善业务
     * @param resultObj
     */
    @IceServiceMethod
    public void updateRetord(JsonObject resultObj, Retord retord) {
        addToResult(retordDao.update(retord), resultObj);
    }


    /**
     * 写入逆向订单操作日志
     * @param resultObj
     * @param logs
     */
    @IceServiceMethod
    public void createRetApplyLogs(JsonObject resultObj, RetordLogs logs) {
        if (logs != null && logs.getRetOrderId() != null) {
            logs.setCreateTime(new Date());
            int result = retordLogsDao.insert(logs);
            addToResult(result > 0 ? true : false, resultObj);
        }
    }

    /**
     * 获得逆向订单日志操作列表
     * @param resultObj
     * @param retId
     */
    @IceServiceMethod
    public void getRetordLogsListByRetId(JsonObject resultObj, String retId) {
        if (!Strings.isNullOrEmpty(retId)) {
            RetordLogs logs = new RetordLogs();
            logs.setRetOrderId(retId);
            addToResult(retordLogsDao.getListByObj(logs), resultObj);
        }
    }

    /**
     * 用户获得申请退换货记录
     * @param resultObj
     */
    @IceServiceMethod
    public void getRetordPagerByUid(JsonObject resultObj, PageInfo pageInfo, int uid) {
        try {
            if (uid > 0) {
                Retord retord = new Retord();
                retord.setUid(uid);
                PagerControl<Retord> pc = retordDao.getPagerByObj(retord, pageInfo, null, "order by create_time desc");
                if (pc != null && pc.getEntityList() != null && pc.getEntityList().size() > 0) {
                    for (Retord ret : pc.getEntityList()) {
                        if (ret != null && ret.getRetordOrderId() != null) {
                            ret.setOrdiList(ordiDao.getOrdIListByOrdId(ret.getRetordOrderId()));
                        }
                    }
                }
                addToResult(pc, resultObj);
            }
        } catch (Exception e) {
            addToResult((JsonArray) null, resultObj);
            throw new ServiceException("用户个人中心查询退换货记录时，服务端方法出现异常，returnOrderService-->getRetordPagerByUid()", e.getMessage());
        }
    }


    /**
     * 获得退换货详情
     * 包括处理业务逻辑
     * @param resultObj
     * @param id
     */
    @IceServiceMethod
    public void getRetordById(JsonObject resultObj, String id) {
        try {
            if (!Strings.isNullOrEmpty(id)) {
                Retord ret = retordDao.getEntityById(id);
                if (ret != null) {
                    List<Ordi> ordiList = ordiDao.getOrdIListByOrdId(ret.getRetordOrderId());
                    List<Ordi> oldOrdiList = ordiDao.getOrdIListByOrdId(ret.getOldOrderId());
                    //查询原订单行销售数量，退换货详情页展示用到
                    if (ordiList != null && ordiList.size() > 0 && oldOrdiList != null && oldOrdiList.size() > 0) {
                        for (Ordi o : ordiList) {
                            if (o != null && o.getProId() != null) {
                                SimpleProduct product = ProProductClient.getProductById(o.getProId());
                                if (product != null && product.getBarCode() != null) {
                                    o.setProBarCode(product.getBarCode());
                                }
                                for (Ordi oo : oldOrdiList) {
                                    if (oo != null && oo.getProId() != null) {
                                        if (o.getProId().equals(oo.getProId())) {
                                            //将proCateId暂时用来存储原订单行销售数量，用于页面展示
                                            o.setProCateId(oo.getSaleNum());
                                        /*//将proSellType暂时用来原订单是否已经申请过退换货，(前台更改投诉页面用到)
                                        oo.setProSellType((short)1);//1表示申请过，(前台更改投诉页面用到)
                                        //将proCateId暂时用来存储申请退换货的数量，(前台更改投诉页面用到)
                                        oo.setProCateId(o.getSaleNum());*/
                                        } /*else {
                                        oo.setProSellType((short)0);//0表示还未申请，(前台更改投诉页面用到)
                                    }*/
                                    }
                                }
                            }
                        }
                    }
                    ret.setOrdiList(ordiList);
                /*ret.setOldOrdiList(oldOrdiList);*/
                    RetordLogs logs = new RetordLogs();
                    logs.setRetOrderId(id);
                    ret.setRetLogsList(retordLogsDao.getListByObj(logs));
                }
                addToResult(ret, resultObj);
            }
        } catch (Exception e) {
            addToResult((JsonArray) null, resultObj);
            throw new ServiceException("服务端获得退换货详情方法出现异常，returnOrderService-->getRetordById()", e.getMessage());
        }
    }

    /**
     * 获得干净的退换货对象，只是这个对象，没有处理逻辑,没有日志
     * @param resultObj
     * @param id
     */
    @IceServiceMethod
    public void getCleanRetordById(JsonObject resultObj, String id) {
        if (!Strings.isNullOrEmpty(id)) {
            addToResult(retordDao.getEntityById(id), resultObj);
        }
    }

    @IceServiceMethod
    public void getRetordListByOid(JsonObject resultObj, String oid) {
        Retord ret = new Retord();
        ret.setOldOrderId(oid);
        addToResult(retordDao.getListByObj(ret), resultObj);
    }

    /**
     * 判断用户是否已经申请过(判断是有存在正在处理的申请)
     * @param resultObj
     * @param oid
     */
    @IceServiceMethod
    public void getIsRepeatByOrdId(JsonObject resultObj, String oid) {
        if (!Strings.isNullOrEmpty(oid)) {
            Ord ord = ordDao.getEntityById(oid);
            //判断订单是否适合申请退换货
            if (ord != null && (ord.getOrderStatus().equals(OrdITotalStatus.ORDI_FINISHED.getCode()) || ord.getOrderStatus().equals(OrdITotalStatus.ORDI_CONSINGMENT.getCode()))) {
                Retord ret = new Retord();
                ret.setOldOrderId(oid);
                List<Retord> retord = retordDao.getListByObj(ret);
                //判断该订单是否存在正在处理的退换货
                if (retord != null && retord.size() > 0) {
                    for (Retord r : retord) {
                        if (r.getIsEndNode() == 0) {
                            addToResult(true, resultObj);
                            return;
                        }
                    }
                }
                addToResult(false, resultObj);
            } else {
                addToResult(true, resultObj);
            }
        } else {
            addToResult(true, resultObj);
        }
    }

    /**
     * 判断用户是否已经申请过(该方法暂时未用到，但请保留)
     * （是否存在已经申请过退换货的商品，是否存在正在处理中的申请）
     * @param resultObj
     * @param oldOrdis
     */
    @IceServiceMethod
    public void getIsRepeatByOrdiIds(JsonObject resultObj, String[] oldOrdis) {
        try {
            if (oldOrdis == null) {
                addToResult(false, resultObj);
                return;
            }
            //当前需要申请退换货的订单行列表
            List<Ordi> currOrdiList = new ArrayList<Ordi>();
            String oldOrdId = null;
            for (String o : oldOrdis) {
                //str[0]原订单行id  str[1]数量
                String[] str = o.split("_");
                if (str.length == 2) {
                    Ordi ordi = ordiDao.getEntityById(str[0]);
                    currOrdiList.add(ordi);
                    oldOrdId = ordi.getOrderId();
                } else {
                    throw new Exception("传入订单行数据错误");
                }
            }
            Retord ret = new Retord();
            ret.setOldOrderId(oldOrdId);
            Retord retord = retordDao.getEntityByObj(ret);
            //如果该订单已经存在正在处理中的申请，则返回true
            if (retord != null && retord.getIsEndNode() == 0) {
                addToResult(true, resultObj);
                return;
            }
            if (retord != null) {
                List<Ordi> hasApply = ordiDao.getOrdIListByOrdId(retord.getRetordOrderId());
                if (hasApply != null && currOrdiList != null && hasApply.size() > 0 && currOrdiList.size() > 0) {
                    for (Ordi o : currOrdiList) {
                        for (Ordi oo : hasApply) {
                            //如果存在已经申请过的订单行，则返回true
                            if (o.getProId().equals(oo.getProId())) {
                                addToResult(true, resultObj);
                                return;
                            }
                        }
                    }
                }
            }
            addToResult(false, resultObj);
        } catch (Exception e) {
            addToResult(false, resultObj);
            throw new ServiceException("");
        }
    }


    //流程节点控制配置
    static {

        //等待审核-> 不通过
        flowStateMap.put(10, 12);
        //等待审核-> 不通过 -> 结束
        flowStateMap.put(12, 100);

        //等待审核->用户取消
        flowStateMap.put(10, 11);
        //等待审核->用户取消 ->结束
        flowStateMap.put(11, 100);

        //---------------------------------------------------  退货
        //等待审核-> 通过
        flowStateMap.put(10, 15);
        //等待审核-> 通过 -> 等待买家退货
        flowStateMap.put(15, 20);
        //等待审核-> 通过 -> 等待买家退货 -> 仓库确认收货
        flowStateMap.put(20, 40);
        //等待审核-> 通过 -> 等待买家退货 -> 仓库确认收货-> 等待退款
        flowStateMap.put(40, 70);
        //等待审核-> 通过 -> 等待买家退货 -> 仓库确认收货-> 等待退款 -> 财务退款操作 确认退款
        flowStateMap.put(70, 75);
        //等待审核-> 通过 -> 等待买家退货 -> 仓库确认收货-> 等待退款 -> 财务退款操作 确认退款 -> 退款完成
        flowStateMap.put(75, 80);
        //结束
        flowStateMap.put(80, 100);
        //---------------------------------------------------  退货

        //因为有节点合并，所以注释掉节点重复的代码
        //---------------------------------------------------  换货
        //退货
        //等待审核-> 通过 -> 等待买家退货
        //flowStateMap.put(15, 20);
        //等待审核-> 通过 -> 等待买家退货 -> 仓库确认收货
        //flowStateMap.put(20, 40);
        //等待审核-> 通过 -> 等待买家退货 -> 仓库确认收货->    等待发货
        flowStateMap.put(40, 45);
        //等待审核-> 通过 -> 等待买家退货 -> 仓库确认收货->    等待发货   ->已经发货
        flowStateMap.put(45, 48);
        //等待审核-> 通过 -> 等待买家退货 -> 仓库确认收货->    发货操作 等待买家确认收货
        flowStateMap.put(48, 60);
        //等待审核-> 通过 -> 等待买家退货 -> 仓库确认收货->  发货操作 等待买家确认收货 ->买家确认收货
       // flowStateMap.put(50, 60);
        //等待审核-> 通过 -> 等待买家退货 -> 仓库确认收货->  发货操作 等待买家确认收货 ->买家确认收货 ->退换货结束
        flowStateMap.put(60, 100);

        //如果需要补偿 用户运费 这里有个分支
        //等待审核-> 通过 -> 等待买家退货 -> 仓库确认收货->  发货操作 等待买家确认收货 ->买家确认收货 ->等待财务退款
        flowStateMap.put(60, 70);
        //等待审核-> 通过 -> 等待买家退货 -> 仓库确认收货->  发货操作 等待买家确认收货 ->买家确认收货 ->等待财务退款
        //-> 财务退款操作 确认退款
        //flowStateMap.put(70, 75);
        //等待审核-> 通过 -> 等待买家退货 -> 仓库确认收货->  发货操作 等待买家确认收货 ->买家确认收货 ->等待财务退款
        //-> 财务退款操作 确认退款   -> 退款完成
        //flowStateMap.put(75, 80);
        //结束
        //flowStateMap.put(80, 100);
        //--------------------------------------------------- 换货

        //等待审核-> 通过 -> 等待买家退货 -> 仓库拒绝收货
        flowStateMap.put(20, 41);
        //等待审核-> 通过 -> 等待买家退货 -> 仓库拒绝收货 -> 客服操作 取消退换货申请
        flowStateMap.put(41, 85);
        //等待审核-> 通过 -> 等待买家退货 -> 仓库拒绝收货 ->   客服操作 取消退换货申请 over
        flowStateMap.put(85, 100);

        //等待退款 客服取消
        flowStateMap.put(70, 85);

        //客服同意审核  还可以继续编辑 并且还是卖家等待确认收货状态
        flowStateMap.put(20, 18);

        //换货 发货状态， 客服可以编辑
        flowStateMap.put(45, 18);

        //等待审核-> 通过 -> 等待买家退货 -> 仓库确认收货 ->客服取消动作
        flowStateMap.put(20, 85);

        //等待退款  还可以继续编辑 并且还是等待退款
        flowStateMap.put(70, 18);
        //这一步是程序自动完成
        //flowStateMap.put(18, 20);

//        //换货买家确认收货，走到退款


    }


    /**
     * @param resultObj
     * @param curStepObj
     * @param retordLogs
     * @param nextState
     * @param optArgs
     */
    @IceServiceMethod
    public void optRetord(JsonObject resultObj, Retord curStepObj,
                          RetordLogs retordLogs, int nextState, String[] optArgs) {
        //表示正常
        boolean rState = false;
        //完成编辑赋值 以及操作行
        curStepObj = updateRetordObj(curStepObj, nextState);

        //验证流程 节点是否存在
        if (curStepObj == null || !flowStateCrt(Integer.parseInt(curStepObj.getRetStatus()), nextState)) {
            addToResult(-1, resultObj);
            return;
        }


        switch (nextState) {
            case 11:  //用户取消退换货
                rState = handleFlow_11(curStepObj, retordLogs);
                break;
            case 12:  //不通过审核
                rState = handleFlow_12(curStepObj, retordLogs);
                break;
            case 15:  //审核通过
                rState = handleFlow_15(curStepObj, retordLogs);
                break;
            case 18:  //后台人员编辑
                rState = handleFlow_18(curStepObj, retordLogs, optArgs);
                break;
            //等待买家退货  系统自动流程
            //case 20:
            //    handleFlow_20(curStepObj, retordLogs);
            //    break;
            case 40:  //仓库确认收货
                rState = handleFlow_40(curStepObj, retordLogs);
                break;
            case 41:  //仓库拒绝收货
                rState = handleFlow_41(curStepObj, retordLogs);
                break;
            //等待仓库发货  系统自动流程
            //case 45:
            //    handleFlow_45(curStepObj, retordLogs);
            //    break;
            case 48:  //仓库发货动作
                rState = handleFlow_48(curStepObj, retordLogs);
                break;
            //case 50:  //等待买家确认收货 系统自动流程
            //    handleFlow_50(curStepObj, retordLogs);
            //   break;
            case 60:  //买家确认收货 业务人员操作
                rState = handleFlow_60(curStepObj, retordLogs);
                break;
            case 70:  //等待退款 系统自动流程 /或编辑跳转
                handleFlow_70(curStepObj, retordLogs);
                break;
            case 75:  //财务退款操作
                rState = handleFlow_75(curStepObj, retordLogs, optArgs);
                break;
            //case 80:  //退换货已完成 系统自动流程
            //    handleFlow_80(curStepObj, retordLogs);
            //    break;
            case 85:  //客服取消退换货申请
                rState = handleFlow_85(curStepObj, retordLogs);
                break;
            //case 100:  //退换货结束 系统自动流程
            //    handleFlow_100(curStepObj, retordLogs);
            //    break;
            default:
                //不可能出现
                //System.out.println("default");
        }
        if (rState) {
            addToResult(1, resultObj);
        } else {
            addToResult(-1, resultObj);
        }

    }


    /**
     * 对form 赋值
     * @param userFormObj
     * @return
     */
    private Retord updateRetordObj(Retord userFormObj, int nextStatus) {
        Retord dbRetord = retordDao.getEntityById(userFormObj.getRetordOrderId(), true);
        if (dbRetord != null) {
            //审核同意 15     后台人员修改 18
            //表示15节点是修改数据
            if (nextStatus == 15 || nextStatus == 18) {
                dbRetord.setRetPayFare(userFormObj.getRetPayFare());
                dbRetord.setRetPayCompensate(userFormObj.getRetPayCompensate());
                dbRetord.setRetPayAmount(userFormObj.getRetPayAmount());
                double total = NumberUtil.add(dbRetord.getRetPayAmount(), dbRetord.getRetPayCompensate());
                total = NumberUtil.add(total, dbRetord.getRetPayFare());
                dbRetord.setRetTotalAmount(total);
                //修改卡支付金额
                dbRetord.setRetRealCardAmount(userFormObj.getRetRealCardAmount());
                dbRetord.setRetRealMlwAmount(userFormObj.getRetRealMlwAmount());
                dbRetord.setRetPayType(userFormObj.getRetPayType());
                dbRetord.setRetType(userFormObj.getRetType());
                dbRetord.setRetPayType(userFormObj.getRetPayType());
                dbRetord.setServiceRetAlipay(userFormObj.getServiceRetAlipay());
                dbRetord.setServiceRetAlipayName(userFormObj.getServiceRetAlipayName());
                dbRetord.setServiceRetBank(userFormObj.getServiceRetBank());
                dbRetord.setServiceRetCardName(userFormObj.getServiceRetCardName());
                dbRetord.setServiceRetCardNum(userFormObj.getServiceRetCardNum());
                dbRetord.setServiceRetOpenBank(userFormObj.getServiceRetOpenBank());
                dbRetord.setAdMobile(userFormObj.getAdMobile());
                dbRetord.setAdPhone(userFormObj.getAdPhone());
                dbRetord.setAdDetailAddr(userFormObj.getAdDetailAddr());
                dbRetord.setAdRecvName(userFormObj.getAdRecvName());
                // 用户打钩的 选项 会在这里遍历
                List<Ordi> list = userFormObj.getOrdiList();
                //把订单所有行 状态 status =0
                Map<String, Integer> selected = new HashMap<String, Integer>();
                for (Ordi obj : list) {
                    selected.put(obj.getOrderItemId(), obj.getSaleNum());
                }
                List<Ordi> existObj = ordiDao.getOrdIListByOrdId(dbRetord.getRetordOrderId(), true);
                for (Ordi obj : existObj) {
                    if (selected.containsKey(obj.getOrderItemId())) {
                        obj.setState((short) 1);
                        obj.setSaleNum(selected.get(obj.getOrderItemId()));
                    } else {
                        obj.setState((short) 0);
                        obj.setSaleNum(0);
                    }
                    ordiDao.update(obj);
                }
            }
            return dbRetord;
        }
        return null;
    }

    /**
     * 用户取消
     * 处理节点 11业务
     */
    private boolean handleFlow_11(Retord curStepObj, RetordLogs retordLogs) {
        retordLogs.setOptContent("您取消了退/换货申请");
        curStepObj.setIsEndNode(1);
        return defaultHandleFlow(curStepObj, retordLogs, OrdIRetStatus.RET_BUYER_OPT_CANCEL);
        //转向 100业务流程
        // return handle ? handleFlow_100(curStepObj, retordLogs) : false;
    }

    /**
     * 拒绝申请业务
     * 处理节点 12业务
     */
    private boolean handleFlow_12(Retord curStepObj, RetordLogs retordLogs) {
        curStepObj.setIsEndNode(1);
        return defaultHandleFlow(curStepObj, retordLogs, OrdIRetStatus.RET_APPLY_REJECTED);
        //转向 100业务流程
        // return handle ? handleFlow_100(curStepObj, retordLogs) : false;
    }


    /**
     * 同意退换货流程
     * 处理节点15业务
     */
    private boolean handleFlow_15(Retord curStepObj, RetordLogs retordLogs) {
        //retordLogs.setOptContent("客服同意退换货");
        boolean handle = defaultHandleFlow(curStepObj, retordLogs, OrdIRetStatus.RET_APPLY_PASSED);
        //转向 20业务流程
        return handle ? handleFlow_20(curStepObj, retordLogs) : false;
    }

    /**
     * 后台人员编辑
     * 处理节点18业务
     */
    private boolean handleFlow_18(Retord curStepObj, RetordLogs retordLogs, String[] optArgs) {
        String curStep = curStepObj.getRetStatus();
        boolean handle = defaultHandleFlow(curStepObj, retordLogs, OrdIRetStatus.RET_SELLER_OPT_EDIT);
        //等待买家邮寄到仓库 编辑
        if ("20".equals(curStep)) {
            //转向 20业务流程
            return handle ? handleFlow_20(curStepObj, retordLogs) : false;
        }
        //等待退款中 编辑
        if ("70".equals(curStep)) {
            //转向 70业务流程
            return handle ? handleFlow_70(curStepObj, retordLogs) : false;
        }
        //等待退款中 编辑
        if ("45".equals(curStep)) {
            //转向 70业务流程
            return handle ? handleFlow_45(curStepObj, retordLogs) : false;
        }

        return false;
    }


    /**
     * 等待买家邮寄到仓库
     * 处理节点20业务
     * @return
     */
    private boolean handleFlow_20(Retord curStepObj, RetordLogs retordLogs) {
        retordLogs.setOptMan("系统");
        retordLogs.setOptContent("请客户尽快邮寄到客服指定的地址");
        return defaultHandleFlow(curStepObj, retordLogs, OrdIRetStatus.RET_BUYER_WAIT_CONSIGNMENT);
    }


    /**
     * 仓库确认收到客户退换货
     * 处理节点40业务
     * @return
     */
    private boolean handleFlow_40(Retord curStepObj, RetordLogs retordLogs) {
        boolean handle = defaultHandleFlow(curStepObj, retordLogs, OrdIRetStatus.RET_SELLER_RECEIPTED);
        //分支操作
        //是否是换货
        if (RetType.CHG.getCode().equals(curStepObj.getRetType())) {
            return handleFlow_45(curStepObj, retordLogs);
        } else {//退货操作
            return handleFlow_70(curStepObj, retordLogs);
        }
    }


    /**
     * 仓库拒绝收客户退货
     * 处理节点41业务
     * @return
     */
    private boolean handleFlow_41(Retord curStepObj, RetordLogs retordLogs) {
        return defaultHandleFlow(curStepObj, retordLogs, OrdIRetStatus.RET_SELLER_NO_RECEIPTED);
        // return handle ? handleFlow_100(curStepObj, retordLogs) : false;
    }

    /**
     * 仓库重新发货 后 转到 买家等待收货业务
     * 处理节点50业务
     * @return
     */
    private boolean handleFlow_45(Retord curStepObj, RetordLogs retordLogs) {
        retordLogs.setOptContent("转交给仓库发货部门，等待仓库发货");
        retordLogs.setOptMan("系统");
        return defaultHandleFlow(curStepObj, retordLogs, OrdIRetStatus.RET_SELLER_WAITING_SEND);
    }


    /**
     * 仓库发货 业务
     * 处理节点48业务
     * @return
     */
    private boolean handleFlow_48(Retord curStepObj, RetordLogs retordLogs) {
        String content = retordLogs.getOptContent();
        OrdLogistics logistics = new OrdLogistics();
        logistics.setOrderId(curStepObj.getRetordOrderId());
        logistics.setUid(curStepObj.getUid());
        logistics.setBillType((int) Constant.ORDER_BILL_TYPE_FORWARD);
        if (content != null) {
            String[] info = content.split(",|，");
            if (info != null && info.length > 1) {
                TransportCompany temp = TransportCompany.SF;
                for (TransportCompany tc : TransportCompany.values()) {
                    if (tc.getCode().equals(info[1])) {
                        temp = tc;
                    }
                }
                logistics.setLogisticsCompany(temp.getCode());
                logistics.setLogisticsNumber(info[2].split(":")[1]);
            }
        }
        logistics.setCreateTime(DateUtil.getCurrentTimestamp());
        logistics.setUpdateTime(DateUtil.getCurrentTimestamp());
        List<Ordi> list = ordiDao.getOrdIListByOrdId(curStepObj.getOldOrderId(), true);
        try {
            if (list != null && list.size() > 0) {
                for (Ordi ordi : list) {
                    logistics.setOrdiId(ordi.getOrderItemId());
                    logisticsDao.insert(logistics);
                }
            }
        } catch (Exception e) {
            throw new ServiceException("记录发货单出现异常", e.getMessage());
        }
        //添加配送公司信息
        return defaultHandleFlow(curStepObj, retordLogs, OrdIRetStatus.RET_SELLER_AG_SEND);
        //  return handle ? handleFlow_50(curStepObj, retordLogs) : false;
    }

    /**
     * 买家收到货处理业务 换货业务
     * 处理节点60业务
     * @return
     */
    private boolean handleFlow_60(Retord curStepObj, RetordLogs retordLogs) {
        boolean handle = defaultHandleFlow(curStepObj, retordLogs, OrdIRetStatus.RET_BUYER_RECEIPTED);
        //分支 如果有补偿金额
        if (curStepObj.getRetTotalAmount() != null && curStepObj.getRetTotalAmount() > 0) {
            return handle ? handleFlow_70(curStepObj, retordLogs) : false;
        } else {
            curStepObj.setIsEndNode(1);
            return handle ? defaultHandleFlow(curStepObj, retordLogs, OrdIRetStatus.RET_BUYER_RECEIPTED_END) : false;
        }
    }


    /**
     * 等待退款状态业务
     * 处理节点70业务
     * @return
     */
    private boolean handleFlow_70(Retord curStepObj, RetordLogs retordLogs) {
        retordLogs.setOptContent("系统确认完毕等待财务处理退款，或换货补偿处理");
        retordLogs.setOptMan("系统");
        return defaultHandleFlow(curStepObj, retordLogs, OrdIRetStatus.RET_REFUND_WAIT);
    }

    /**
     * 退款状态业务
     * 处理节点75业务
     * @return
     */
    private boolean handleFlow_75(Retord curStepObj, RetordLogs retordLogs, String[] args) {
        //args[0]定义做管理员ID
        int adminId = Integer.parseInt(args[0].split("=")[1]);
        PayHead payHead = new PayHead();
        Date date = new Date();
        payHead.setCreateTime(date);
        //赋值给pay head的订单ID，以旧orderId-retOrderId形式
        payHead.setOrderId(curStepObj.getOldOrderId() + "-" + curStepObj.getRetordOrderId());
        // ------------- 定义退款的支付项 PayItem， 允许组合退款，它应该是个集合 -----------------//
        List<PayItem> payItems = new ArrayList<PayItem>();

        //支付宝或银行的退款
        PayItem aliOrBankItem = new PayItem();
        aliOrBankItem.setCreateTime(date);
        //判断是否退款回美丽湾钱包
        String payCode = PayCode.MLW_W.name();
        boolean isRetThird = false ;
        Map retPayMap = new HashMap();
        //退款到支付宝
        if (curStepObj.getRetPayType().equals(RetPayType.THIRD_ALIPAY.getCode())) {
            payCode = PayCode.RET_ALI_FUND.name();
            retPayMap.put("retAlipay", curStepObj.getServiceRetAlipay());
            isRetThird = true ;
        } else if (curStepObj.getRetPayType().equals(RetPayType.THIRD_BANK.getCode())) {
            payCode = PayCode.RET_BANK_FUND.name();
            retPayMap.put("retBank", curStepObj.getServiceRetBank());
            retPayMap.put("retOpenBank", curStepObj.getServiceRetOpenBank());
            retPayMap.put("retCardName", curStepObj.getServiceRetCardName());
            retPayMap.put("retCardNum", curStepObj.getServiceRetCardNum());
            isRetThird = true ;
        }
        aliOrBankItem.setPayCode(payCode);
        String jsonStr = new Gson().toJson(retPayMap);
        aliOrBankItem.setExtraInfo(jsonStr);
        //解决getRetTotalAmount空指针的bug，如果空指针则给他赋值0元退款， modify by yuxiong 2013.11.25
        BigDecimal retCardAmount = BigDecimal.valueOf(0d);
        //6.1.1 退换货改动需求 lzl 2014.07.03
        BigDecimal retWalletAmount = BigDecimal.valueOf(0d);
        if (curStepObj.getRetRealCardAmount() != null && curStepObj.getRetRealCardAmount() > 0) {
            retCardAmount = BigDecimal.valueOf(curStepObj.getRetRealCardAmount());
        }
        if (curStepObj.getRetRealMlwAmount() != null && curStepObj.getRetRealMlwAmount() > 0) {
        	retWalletAmount = BigDecimal.valueOf(curStepObj.getRetRealMlwAmount());
        }
        BigDecimal retTotalAmount = (curStepObj.getRetTotalAmount()==null?
                BigDecimal.valueOf(0d) : BigDecimal.valueOf(NumberUtil.subtract(curStepObj.getRetTotalAmount(), (retCardAmount.doubleValue()+retWalletAmount.doubleValue()), 2)));
        aliOrBankItem.setPayAmount(retTotalAmount);
        payItems.add(aliOrBankItem);

        //如果礼品卡退款金额大于0， 则往退款集合里加入礼品卡的退款
        if(curStepObj.getRetRealCardAmount()!=null && curStepObj.getRetRealCardAmount() > 0){
            PayItem mlwCardItem = new PayItem();
            mlwCardItem.setCreateTime(date);
            mlwCardItem.setPayCode(PayCode.MLW_C.name());
            mlwCardItem.setPayAmount(new BigDecimal(curStepObj.getRetRealCardAmount()));
            payItems.add(mlwCardItem);
           // retTotalAmount = retTotalAmount.add(BigDecimal.valueOf(curStepObj.getRetRealCardAmount()));
        }
        // ------------- 定义退款的支付项 PayItem， 允许组合退款，它应该是个集合 -----------------//
        
        //如果选择了退到第三方则需要判断退到钱包的情况 lzl  lzl 2014.07.03
        if(isRetThird && curStepObj.getRetRealMlwAmount() != null && curStepObj.getRetRealMlwAmount() > 0){
        	PayItem mlwWalletItem = new PayItem();
            mlwWalletItem.setCreateTime(date);
            mlwWalletItem.setPayCode(PayCode.MLW_W.name());
            mlwWalletItem.setPayAmount(new BigDecimal(curStepObj.getRetRealMlwAmount()));
            payItems.add(mlwWalletItem);
        }

        payHead.setPayItems(payItems);
        payHead.setPayType(PayType.BACK_FUND.name());
        //拼凑subject
        List<Ordi> ordiList = ordiDao.getOrdIListByOrdId(curStepObj.getRetordOrderId(), true);
        if (ordiList != null && !ordiList.isEmpty()) {
            String proName = ordiList.get(0).getProName();
            if (ordiList.size() > 1) {
                proName += " 等商品";
            }
            payHead.setSubject("[退款]" + proName);
        } else {
            payHead.setSubject("");
        }
        payHead.setTargetUid(curStepObj.getUid());
        payHead.setTotalAmount(retTotalAmount);
        //退款的动作把管理员ID设置给uid
        payHead.setUid(adminId);
        //管理员实际的退款动作
        //boolean paySuccess = PayClient.backMoneyForAdmin(payHead);
        boolean paySuccess = PayClient.refundMoneyForAdmin(payHead, IPUtil.getLocalIp(), adminId);
        if (!paySuccess) {
            logger.warn("退换货时，财务人员给用户退钱失败", "操作人:(" + payHead.getUid() + ")退给用户:(" + payHead.getTargetUid() + "), 需要退钱金额为:(" + payHead.getTotalAmount() + "), payHead" + payHead.toString(), null);
            return false;
        }
        if (paySuccess && curStepObj.getRetPayType().equals(RetPayType.MLW_WALLET.getCode())) {
            logger.info("退换货时，财务人员给用户退钱成功，退入美丽钱包", "操作人:(" + payHead.getUid() + "),退给用户:(" + payHead.getTargetUid() + "), 退钱金额为：（" + payHead.getTotalAmount() + "），payHead" + payHead.toString(), null);
        }
        boolean handle = defaultHandleFlow(curStepObj, retordLogs, OrdIRetStatus.RET_REFUND_FINISHED);
        return handle ? handleFlow_80(curStepObj, retordLogs) : false;
    }


    /**
     * 完成退款状态业务
     * 处理节点75业务
     * @return
     */
    private boolean handleFlow_80(Retord curStepObj, RetordLogs retordLogs) {
        retordLogs.setOptContent("退款业务完成");
        retordLogs.setOptMan("系统");

        curStepObj.setIsEndNode(1);
        return defaultHandleFlow(curStepObj, retordLogs, OrdIRetStatus.RET_FINISHED);
        //转向 100业务流程
        //return handle ? handleFlow_100(curStepObj, retordLogs) : false;
    }

    /**
     * 客服取消退货申请
     * 处理节点85业务
     * @return
     */
    private boolean handleFlow_85(Retord curStepObj, RetordLogs retordLogs) {
//        retordLogs.setOptContent("客服取消退换货申请");
        curStepObj.setIsEndNode(1);
        return defaultHandleFlow(curStepObj, retordLogs, OrdIRetStatus.RET_CANCEL);
        // return handle ? handleFlow_100(curStepObj, retordLogs) : false;
    }

    /**
     * 结束退换货
     * 处理节点100业务
     * @return
     */
//    private boolean handleFlow_100(Retord curStepObj, RetordLogs retordLogs) {
//        retordLogs.setOptContent("结束退换货处理");
//        retordLogs.setOptMan("系统");
//        return defaultHandleFlow(curStepObj, retordLogs, OrdIRetStatus.RET_DONE);
//    }


    /**
     * 流程基本正常操作 同步更新 反向订单状态
     * @param curStepObj
     * @param retordLogs
     * @param nextState
     * @return
     */
    private boolean defaultHandleFlow(Retord curStepObj, RetordLogs retordLogs, OrdIRetStatus nextState) {
        try {
            retordLogs.setId(null);
            retordLogs.setCreateTime(new Date());
            retordLogs.setOptResult(nextState.getDesc());
            retordLogs.setOldOrderId(curStepObj.getOldOrderId());
            retordLogs.setRetOrderId(curStepObj.getRetordOrderId());
            retordLogs.setOptStatusCode(nextState.getCode());
            Ord returnOrd = ordDao.getEntityById(curStepObj.getRetordOrderId(), true);
            if (returnOrd != null) {
                synchronizedState(curStepObj, returnOrd, nextState);
                curStepObj.setUpdateTime(new Date());
                this.retordDao.update(curStepObj);
                this.retordLogsDao.insert(retordLogs);
                return true;
            }
        } catch (Exception e) {
            throw new ServiceException("逆向订单基本流程控制出现异常，returnOrderService-->defaultHandleFlow()", e.getMessage());
        }
        return false;
    }


    /**
     * 流程控制器
     * @param stepState
     * @param nextState
     * @return
     */
    private boolean flowStateCrt(int stepState, int nextState) {
        return flowStateMap.asMap().get(stepState).contains(nextState);
    }

    public static void main(String[] args) {
        System.out.println(flowStateMap.asMap().get(20).contains(401));

        System.out.println(Integer.parseInt(OrdIRetStatus.RET_APPLY.getCode()));
    }
    
    /**
     * 获取导出退换货列表需要的数据 
     * lzl add 20140430
     * @param resultObj
     * @param starTime
     * @param endTime
     */
    public void getRetOrderExcel(JsonObject resultObj, String starTime, String endTime){
    	List<RetOrdReportRow> list = new ArrayList<RetOrdReportRow>() ;
    	list = retordDao.getRetOrderExcel(starTime, endTime) ;
    	addToResult(list, resultObj);
    }
}

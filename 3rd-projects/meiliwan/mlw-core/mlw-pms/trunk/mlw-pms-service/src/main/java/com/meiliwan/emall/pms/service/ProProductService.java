package com.meiliwan.emall.pms.service;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.RedisSearchInfoUtil;
import com.meiliwan.emall.pms.bean.*;
import com.meiliwan.emall.pms.cache.ProDetailPageCacheTool;
import com.meiliwan.emall.pms.cache.dto.PropSkuDto;
import com.meiliwan.emall.pms.cache.dto.PropValueSkuDto;
import com.meiliwan.emall.pms.cache.dto.SinglePropSkuDto;
import com.meiliwan.emall.pms.constant.Constant;
import com.meiliwan.emall.pms.constant.ProInfoType;
import com.meiliwan.emall.pms.dao.*;
import com.meiliwan.emall.pms.dto.*;
import com.meiliwan.emall.pms.service.helper.ProProductServiceHelper;
import com.meiliwan.emall.pms.service.helper.ProductValidate;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;
import com.meiliwan.emall.stock.bean.ProStock;
import com.meiliwan.emall.stock.client.ProStockClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;
import static com.meiliwan.emall.icetool.JSONTool.addToResultMap;

/**
 * User: wuzixin
 * Date: 13-6-3
 * Time: 下午3:25
 */
@Service
public class ProProductService extends DefaultBaseServiceImpl {

    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());

    @Autowired
    private ProProductDao proProductDao;
    @Autowired
    private ProSelfPropertyDao proSelfPropertyDao;
    @Autowired
    private ProDetailDao proDetailDao;
    @Autowired
    private ProActionDao proActionDao;
    @Autowired
    private ProStoreProdDao proStoreProdDao;
    @Autowired
    private ProStoreDao proStoreDao;
    @Autowired
    private ProSpuDao spuDao;
    @Autowired
    private SkuSelfPropertyDao skuSelfPropertyDao;
    @Autowired
    private PriceChangeLogDao changeLogDao;
    @Autowired
    private ProPropertyValueDao proPropertyValueDao;
    @Autowired
    private ProBrandDao proBrandDao;
    @Autowired
    private ProPlaceDao proPlaceDao;
    @Autowired
    private ProSupplierDao proSupplierDao;
    @Autowired
    private ProductCacheDao productCacheDao;
    @Autowired
    private ProProductPropertyDao proProductPropertyDao;
    @Autowired
    private ProImagesDao proImagesDao;


    /**
     * 增加商品SKU相关信息
     *
     * @param resultObj
     * @param proProduct
     */
    public void addProProduct(JsonObject resultObj, ProProduct proProduct) {
        int proId = 0;
        if (proProduct != null) {
            try {
                proProductDao.insertPro(proProduct);
                proId = proProduct.getProId();
            } catch (Exception e) {
                throw new ServiceException("service-pms-ProProductService.addProProduct:{}", proProduct == null ? "" : proProduct.toString(), e);
            }
        }
        addToResult(proId, resultObj);
    }

    /**
     * 修改商品SKU信息，同时增加商品的价格、固有属性、价格记录
     *
     * @param resultObj
     * @param proProduct 商品SKU对象数据
     */
    public void updateProProduct(JsonObject resultObj, ProProduct proProduct) {
        boolean suc = false;
        if (proProduct != null) {
            try {
                int count = proProductDao.updatePro(proProduct);
                if (count > 0) {
                    //增加SKU固有属性
                    if (proProduct.getSkuSelfProps() != null && proProduct.getSkuSelfProps().size() > 0) {
                        skuSelfPropertyDao.insertByBatch(proProduct.getProId(), proProduct.getSkuSelfProps());
                    }
                    //增加商品的价格的变更记录
                    PriceChangeLog log = new PriceChangeLog();
                    log.setProId(proProduct.getProId());
                    log.setAdminId(proProduct.getAdminId());
                    log.setLastPrice(proProduct.getMlwPrice());
                    log.setNowPrice(proProduct.getMlwPrice());
                    changeLogDao.insert(log);
                }
                productCacheDao.setSkuCacheByProId(proProduct.getProId());
                suc = true;
            } catch (Exception e) {
                throw new ServiceException("service-pms-ProProductService.updateProProduct:{}", proProduct == null ? "" : proProduct.toString(), e);
            }
        }
        addToResult(suc, resultObj);
    }

    /**
     * 修改商品自定义属性固有属性
     *
     * @param resultObj
     * @param list      sku固有属性列表
     * @param proId
     */
    public void updateSkuSelfProperty(JsonObject resultObj, List<SkuSelfProperty> list, int proId) {
        boolean suc = false;
        if (list != null && list.size() > 0) {
            try {
                List<SkuSelfProperty> addList = new ArrayList<SkuSelfProperty>();
                for (SkuSelfProperty ssp : list) {
                    if (ssp.getId() == null && StringUtils.isNotBlank(ssp.getSelfPropName())
                            && StringUtils.isNotBlank(ssp.getSelfPropValue())) {
                        addList.add(ssp);
                    }
                    //更新
                    if (ssp.getId() != null && ssp.getId() > 0 && StringUtils.isNotBlank(ssp.getSelfPropName())
                            && StringUtils.isNotBlank(ssp.getSelfPropValue())) {
                        skuSelfPropertyDao.update(ssp);
                    }
                    //删除
                    if (ssp.getId() != null && ssp.getId() > 0 && (StringUtils.isBlank(ssp.getSelfPropName())
                            || StringUtils.isBlank(ssp.getSelfPropValue()))) {
                        skuSelfPropertyDao.delete(ssp.getId());
                    }
                }
                //新增
                skuSelfPropertyDao.insertByBatch(proId, addList);
                //修改缓存
                productCacheDao.setSkuSelfPropsCacheByProId(proId);
                suc = true;
            } catch (Exception e) {
                throw new ServiceException("service-pms-ProProductService.updateSkuSelfProperty:{}", list == null ? "" : list.toString(), e);
            }
            addToResult(suc, resultObj);
        }
    }


    /**
     * 修改SKU信息，同时增加SKU用户行为、以及价格记录等
     * 业务场景：管理后台sku编辑功能
     *
     * @param resultObj
     * @param proProduct
     */
    public void updateProAndAddAction(JsonObject resultObj, ProProduct proProduct) {
        boolean suc = false;
        try {
            int count = proProductDao.updatePro(proProduct);
            if (count > 0) {
                //增加用户行为
                proActionDao.insert(proProduct.getAction());
                //增加SKU固有属性
                skuSelfPropertyDao.insertByBatch(proProduct.getProId(), proProduct.getSkuSelfProps());
                //增加商品相关价格记录
                PriceChangeLog log = new PriceChangeLog();
                log.setProId(proProduct.getProId());
                log.setLastPrice(proProduct.getMlwPrice());
                log.setNowPrice(proProduct.getMlwPrice());
                log.setAdminId(proProduct.getAdminId());
                changeLogDao.insert(log);
            }
            productCacheDao.setSkuCacheByProId(proProduct.getProId());
            suc = true;
        } catch (Exception e) {
            throw new ServiceException("service-pms-ProProductService.updateProAndAddAction:{}", proProduct == null ? "" : proProduct.toString(), e);
        }
        addToResult(suc, resultObj);
    }

    /**
     * 修改商品的自身的相关属性，比如状态、库存、价格等
     */
    public void updateByProduct(JsonObject resultObj, ProProduct product) {
        try {
            int count = proProductDao.updatePro(product);
            if (count > 0) {
                RedisSearchInfoUtil.addSearchInfo(product.getProId());
                proProductDao.getEntityById(product.getProId(), true);
                productCacheDao.setSkuCacheByProId(product.getProId());
            }
            addToResult(true, resultObj);
        } catch (Exception e) {
            addToResult(false, resultObj);
        }
    }

    /**
     * 修改商品SKU对应的价格
     *
     * @param resultObj
     * @param product   sku实体
     */
    public synchronized void updateByPrice(JsonObject resultObj, ProProduct product) {
        int count = 0;
        if (product != null) {
            ProProduct oldProduct = proProductDao.getWholeProductById(product.getProId());

            ProProduct updateProduct = new ProProduct();
            updateProduct.setProId(product.getProId());
            updateProduct.setMlwPrice(product.getMlwPrice());  //美丽价
            updateProduct.setMarketPrice(product.getMarketPrice());  //市场价
            updateProduct.setTradePrice(product.getTradePrice());    //进货价
            updateProduct.setUpdateTime(new Date());   //更新时间
            count = proProductDao.updatePro(updateProduct);
            if (count > 0) {
                if (product.getMlwPrice() != null && oldProduct != null) {

                    PriceChangeLog log = new PriceChangeLog();
                    log.setProId(product.getProId());
                    log.setNowPrice(product.getMlwPrice());
                    log.setLastPrice(oldProduct.getMlwPrice());  //修改的原始价格
                    log.setAdminId(product.getAdminId());
                    changeLogDao.insert(log);
                }
                RedisSearchInfoUtil.addSearchInfo(product.getProId());//通知搜索更新索引
                productCacheDao.setSkuCacheByProId(product.getProId());
            }
        }
        addToResult(count > 0 ? true : false, resultObj);
    }

    /**
     * 批量删除商品
     *
     * @param resultObj
     * @param ids
     */
    public void delProByIds(JsonObject resultObj, int[] ids) {
        int count = proProductDao.delSkuByIds(ids);
        addToResult(true, resultObj);
    }

    /**
     * 批量下架商品
     *
     * @param resultObj
     * @param ids
     */
    public void updateStateToOffByIds(JsonObject resultObj, int[] ids) {
        int count = proProductDao.updateStateToOffByIds(ids);
        if (count > 0) {
            RedisSearchInfoUtil.addSearchInfo(ids);
        }
        addToResult(true, resultObj);
    }

    /**
     * 批量上架商品
     *
     * @param resultObj
     * @param ids
     */
    public void updateStateToONByIds(JsonObject resultObj, int[] ids) {
        List<SimpleProduct> list = proProductDao.getSimpleListByIds(ids);
        Map<Integer, String> map = new HashMap<Integer, String>();
        if (list != null && list.size() > 0) {
            int size = list.size();
            //表示第一次上架的ids；
            List<Integer> first = new ArrayList<Integer>();
            //表示第多次上架的ids
            List<Integer> second = new ArrayList<Integer>();
            for (SimpleProduct product : list) {
                boolean flag = true;
                if (ProductValidate.getPriceValid(product)) {
                    map.put(product.getProId(), "价格不完整,");
                    flag = false;
                }
                if (ProductValidate.getImgValid(product)) {
                    map.put(product.getProId(), (map.get(product.getProId()) == null ? "" : map.get(product.getProId())) + "图片未上传,");
                    flag = false;
                }
                if (ProductValidate.getStockVaild(product.getProId())) {
                    map.put(product.getProId(), (map.get(product.getProId()) == null ? "" : map.get(product.getProId())) + "库存不足,");
                    flag = false;
                }
                if (StringUtils.isNotEmpty(product.getSkuPropertyStr())) {
                    if (validateSkuProp(product.getSpuId(), product.getSkuPropertyStr())) {
                        map.put(product.getProId(), (map.get(product.getProId()) == null ? "" : map.get(product.getProId())) + "对应spu不存在该规格");
                        flag = false;
                    }
                }
                if (flag) {
                    if (product.getOnTime() != null) {
                        second.add(product.getProId());
                    } else {
                        first.add(product.getProId());
                    }
                }
            }
            //第一次上架操作
            try {
                if (first != null && first.size() > 0) {
                    proProductDao.updateStateToONByIds(1, first);
                    RedisSearchInfoUtil.addSearchInfo(first);
                }
            } catch (Exception e) {
                throw new ServiceException("service-pms-ProProductService.deleteSpu:{}", "flag:1,ids:" + first.toString(), e);
            }
            //多次上架操作
            try {
                if (second != null && second.size() > 0) {
                    proProductDao.updateStateToONByIds(0, second);
                    RedisSearchInfoUtil.addSearchInfo(second);
                }
            } catch (Exception e) {
                throw new ServiceException("service-pms-ProProductService.deleteSpu:{}", "flag:0,ids:" + first.toString(), e);
            }
        }
        addToResult(new Gson().toJson(map), resultObj);
    }

    /**
     * 修改预售商品相关的时间
     *
     * @param resultObj
     * @param proId
     * @param endTime
     * @param sendTime
     */
    public void updatePresaleTime(JsonObject resultObj, int proId, String endTime, String sendTime) {
        int count = proProductDao.updatePresaleTime(proId, endTime, sendTime);
        addToResult(count, resultObj);
    }


    /**
     * 获取sku信息，部分字段
     * 通过商品SKU ID获取相关的商品,不包括可使用库存
     *
     * @param proId 商品SKU ID
     */
    public void getProductById(JsonObject resultObj, int proId) {
        SimpleProduct product = null;
        if (proId > 0) {
            product = proProductDao.getEntityById(proId);
        }
        addToResult(product, resultObj);
    }

    /**
     * 根据spuId 获取对应的SKU列表
     *
     * @param resultObj
     * @param spuId
     */
    public void getListProBySpuId(JsonObject resultObj, int spuId) {
        SimpleProduct product = new SimpleProduct();
        product.setSpuId(spuId);
        List<SimpleProduct> list = proProductDao.getListByObj(product);
        addToResult(list, resultObj);
    }

    /**
     * 通过SKU ID获取SKU相关信息，里面包含所要的可使用库存信息
     *
     * @param resultObj
     * @param proId
     */
    public void getProductWithStockById(JsonObject resultObj, int proId) {
        SimpleProIfStock proIfStock = new SimpleProIfStock();
        if (proId > 0) {
            SimpleProduct product = null;
            product = proProductDao.getEntityById(proId);
            if (product != null) {
                int sellStock = ProStockClient.getSellStock(proId);
                proIfStock.setProduct(product);
                proIfStock.setStock(sellStock);
            }
        }
        addToResult(proIfStock, resultObj);
    }

    /**
     * 根据条件分页
     *
     * @param resultObj
     * @param pageInfo
     * @return
     */
    public void pageByObj(JsonObject resultObj,
                          ProductDTO dto, PageInfo pageInfo, String order_name, String order_form) {

        if (dto.getProName() != null && !Strings.isNullOrEmpty(dto.getProName().trim())) {
            dto.setProName("%" + dto.getProName().trim() + "%");
        }
        StringBuilder orderBySql = new StringBuilder();
        if (!"".equals(order_name) && null != order_name
                && !"".equals(order_form) && null != order_form) {
            orderBySql.append(" order by ").append(order_name)
                    .append(" ").append(order_form);
        }

        PagerControl<ProductDTO> pages = proProductDao.getPagerByDTO(dto, pageInfo, null, orderBySql.toString());
        addToResult(pages, resultObj);
    }

    /**
     * 获取商品SKU整个实体对象
     *
     * @param proId
     * @return
     */
    public void getWholeProductById(JsonObject resultObj, Integer proId) {
        ProProduct product = null;
        if (proId > 0) {
            product = productCacheDao.getSkuCacheByProId(proId);
        }
        addToResult(product, resultObj);
    }

    /**
     * 获取商品SKU整个实体对象
     *
     * @param proId
     * @return
     */
    public void getProductForAdminShowById(JsonObject resultObj, Integer proId) {
        ProProduct product = null;
        if (proId > 0) {
            product = productCacheDao.getSkuCacheByProId(proId);
            //sku自定义属性
            SkuSelfProperty selfPropertyQuery = new SkuSelfProperty();
            selfPropertyQuery.setProId(proId);
            List<SkuSelfProperty> selfProps = skuSelfPropertyDao.getListByObj(selfPropertyQuery);
            product.setSkuSelfProps(selfProps);
        }

        addToResult(product, resultObj);
    }

    /**
     * 通过商品SKU IDS 获取商品头列表，不包括商品可使用库存
     *
     * @param resultObj
     * @param ids       商品SKU ID数组
     */
    public void getSimpleListByProIds(JsonObject resultObj, int[] ids) {
        List<SimpleProduct> list = null;
        if (ids.length > 0) {
            list = proProductDao.getSimpleListByIds(ids);
        }
        addToResult(list == null ? new ArrayList<SimpleProduct>() : list, resultObj);
    }

    /**
     * 通过SKU Ids 获取SKU头列表，包括SKU可使用库存
     *
     * @param resultObj
     * @param ids       商品SKU ID数组
     */
    public void getSimpleListWithStockByIds(JsonObject resultObj, int[] ids) {
        List<SimpleProIfStock> stocks = new ArrayList<SimpleProIfStock>();
        if (ids != null && ids.length > 0) {
            List<SimpleProduct> list = proProductDao.getSimpleListByIds(ids);
            if (list != null && list.size() > 0) {
                Map<Integer, ProStock> map = ProStockClient.getMapByIds(ids);
                for (SimpleProduct product : list) {
                    SimpleProIfStock stock = new SimpleProIfStock();
                    stock.setProduct(product);
                    stock.setStock(map.get(product.getProId()) == null ? 0 : map.get(product.getProId()).getSellStock());
                    stocks.add(stock);
                }
            }
        }
        addToResult(stocks, resultObj);
    }

    /**
     * 根据商品IDS和名称查询商品列表
     *
     * @param resultObj
     * @param ids
     * @param proName
     */
    public void getListByIdsAndName(JsonObject resultObj, int[] ids, String proName) {
        List<SimpleProIfStock> proIfStocks = new ArrayList<SimpleProIfStock>();
        if (ids != null && ids.length > 0) {
            List<SimpleProduct> list = null;
            if (!Strings.isNullOrEmpty(proName)) {
                String name = "'%" + proName + "%'";
                list = proProductDao.getListByIdsAndName(ids, name);
            } else {
                list = proProductDao.getSimpleListByIds(ids);
            }
            if (list != null && list.size() > 0) {
                //获取库存
                Map<Integer, ProStock> map = ProStockClient.getMapByIds(ids);
                for (SimpleProduct product : list) {
                    SimpleProIfStock stock = new SimpleProIfStock();
                    stock.setProduct(product);
                    stock.setStock(map.get(product.getProId()) == null ? 0 : map.get(product.getProId()).getStock());
                    proIfStocks.add(stock);
                }
            }
        }
        addToResult(proIfStocks, resultObj);
    }


    /**
     * 通过商品ID获取商品详细信息，包括商品描述等
     *
     * @param resultObj
     * @param proId
     */
    public void getProDetailByProId(JsonObject resultObj, int proId) {
        addToResult(proDetailDao.getEntityById(proId), resultObj);
    }

    /**
     * 根据更新时间获取商品列表
     *
     * @param resultObj
     * @param min
     * @param max
     */
    public void getListByUpdateTime(JsonObject resultObj, String min, String max) {
        List<ProProduct> list = proProductDao.getListByUpdateTime(min, max);
        addToResult(list, resultObj);
    }

    /**
     * 修改商品详细信息，detail表
     *
     * @param resultObj
     * @param detail
     */
    public void updateProDetail(JsonObject resultObj, ProDetail detail) {
        boolean suc = false;
        if (detail != null) {
            int count = proDetailDao.update(detail);
            if (count > 0) {
                suc = true;
            }
        }
        addToResult(suc, resultObj);
    }

    /**
     * 验证商品SKU是否可以加入购物车
     *
     * @param resultObj
     * @param proId
     */
    public void checkJoinCart(JsonObject resultObj, int proId) {
        String suc = "failure";
        SimpleProduct product = proProductDao.getEntityById(proId);
        if (product != null) {
            if (product.getState() == Constant.PRODUCT_OFF) {
                suc = Constant.PRODUCT_GET_OFF;
            } else {
                int stock = ProStockClient.getSellStock(proId);
                if (stock <= 0) {
                    suc = Constant.PRODUCT_GET_OFF;
                } else {
                    suc = "success";
                }
            }
        }
        addToResult(suc, resultObj);
    }

    /**
     * 对购物车里的每一个库存项商品状态进行校验
     * 业务场景：
     * 用户查看购物车(或者返回购物车时；或去查看购物车时)，进行商品有效性验证
     *
     * @param ids : 购物车商品列表;
     * @return items
     */
    public void checkProStatusIfSub(JsonObject resultObj, int[] ids) {
        List<ProductItemStatus> items = new ArrayList<ProductItemStatus>();
        if (ids.length <= 5) {
            for (Integer id : ids) {
                ProductItemStatus item = new ProductItemStatus(id);
                SimpleProduct product = proProductDao.getEntityById(id);
                if (product != null) {
                    if (product.getState().equals(Constant.PRODUCT_ON)) {
                        item.setStatus(true);
                    } else {
                        item.setStatus(false);
                    }
                } else {
                    item.setStatus(false);
                }
                items.add(item);
            }
        } else {
            List<SimpleProduct> products = proProductDao.getSimpleListByIds(ids);
            for (SimpleProduct product : products) {
                ProductItemStatus item = new ProductItemStatus(product.getProId());
                if (product.getProId().equals(Constant.PRODUCT_ON)) {
                    item.setStatus(true);
                } else {
                    item.setStatus(false);
                }
                items.add(item);
            }
        }
        addToResult(items, resultObj);
    }

    /**
     * 对提交订单的订单行商品进行有效性，并返回每一个订单行商品的状态,及整个订单状态;
     * 业务场景：用户提交订单选择在线支付时，对每一个商品进行有效性校验
     *
     * @param ids 订单商品列表
     * @return orderItemStatus  订单商品行校验的结果
     */
    public void checkProStatusIfOrderSub(JsonObject resultObj, int[] ids) {
        ProductOrderItemStatus orderItemStatus = new ProductOrderItemStatus();
        List<ProductItemStatus> items = new ArrayList<ProductItemStatus>();
        if (ids != null && ids.length > 0) {
            int flag = 0;
            if (ids.length <= 5) {
                for (Integer id : ids) {
                    ProductItemStatus item = new ProductItemStatus(id);
                    SimpleProduct product = proProductDao.getEntityById(id);
                    if (product != null) {
                        if ((int) product.getState() == Constant.PRODUCT_ON) {
                            item.setStatus(true);
                        } else {
                            item.setStatus(false);
                            flag++;
                        }
                    } else {
                        item.setStatus(false);
                        flag++;
                    }
                    items.add(item);
                }
            } else {
                List<SimpleProduct> products = proProductDao.getSimpleListByIds(ids);
                for (SimpleProduct product : products) {
                    ProductItemStatus item = new ProductItemStatus(product.getProId());
                    if ((int) product.getState() == Constant.PRODUCT_ON) {
                        item.setStatus(true);
                    } else {
                        item.setStatus(false);
                        flag++;
                    }
                    items.add(item);
                }
            }
            orderItemStatus.setItemStatus(items);
            if (flag > 0) {
                orderItemStatus.setStatus(false);
            } else {
                orderItemStatus.setStatus(true);
            }
        }
        addToResult(orderItemStatus, resultObj);
    }

    /**
     * 检查商品的有效性
     *
     * @param resultObj
     * @param proId
     */
    public void checkProIsEffect(JsonObject resultObj, int proId) {
        boolean suc = false;
        SimpleProduct product = proProductDao.getEntityById(proId);
        if (product != null && product.getState() == Constant.PRODUCT_ON) {
            suc = true;
        }
        addToResult(suc, resultObj);
    }

    /**
     * 检查商品SKU条形码是否存在，保证商品条形码的唯一性
     *
     * @param resultObj
     * @param barCode
     */
    public void checkProductByBarCode(JsonObject resultObj, String barCode, int proId, String type) {
        boolean suc = false;
        if (StringUtils.isEmpty(type)) {
            suc = true;
        } else {
            SimpleProduct product = new SimpleProduct();
            product.setBarCode(barCode);
            List<SimpleProduct> list = proProductDao.getListByObj(product, null);
            if (list != null && list.size() > 0) {
                if (type.equals("add")) {
                    suc = true;
                } else if (type.equals("update")) {
                    if (list.size() == 1) {
                        if (list.get(0).getProId() == proId) {
                            suc = false;
                        } else {
                            suc = true;
                        }
                    } else {
                        suc = true;
                    }
                }
            }
        }
        addToResult(suc, resultObj);
    }

    //获取有商品ID组成的字符串
    private String getIdsByString(int[] ids) {
        if (ids != null && ids.length > 0) {
            String whereSql = "";
            boolean firstFlag = true;
            for (int id : ids) {
                if (firstFlag) {
                    whereSql = whereSql + id;
                    firstFlag = false;
                } else {
                    whereSql = whereSql + "," + id;
                }
            }
            return whereSql;
        } else {
            return "0";
        }
    }

    /**
     * 用于商品详情页，
     * 获取商品详情所有信息业务缓存得不到时调此接口
     * create by yiyou.luo
     *
     * @param resultObj
     * @param proId
     */
    public void getAllProDetail(JsonObject resultObj, int proId) {
        Map<String, Object> proInfo = buildAllProDetail(proId);
        addToResultMap(proInfo, resultObj);
    }

    /**
     * add by yyluo
     * 获取spu规格缓存
     * 获取spu规格缓存缓存得不到时调此接口
     *
     * @param spuId
     * @return
     */
    public void getSpuStandard(JsonObject resultObj, Integer spuId) {
        Map<String, Object> proInfo = buildSpuStandardCacheInfo(spuId);
        addToResultMap(proInfo, resultObj);
    }

    /**
     * 通过商品ID获取对应前台搜索列表对应的商品信息
     * 业务：做异步请求获取商前台列表单个商品信息
     *
     * @param resultObj
     * @param proId
     */
    public void getFrontProById(JsonObject resultObj, int proId) {
        FrontListVo vo = new FrontListVo();
        if (proId > 0) {
            SimpleProduct product = proProductDao.getEntityById(proId);
            if (product != null) {
                vo.setProId(proId);
                vo.setProName(product.getProName());
                vo.setShortName(product.getShortName());
                vo.setSkuName(product.getSkuName());
                vo.setMlwPrice(product.getMlwPrice());
                ProAction action = proActionDao.getEntityById(proId);
                if (action != null) {
                    vo.setComNum(action.getTotalCmtNum());
                    vo.setScore(action.getAvgScore());
                } else {
                    vo.setComNum(0);
                    vo.setScore(0.00);
                }
            }
        }
        addToResult(vo, resultObj);
    }

    /**
     * 通过spuID获取商品列表，主要提供oms使用
     *
     * @param resultObj
     * @param spuId
     */
    public void getProductToOmsBySpuId(JsonObject resultObj, int spuId) {
        List<ProProduct> list = proProductDao.getProductToOmsBySpuId(spuId);
        addToResult(list, resultObj);
    }

    /**
     * 添加or更新 商品详情所有信息到Cache
     * create by yiyou.luo
     *
     * @param resultObj
     * @param proId
     */
    public void addAllProDetailToCache(JsonObject resultObj, int proId) {
        //添加商品信息岛缓存中
        boolean isSuccess = false;
        Map<String, Object> proInfo = new HashMap<String, Object>();
        try {
            proInfo = buildAllProDetail(proId);
            if (proInfo.size() > 0) {
                isSuccess = ProDetailPageCacheTool.addAllProDetail(proId, proInfo);
            }
        } catch (Exception e) {
            logger.warn("添加商品详情页信息到缓存失败", "proId:" + proId + "proInfo" + proInfo + ",service:ProProductService_addAllProDetailToCache", null);
        }
        addToResult(isSuccess, resultObj);
    }

    /**
     * 构建商品详情所有信息
     *
     * @param proId
     * @return
     */
    private Map<String, Object> buildAllProDetail(Integer proId) {
        Map<String, Object> proInfo = new HashMap<String, Object>();
        try {
            ProProduct skuDetail = productCacheDao.getSkuCacheByProId(proId);
            if (skuDetail != null && skuDetail.getId() > 0) {

                //SKU信息
                proInfo.put(ProInfoType.SKU_INF.toString(), new Gson().toJson(skuDetail));

                //SPU信息
                ProSpu spu = new ProSpu();
                if (skuDetail.getSpuId() > 0) {
                    int spuId = skuDetail.getSpuId();
                    spu = spuDao.getEntityById(spuId);
                    proInfo.put(ProInfoType.SPU_INF.toString(), new Gson().toJson(spu));

                    //商品详情
                    ProDetail proDetail = proDetailDao.getEntityById(spuId);
                    proInfo.put(ProInfoType.PRO_DETAIL.toString(), new Gson().toJson(proDetail));

                    //SPU固有属性详情信息
                    ProSelfProperty property = new ProSelfProperty();
                    property.setSpuId(spuId);
                    List<ProSelfProperty> properties = proSelfPropertyDao.getListByObj(property);
                    proInfo.put(ProInfoType.SPU_SELF_PROLIST.toString(), new Gson().toJson(properties));

                    //品牌信息
                    if (spu.getBrandId() > 0) {
                        ProBrand brand = proBrandDao.getEntityById(spu.getBrandId());
                        if (brand != null) {
                            proInfo.put(ProInfoType.BRAND.toString(), new Gson().toJson(brand));
                        }
                    }

                    //产地信息
                    if (spu.getPlaceId() > 0) {
                        ProPlace place = proPlaceDao.getEntityById(spu.getPlaceId());
                        if (place != null) {
                            proInfo.put(ProInfoType.PLACE.toString(), new Gson().toJson(place));
                        }
                    }

                    //店铺信息
                    ProStoreProd pspQuery = new ProStoreProd();
                    pspQuery.setSpuId(spuId);
                    List<ProStoreProd> pspList = proStoreProdDao.getListByObj(pspQuery);
                    if (pspList != null && pspList.size() > 0) {
                        ProStoreProd psp = pspList.get(0);
                        if (psp != null && psp.getStoreId() > 0) {
                            ProStore store = proStoreDao.getEntityById(psp.getStoreId());
                            if (store != null) {
                                proInfo.put(ProInfoType.STORE.toString(), new Gson().toJson(store));
                            }
                        }
                    }

                    //供应商信息
                    if (spu.getSupplierId() > 0) {
                        ProSupplier supplier = proSupplierDao.getEntityById(spu.getSupplierId());
                        if (supplier != null) {
                            proInfo.put(ProInfoType.SUPPERLIER.toString(), new Gson().toJson(supplier));
                        }
                    }

                } else {
                    logger.warn("sku实例中找不到spuId", "proId:" + proId + ",service:ProProductService_buildAllProDetail", null);
                }

                // SKU固有属性信息
                List<SkuSelfProperty> skuSelfProperties = productCacheDao.getSkuSelfPropsCacheByProId(proId);
                if (skuSelfProperties != null && skuSelfProperties.size() > 0) {
                    proInfo.put(ProInfoType.SKU_SELF_PROLIST.toString(), new Gson().toJson(skuSelfProperties));
                } else {
                    logger.warn("找不到SKU固有属性信息", "proId:" + proId + ",service:ProProductService_buildAllProDetail", null);
                }
            }
        } catch (Exception e) {
            logger.warn("获取商品详情所有信息业务信息异常", "proId:" + proId + ",service:ProProductService_buildAllProDetail", null);
        }
        return proInfo;
    }


    /**
     * 添加 or 更新spu规格数据到Cache
     * 只处理单规格和双规格，无规格不需处理
     * create by yiyou.luo
     *
     * @param resultObj
     * @param spuId
     */
    public void addSpuStandardInfoToCache(JsonObject resultObj, int spuId) {
        Map cacheInfo = buildSpuStandardCacheInfo(spuId);
        boolean success = false;
        if (cacheInfo.size() > 0) {
            success = ProDetailPageCacheTool.addOrUpdateSpuStandard(spuId, cacheInfo);
            if (success = false) {
                logger.warn("添加 or 更新spu规格数据到Cache 失败  ProProductService.addSpuStandardInfoToCache()", spuId, null);
            }
        }
        addToResult(success, resultObj);
    }

    /**
     * 构建spu规格缓存数据
     * 只处理单规格和双规格，无规格不需处理
     * create by yiyou.luo
     *
     * @param spuId
     * @return
     */
    private Map<String, Object> buildSpuStandardCacheInfo(Integer spuId) {
        /*
        步骤：
            1、根据spuid获取该sku的规格属性集合 &　spu对应的sku列表，
            2、确定是单规格还是双规、
            3、构建单规格数据
            4、构建双规格数据
         */

        Map<String, Object> skuMap = new HashMap();
        try {
            //查询SPU规格属性
            ProProductProperty entity = new ProProductProperty();
            entity.setSpuId(spuId);
            entity.setIsSku(Constant.IS_SKU_YES);
            // List<ProProductProperty> proProps = proProductPropertyDao.getListByObj(entity);
            List<ProProperty> proProps = productCacheDao.getSpuWithSkuPropsCacheById(spuId);

            SimpleProduct queryPro = new SimpleProduct();
            queryPro.setSpuId(spuId);
            //queryPro.setState((short)1); //上架商品
            List<SimpleProduct> products = proProductDao.getListByObj(queryPro);
            Map<String, SimpleProduct> productMap = ProProductServiceHelper.getProductMapBySkuId(spuId, products);

            // SPU规格属性异图
            ProImages imageQuit = new ProImages();
            imageQuit.setSpuId(spuId);
            List<ProImages> imagesList = proImagesDao.getListByObj(imageQuit);


            if (proProps != null && proProps.size() > 0 && products != null && products.size() > 0) {
            /*
                 一、单规格情况
             */
                if (proProps.size() == 1) {
                    ProProperty ppProp = proProps.get(0);  //当前规格属性
                    List<ProPropertyValue> ppvList = ppProp.getProProValue();

                    if (ppvList != null && ppvList.size() > 0 && productMap.size() > 0) {
                        for (SimpleProduct sPro : products) {
                            SinglePropSkuDto singlePropSkuDto = new SinglePropSkuDto();
                            singlePropSkuDto.setProPropId(ppProp.getProPropId());
                            singlePropSkuDto.setPropName(ppProp.getName());

                            List<PropValueSkuDto> pvSkuList = new ArrayList<PropValueSkuDto>();
                            for (ProPropertyValue ppv : ppvList) {
                                PropValueSkuDto pvskd = new PropValueSkuDto();
                                pvskd.setPropValueId(ppv.getId());
                                pvskd.setPropValue(ppv.getName());
                                SimpleProduct sku = productMap.get(ppProp.getProPropId() + ":" + ppv.getId() + ";");

                                if (sku != null) {
                                    /*//是否异图
                                    if (ppProp.getIsImage() == 1) {
                                        pvskd.setImageUrl(sku.getDefaultImageUri());
                                    }*/
                                    if (sku.getState() != null && sku.getState().intValue() == Constant.PRODUCT_ON) {
                                        pvskd.setSkuId(sku.getId()); //设置skuid
                                    }
                                    if (sPro.getProId() == sku.getProId()) {
                                        pvskd.setSelected("1");   //当前选中
                                    } else {
                                        pvskd.setSelected("0");  //当前非选中
                                    }
                                }
                                pvSkuList.add(pvskd);
                            }
                            //异图时请把未构建的规格填充剩余的图
                            if (ppProp.getIsImage() == 1) {
                                ProProductServiceHelper.buildDefaultImage(pvSkuList, imagesList);
                            }


                            singlePropSkuDto.setPvSkuList(pvSkuList);

                            String skuPropertyStr = sPro.getSkuPropertyStr();
                            Integer pvId = ProProductServiceHelper.getPropValueId(sPro.getSkuPropertyStr());
                            if (pvId > 0) {
                                ProPropertyValue pv = proPropertyValueDao.getEntityById(pvId);
                                if (pv != null && StringUtils.isNotBlank(pv.getName())) {
                                    singlePropSkuDto.setPropValueName(pv.getName()); //设置颜色属性值名称
                                }
                            }
                            skuMap.put(sPro.getId().toString(), new Gson().toJson(singlePropSkuDto));
                        }
                    }
                }

            /*
                 二、双规格情况
             */
                if (proProps.size() == 2) {
                    //规格一
                    ProProperty proProp1 = proProps.get(0);

                    //规格二
                    ProProperty proProp2 = proProps.get(1);

                    for (SimpleProduct sPro : products) {
                        List<PropSkuDto> propSkuList = new ArrayList<PropSkuDto>();
                        String skuPropertyStr = sPro.getSkuPropertyStr();
                        String[] propStrs = skuPropertyStr.split(";");
                        if (propStrs.length == 2) {
                            String propStr1 = propStrs[0] + ";";
                            String propStr2 = propStrs[1] + ";";
                            List<PropValueSkuDto> pvSkuList1 = ProProductServiceHelper.getPvSkuList(proProp1, productMap, sPro.getProId(), propStr2);
                            List<PropValueSkuDto> pvSkuList2 = ProProductServiceHelper.getPvSkuList(proProp2, productMap, sPro.getProId(), propStr1);

                            //遍历spu规格属性 eg：尺寸、颜色
                            PropSkuDto propSkuDto1 = new PropSkuDto();
                            propSkuDto1.setProPropId(proProp1.getProPropId());
                            propSkuDto1.setPropName(proProp1.getName());
                            propSkuDto1.setMapProPropId(proProp2.getProPropId()); //相对的规格id
                            propSkuDto1.setMapPropName(proProp2.getName());   //相对的规格名：eg：尺寸
                            propSkuDto1.setPvSkuList(pvSkuList1);
                            //异图时请把未构建的规格填充剩余的图
                            if (proProp1.getIsImage() == 1) {
                                ProProductServiceHelper.buildDefaultImage(pvSkuList1, imagesList);
                            }
                            propSkuList.add(propSkuDto1);

                            //获取规格属性包含的属性集合：eg；尺码的全部码数 35、36、37、、43
                            PropSkuDto propSkuDto2 = new PropSkuDto();
                            propSkuDto2.setProPropId(proProp2.getProPropId());
                            propSkuDto2.setPropName(proProp2.getName());
                            propSkuDto2.setMapProPropId(proProp1.getProPropId());   //相对的规格id
                            propSkuDto2.setMapPropName(proProp1.getName());  //相对的规格名：eg：颜色
                            propSkuDto2.setPvSkuList(pvSkuList2);
                            //异图时请把未构建的规格填充剩余的图
                            if (proProp2.getIsImage() == 1) {
                                ProProductServiceHelper.buildDefaultImage(pvSkuList2, imagesList);
                            }
                            propSkuList.add(propSkuDto2);

                            Gson gson = new Gson();
                            skuMap.put(sPro.getProId().toString(), gson.toJson(propSkuList));
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e, "构建spu规格数据失败 --spuId=" + spuId + " --ProProductService.buildSpuStandardCacheInfo", null);
        }

        return skuMap;
    }

    //验证spu是否包含对应改规格的商品
    private boolean validateSkuProp(int spuId, String skuPropStr) {
        boolean suc = true;
        ProProductProperty ppp = new ProProductProperty();
        ppp.setSpuId(spuId);
        ppp.setIsSku(Constant.IS_SKU_YES);
        List<ProProductProperty> list = proProductPropertyDao.getListByObj(ppp);
        if (list != null && list.size() > 0) {
            String[] skuStrs = skuPropStr.split(";");
            if (skuStrs.length == 1) {
                String values = skuStrs[0].split(":")[1];
                if (StringUtils.contains(list.get(0).getValueId(), values)) ;
                {
                    suc = false;
                }
            } else {
                String value1 = skuStrs[0].split(":")[1];
                String value2 = skuStrs[1].split(":")[1];
                if ((StringUtils.contains(list.get(0).getValueId(), value1) && StringUtils.contains(list.get(1).getValueId(), value2)) ||
                        (StringUtils.contains(list.get(0).getValueId(), value2) && StringUtils.contains(list.get(1).getValueId(), value1))) {
                    suc = false;
                }
            }
        }
        return suc;
    }

    public int updateBrandIdToOne(int newId, int oldId) {
        return proProductDao.updateBrandIdToOne(newId, oldId);
    }

}

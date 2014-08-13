package com.meiliwan.emall.pms.client;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.pms.bean.*;
import com.meiliwan.emall.pms.dto.*;

/**
 * User: wuzixin
 * Date: 13-6-8
 * Time: 上午10:24
 */
public class ProProductClient {

    public static String RESULTOBJ = "resultObj";

    /**
     * 增加商品
     *
     * @param product
     * @return
     */
    public static int addProProduct(ProProduct product) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proProductService/addProProduct", product));
        return obj.get("resultObj").getAsInt();
    }

    /**
     * 修改商品，这个功能包括修改商品相关属性、固有属性、以及相关的关系等信息
     *
     * @param product
     * @return
     */
    public static boolean updateProProduct(ProProduct product) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proProductService/updateProProduct", product));
        return obj.get("resultObj").getAsBoolean();
    }

    /**
     * 修改商品自定义属性固有属性
     *
     * @param list
     * @param proId
     * @return
     */
    public static boolean updateSkuSelfProperty(List<SkuSelfProperty> list, Integer proId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proProductService/updateSkuSelfProperty", list, proId));
        return obj.get("resultObj").getAsBoolean();
    }


    /**
     * 获得单个商品对象，只包括商品表的相关信息,不包括可使用库存
     *
     * @param proId
     * @return
     */
    public static SimpleProduct getProductById(Integer proId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proProductService/getProductById", proId));
        return new Gson().fromJson(obj.get("resultObj"), SimpleProduct.class);
    }

    /**
     * 通过商品ID获取商品相关信息，里面包含所要的可使用库存信息
     *
     * @param proId
     */
    public static SimpleProIfStock getProductWithStockById(Integer proId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proProductService/getProductWithStockById", proId));
        return new Gson().fromJson(obj.get("resultObj"), SimpleProIfStock.class);
    }

    /**
     * 修改商品的自身的相关属性，比如状态、价格等，也就是是说只修改商品表的相关字段
     */
    public static Boolean updateByProduct(ProProduct product) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proProductService/updateByProduct", product));
        return obj.get("resultObj").getAsBoolean();
    }

    /**
     * 批量更改商品状态
     *
     * @param ids
     * @param pro
     * @return
     */
    public static void changeState(int[] ids, ProProduct pro) {
        IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proProductService/updateState", ids, pro));
        //上线增加静态页

    }

    /**
     * 商品分页
     *
     * @param dto
     * @param pageInfo
     * @param order_name
     * @param order_form
     * @return
     */
    public static PagerControl<ProductDTO> pageByObj(ProductDTO dto,
                                                     PageInfo pageInfo, String order_name, String order_form) {

        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proProductService/pageByObj", dto, pageInfo, order_name, order_form));

        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<PagerControl<ProductDTO>>() {
        }.getType());
    }

    /**
     * 通过商品IDS，获取商品列表,不包括商品可使用库存
     *
     * @param ids
     * @return
     */
    public static List<SimpleProduct> getSimpleListByProIds(int[] ids) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proProductService/getSimpleListByProIds", ids));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<SimpleProduct>>() {
        }.getType());
    }

    /**
     * 通过商品IDS，获取商品列表,包括商品可使用库存
     *
     * @param ids
     * @return
     */
    public static List<SimpleProIfStock> getSimpleListWithStockByIds(int[] ids) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proProductService/getSimpleListWithStockByIds", ids));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<SimpleProIfStock>>() {
        }.getType());
    }

    /**
     * 通过商品IDS和商品名称查找商品列表，主要提供給用户收藏使用
     *
     * @param ids
     * @param proName
     * @return
     */
    public static List<SimpleProIfStock> getListByIdsAndName(int[] ids, String proName) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proProductService/getListByIdsAndName", ids, proName));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<SimpleProIfStock>>() {
        }.getType());
    }


    /**
     * 校验商品是否能够加入购物车
     *
     * @param proId
     */
    public static String checkJoinCart(int proId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proProductService/checkJoinCart", proId));
        return obj.get("resultObj").getAsString();
    }

    /**
     * 根据相关的字段名称修改相应的数据,例如修改商品美丽价等
     *
     * @param product
     * @return
     */
    public static boolean updateByPrice(ProProduct product) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proProductService/updateByPrice", product));
        return obj.get("resultObj").getAsBoolean();
    }

    /**
     * 获取整个商品实体对象
     *
     * @param proId
     * @return
     */
    public static ProProduct getWholeProductById(Integer proId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proProductService/getWholeProductById", proId));
        return new Gson().fromJson(obj.get("resultObj"), ProProduct.class);
    }

    /**
     * 获取sku信息用于后台sku查看
     * add
     *
     * @param proId
     * @return
     */
    public static ProProduct getProductForAdminShowById(Integer proId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proProductService/getProductForAdminShowById", proId));
        return new Gson().fromJson(obj.get("resultObj"), ProProduct.class);
    }

    /**
     * 对购物车里的每一个库存项商品状态进行校验
     * 业务场景：
     * 用户查看购物车(或者返回购物车时；或去查看购物车时)，进行商品有效性验证
     *
     * @param ids : 购物车商品列表;
     * @return items
     */
    public static List<ProductItemStatus> checkProStatusIfSub(int[] ids) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proProductService/checkProStatusIfSub", ids));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<ProductItemStatus>>() {
        }.getType());
    }

    /**
     * 对提交订单的订单行商品进行有效性，并返回每一个订单行商品的状态,及整个订单状态;
     * 业务场景：用户提交订单选择在线支付时，对每一个商品进行有效性校验
     *
     * @param ids 订单商品列表
     * @return orderItemStatus  订单商品行校验的结果
     */
    public static ProductOrderItemStatus checkProStatusIfOrderSub(List<Integer> ids) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proProductService/checkProStatusIfOrderSub", ids));
        return new Gson().fromJson(obj.get("resultObj"), ProductOrderItemStatus.class);
    }

    /**
     * 通过商品ID获取商品详细信息，包括商品描述等
     *
     * @param proId
     */
    public static ProDetail getProDetailByProId(int proId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proProductService/getProDetailByProId", proId));
        return new Gson().fromJson(obj.get("resultObj"), ProDetail.class);
    }

    /**
     * 检查商品的有效性
     *
     * @param proId
     */
    public static boolean checkProIsEffect(int proId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proProductService/checkProIsEffect", proId));
        return obj.get("resultObj").getAsBoolean();
    }

    /**
     * 检查商品条形码是否存在，保证商品条形码的唯一性
     *
     * @param barCode
     * @return
     */
    public static boolean checkProductByBarCode(String barCode, int proId, String type) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proProductService/checkProductByBarCode", barCode, proId, type));
        return obj.get("resultObj").getAsBoolean();
    }

    /**
     * add by yyluo
     * 用于商品详情页，
     * 获取商品详情所有信息业务缓存得不到时调此接口
     *
     * @param proId
     * @return
     */
    public static Map<String, String> getAllProDetail(Integer proId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proProductService/getAllProDetail", proId));
        Map<String, String> map = new Gson().fromJson(obj.get("resultObj"), new TypeToken<Map<String, String>>() {
        }.getType());
        return map;
    }

    /**
     * add by yyluo
     * 获取spu规格缓存
     * 获取spu规格缓存缓存得不到时调此接口
     *
     * @param spuId
     * @return
     */
    public static Map<String, String> getSpuStandard(Integer spuId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proProductService/getSpuStandard", spuId));
        Map<String, String> map = new Gson().fromJson(obj.get("resultObj"), new TypeToken<Map<String, String>>() {
        }.getType());
        return map;
    }


    /**
     * 修改SKU信息，同时增加SKU用户行为、以及价格记录等
     * 业务场景：管理后台sku编辑功能
     *
     * @param product
     * @return
     */
    public static boolean updateProAndAddAction(ProProduct product) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proProductService/updateProAndAddAction", product));
        return obj.get("resultObj").getAsBoolean();
    }

    /**
     * 只处理单规格和双规格，无规格不需处理
     * create by yiyou.luo
     *
     * @param spuId
     */
    public static boolean addSpuStandardInfoToCache(int spuId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proProductService/addSpuStandardInfoToCache", spuId));
        return obj.get("resultObj").getAsBoolean();
    }

    /**
     * 添加or更新 商品详情所有信息到Cache
     * create by yiyou.luo
     *
     * @param skuId
     */
    public static boolean addAllProDetailToCache(int skuId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proProductService/addAllProDetailToCache", skuId));
        return obj.get("resultObj").getAsBoolean();
    }

    /**
     * 根据条件，分页获取SPU列表，主要管理后台使用
     *
     * @param dto
     * @param pageInfo
     * @param order_name
     * @param order_form
     * @return
     */
    public static PagerControl<SpuListDTO> getSpuPageByObj(SpuListDTO dto, PageInfo pageInfo, String order_name, String order_form) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proProductService/getSpuPageByObj", dto, pageInfo, order_name, order_form));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<PagerControl<SpuListDTO>>() {
        }.getType());
    }

    /**
     * 根据spuId 获取对应的SKU列表
     *
     * @param spuId
     */
    public static List<SimpleProduct> getListProBySpuId(int spuId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proProductService/getListProBySpuId", spuId));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<SimpleProduct>>() {
        }.getType());
    }

    /**
     * 批量删除商品
     *
     * @param ids
     */
    public static boolean delProByIds(int[] ids) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proProductService/delProByIds", ids));
        return obj.get("resultObj").getAsBoolean();
    }

    /**
     * 批量下架商品
     *
     * @param ids
     */
    public static boolean updateStateToOffByIds(int[] ids) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proProductService/updateStateToOffByIds", ids));
        return obj.get("resultObj").getAsBoolean();
    }

    /**
     * 批量上架商品
     * 如果是第一次上架，那么更新上架时间，不是第一上架，不更新上架时间
     *
     * @param ids
     */
    public static Map<Integer, String> updateStateToONByIds(int[] ids) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proProductService/updateStateToONByIds", ids));
        Map<Integer, String> map = new Gson().fromJson(obj.get("resultObj").getAsString(), new TypeToken<Map<Integer, String>>() {
        }.getType());
        return map;
    }

    /**
     * 通过商品ID获取对应前台搜索列表对应的商品信息
     * 业务：做异步请求获取商前台列表单个商品信息
     *
     * @param proId
     */
    public static FrontListVo getFrontProById(int proId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proProductService/getFrontProById", proId));
        return new Gson().fromJson(obj.get("resultObj"), FrontListVo.class);
    }

    public static List<ProProduct> getListByUpdateTime(String min, String max) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proProductService/getListByUpdateTime", min, max));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<ProProduct>>() {
        }.getType());
    }

    /**
     * 通过spuID获取商品列表，主要提供oms使用
     *
     * @param spuId
     */
    public static List<ProProduct> getProductToOmsBySpuId(int spuId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proProductService/getProductToOmsBySpuId", spuId));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<ProProduct>>() {
        }.getType());
    }

    /**
     * 修改预售商品相关的时间
     *
     * @param proId
     * @param endTime
     * @param sendTime
     */
    public static int updatePresaleTime(int proId, String endTime, String sendTime) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proProductService/updatePresaleTime", proId,endTime,sendTime));
        return obj.get("resultObj").getAsInt();
    }
}

package com.meiliwan.emall.pms.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.pms.bean.ProDetail;
import com.meiliwan.emall.pms.bean.ProImages;
import com.meiliwan.emall.pms.bean.ProSpu;
import com.meiliwan.emall.pms.bean.SimpleProduct;
import com.meiliwan.emall.pms.dto.ProductDetailDTO;
import com.meiliwan.emall.pms.dto.ProductNamesDto;
import com.meiliwan.emall.pms.dto.ProductPublicParasDto;
import com.meiliwan.emall.pms.dto.SpuListDTO;

import java.util.List;
import java.util.Map;

/**
 * User: jiawu.wu
 * Date: 13-6-8
 * Time: 上午10:24
 */
public class ProSpuClient {

    private static final String RESULTOBJ = "resultObj";
    private static final String SERV_NAME="proSpuService/";

    /**
     * 商品SPU增加业务实现接口
     *
     * @param spu
     * @return
     */
    public static int addSpu(ProSpu spu) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERV_NAME + "addSpu", spu));
        return obj.get("resultObj").getAsInt();
    }

    /**
     * 增加SPU对应的图片地址
     *
     * @param spuId
     * @param imageses
     */
    public static int addImage(int spuId, List<ProImages> imageses) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERV_NAME + "addImage", spuId, imageses));
        return obj.get("resultObj").getAsInt();
    }

    /**
     * 根据spu ID获取spu信息
     *
     * @param spuId
     * @return
     */
    public static ProSpu getSpuById(int spuId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERV_NAME + "getSpuById", spuId));
        return new Gson().fromJson(obj.get("resultObj"), ProSpu.class);
    }


    /**
     * 根据SPU ID 获取SPU相关信息
     * 业务场景：商品编辑 信息包括：spu信息、商品详情、图片详情、固有属性
     *
     * @param spuId
     */
    public static ProSpu getAllSpuById(int spuId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERV_NAME + "getAllSpuById", spuId));
        return new Gson().fromJson(obj.get("resultObj"), ProSpu.class);
    }

    /**
     * 通过商品SPU ID 和属性值ID 获取对应的图片地址
     *
     * @param spuId
     * @param provId 属性值ID
     */
    public static ProImages getImagesById(int spuId, int provId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERV_NAME + "getImagesById", spuId, provId));
        return new Gson().fromJson(obj.get("resultObj"), ProImages.class);
    }

    /**
     * 通过三级类目ID获取对应类目下得商品列表
     *
     * @param categoryId
     */
    public static List<SimpleProduct> getListByThridCatId(int categoryId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERV_NAME + "getListByThridCatId", categoryId));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<SimpleProduct>>() {
        }.getType());
    }

    /**
     * 修改商品详细信息，detail表
     *
     * @param detail
     */
    public static boolean updateProDetail(ProDetail detail) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERV_NAME + "updateProDetail", detail));
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
                JSONTool.buildParams(SERV_NAME + "getSpuPageByObj", dto, pageInfo, order_name, order_form));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<PagerControl<SpuListDTO>>() {
        }.getType());
    }

    /**
     * 根据spuId删除spu信息，同时查找对应spu下得sku，置为删除状态
     *
     * @param spuId
     */
    public static boolean deleteSpu(int spuId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERV_NAME + "deleteSpu", spuId));
        return obj.get("resultObj").getAsBoolean();
    }

    /**
     * 商品SPU 更新商品标题
     *
     * @param dto
     * @return
     */
    public static boolean updateProductNames(ProductNamesDto dto) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERV_NAME + "updateProductNames", dto));
        return obj.get("resultObj").getAsBoolean();
    }

    /**
     * 更新公共参数
     * @param dto
     * @return
     */
    public static boolean updatePublicParas(ProductPublicParasDto dto) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERV_NAME + "updatePublicParas", dto));
        return obj.get("resultObj").getAsBoolean();
    }

    public static boolean updateProDetailByDto(ProductDetailDTO detailDTO){
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERV_NAME + "updateProDetailByDto", detailDTO));
        return obj.get("resultObj").getAsBoolean();
    }

    public static boolean updateSelfProperties(Map<String,Object> mapDto){
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERV_NAME + "updateSelfProperties", mapDto));
        return obj.get("resultObj").getAsBoolean();
    }


    public static boolean updateProProdProperty(Map<String,Object> mapDto){
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERV_NAME + "updateProProdProperty", mapDto));
        return obj.get("resultObj").getAsBoolean();
    }

    public static int updatePrimaryImage(Map<String,Object> mapDto){
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERV_NAME + "updatePrimaryImage", mapDto));
        return obj.get("resultObj").getAsInt();
    }


    public static int updateOrInsertImages(Map<String,Object> mapDto){
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERV_NAME + "updateOrInsertImages", mapDto));
        return obj.get("resultObj").getAsInt();
    }

    public static ProImages getImages(Map<String,Object> mapDto){
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERV_NAME + "getImages", mapDto));
        return new Gson().fromJson(obj.get("resultObj"), ProImages.class);
    }



}

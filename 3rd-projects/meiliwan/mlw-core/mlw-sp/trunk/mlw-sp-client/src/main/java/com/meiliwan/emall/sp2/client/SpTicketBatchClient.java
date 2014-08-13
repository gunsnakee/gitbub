package com.meiliwan.emall.sp2.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.sp2.bean.SpTicketBatch;
import com.meiliwan.emall.sp2.bean.SpTicketBatchProd;
import com.meiliwan.emall.sp2.bean.SpTicketImportDetail;
import com.meiliwan.emall.sp2.bean.SpTicketImportResult;
import com.meiliwan.emall.sp2.dto.TicketImportVo;
import com.meiliwan.emall.sp2.dto.TicketParms;

import java.util.List;

/**
 * User: wuzixin
 * Date: 14-5-29
 * Time: 上午10:31
 */
public class SpTicketBatchClient {

    /**
     * 增加优惠券批次
     *
     * @param batch
     */
    public static int insert(SpTicketBatch batch) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("spTicketBatchService/insert", batch));
        return obj.get("resultObj").getAsInt();
    }

    /**
     * 修改批次信息
     *
     * @param batch
     */
    public static int updateBatch(SpTicketBatch batch) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("spTicketBatchService/updateBatch", batch));
        return obj.get("resultObj").getAsInt();
    }

    /**
     * 修改关联活动URL
     *
     * @param batch
     */
    public static int updateActUrl(SpTicketBatch batch) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("spTicketBatchService/updateActUrl", batch));
        return obj.get("resultObj").getAsInt();
    }

    /**
     * 删除优惠券批次
     * 未上线的优惠券批次可以删除，而且硬删除数据
     *
     * @param batchId
     */
    public static int deleteBatch(int batchId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("spTicketBatchService/deleteBatch", batchId));
        return obj.get("resultObj").getAsInt();
    }

    /**
     * 根据批次号获取对应的优惠券批次
     *
     * @param batchId
     */
    public static SpTicketBatch getBatchById(int batchId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("spTicketBatchService/getBatchById", batchId));
        return new Gson().fromJson(obj.get("resultObj"), SpTicketBatch.class);
    }

    /**
     * 分页查找优惠券批次信息
     *
     * @param batch
     * @param pageInfo
     * @param order_name
     * @param order_form
     */
    public static PagerControl<SpTicketBatch> getPagerByObj(SpTicketBatch batch, PageInfo pageInfo, String order_name, String order_form) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("spTicketBatchService/getPagerByObj", batch, pageInfo, order_name, order_form));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<PagerControl<SpTicketBatch>>() {
        }.getType());
    }

    /**
     * 获取发送或导入优惠券相关信息
     *
     * @param result
     * @param pageInfo
     */
    public static PagerControl<SpTicketImportResult> getImportResultPageByObj(SpTicketImportResult result, PageInfo pageInfo) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("spTicketBatchService/getImportResultPageByObj", result, pageInfo));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<PagerControl<SpTicketImportResult>>() {
        }.getType());
    }

    public static List<SpTicketImportDetail> getImportDetailListByObj(SpTicketImportDetail detail) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("spTicketBatchService/getImportDetailListByObj", detail));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<SpTicketImportDetail>>() {
        }.getType());
    }

    /**
     * 批量插入优惠券批次和商品的关系
     *
     * @param list
     */
    public static int insertByBatchProd(List<SpTicketBatchProd> list) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("spTicketBatchService/insertByBatchProd", list));
        return obj.get("resultObj").getAsInt();
    }

    /**
     * 根据ID删除批次和商品的关系
     *
     * @param id
     */
    public static int deleteBatachProdById(int id) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("spTicketBatchService/deleteBatachProdById", id));
        return obj.get("resultObj").getAsInt();
    }

    /**
     * 根据批次表ID获取批次与商品关系列表
     *
     * @param batchId
     */
    public static List<SpTicketBatchProd> getBatchProdsByBatchId(int batchId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("spTicketBatchService/getBatchProdsByBatchId", batchId));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<SpTicketBatchProd>>() {
        }.getType());
    }

    /**
     * 修改优惠券批次，对批次上线操作
     *
     * @param batchId
     */
    public static int updateStateByOn(int batchId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("spTicketBatchService/updateStateByOn", batchId));
        return obj.get("resultObj").getAsInt();
    }

    /**
     * 更加ids获取商品参加的优惠券
     *
     * @param ids
     */
    public static List<SpTicketBatchProd> getTicketProdsByProIds(int[] ids) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("spTicketBatchService/getTicketProdsByProIds", ids));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<SpTicketBatchProd>>() {
        }.getType());
    }

    /**
     * 更加商品ID和批次ID删除商品
     *
     * @param proId
     * @param batchId
     */
    public static int deleteProdById(int proId, int batchId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("spTicketBatchService/deleteProdById", proId, batchId));
        return obj.get("resultObj").getAsInt();
    }

    /**
     * 发送优惠券给用户
     * 业务场景：包括单个发送和批量发送业务
     *
     * @param parms 参数
     * @throws Exception
     */
    public static boolean getTkSendToUser(List<TicketParms> parms, TicketImportVo vo) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("spTicketBatchService/getTkSendToUser", parms, vo));
        return obj.get("resultObj").getAsBoolean();
    }

    /**
     * 根据ID获取批量导入结果信息
     *
     * @param id
     */
    public static SpTicketImportResult getImportResultById(int id) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("spTicketBatchService/getImportResultById", id));
        return new Gson().fromJson(obj.get("resultObj"), SpTicketImportResult.class);
    }

    /**
     * 更加导入文件名 获取导入记录
     *
     * @param fileName
     */
    public static List<SpTicketImportResult> getListImportResultByFileName(String fileName) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("spTicketBatchService/getListImportResultByFileName", fileName));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<SpTicketImportResult>>() {
        }.getType());
    }

    public static PagerControl<SpTicketBatchProd> getTicketProdPagerByObj( SpTicketBatchProd prod, PageInfo pageInfo){
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("spTicketBatchService/getTicketProdPagerByObj", prod, pageInfo));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<PagerControl<SpTicketBatchProd>>() {
        }.getType());
    }
}

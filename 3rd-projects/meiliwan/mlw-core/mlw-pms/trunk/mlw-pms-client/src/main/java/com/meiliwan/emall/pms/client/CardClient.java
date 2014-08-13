package com.meiliwan.emall.pms.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.pms.bean.Card;
import com.meiliwan.emall.pms.bean.CardBatch;
import com.meiliwan.emall.pms.bean.CardImportLog;
import com.meiliwan.emall.pms.bean.CardImportResult;
import com.meiliwan.emall.pms.dto.CardParmsDTO;

import java.util.List;
import java.util.Map;

/**
 * User: wuzixin
 * Date: 13-12-27
 * Time: 上午11:01
 */
public class CardClient {

    /**
     * 增加礼品卡
     * 这个方法关联到每一个卡的生成业务
     *
     * @param batch
     * @return
     */
    public static Map<String, String> addCard(CardBatch batch) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("cardService/addCard", batch));
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Map<String, String> map = gson.fromJson(obj.get("resultObj").getAsString(), new TypeToken<Map<String, String>>() {
        }.getType());
        return map;
    }

    /**
     * 修改批次表
     *
     * @param batch
     * @return
     */
    public static boolean updateBatch(CardBatch batch) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("cardService/updateBatch", batch));
        return obj.get("resultObj").getAsBoolean();
    }

    /**
     * 修改提前到期提醒时间
     *
     * @param batchId
     * @param preDate
     * @return
     */
    public static boolean updateWarnDate(String batchId, int preDate) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("cardService/updateWarnDate", batchId, preDate));
        return obj.get("resultObj").getAsBoolean();
    }

    /**
     * 查询批次表，获取分页情况
     *
     * @param batch
     * @param pageInfo
     * @param order_name
     * @param order_form
     * @return
     */
    public static PagerControl<CardBatch> getBatchPageByObj(CardBatch batch, PageInfo pageInfo, String order_name, String order_form) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("cardService/getBatchPageByObj", batch, pageInfo, order_name, order_form));

        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<PagerControl<CardBatch>>() {
        }.getType());
    }

    /**
     * 查询礼品卡列表，进行分页
     *
     * @param card
     * @param pageInfo
     * @param order_name
     * @param order_form
     * @return
     */
    public static PagerControl<Card> getCardPageByObj(Card card, PageInfo pageInfo, String order_name, String order_form) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("cardService/getCardPageByObj", card, pageInfo, order_name, order_form));

        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<PagerControl<Card>>() {
        }.getType());
    }

    /**
     * 根据批量礼品卡号获取礼品列表并且包括相关的礼品卡批次信息
     *
     * @param ids
     * @return
     */
    public static List<Card> getCardWithBatchByIds(String[] ids) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildArrayParams("cardService/getCardWithBatchByIds", ids));

        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<Card>>() {
        }.getType());
    }

    /**
     * 获取礼品卡导入记录列表
     *
     * @param result
     * @param pageInfo
     * @param order_name
     * @param order_form
     * @return
     */
    public static PagerControl<CardImportResult> getImportResultByPager(CardImportResult result, PageInfo pageInfo, String order_name, String order_form) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("cardService/getImportResultByPager", result, pageInfo, order_name, order_form));

        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<PagerControl<CardImportResult>>() {
        }.getType());
    }

    /**
     * 获取礼品卡导入记录列表,无分页情况
     *
     * @param result
     * @return
     */
    public static List<CardImportResult> getImportResultListByResult(CardImportResult result) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("cardService/getImportResultListByResult", result));

        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<CardImportResult>>() {
        }.getType());
    }

    /**
     * 获取礼品卡导入日志明细记录
     *
     * @param log
     * @return
     */
    public static List<CardImportLog> getImportLogsByLog(CardImportLog log) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("cardService/getImportLogsByLog", log));

        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<CardImportLog>>() {
        }.getType());
    }

    /**
     * 通过批次ID获取礼品列表，并对密码解密
     * 排除过期和作废礼品卡
     *
     * @param batchId
     * @return list 解密后的列表
     */
    public static List<Card> getCardListByBatchId(String batchId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("cardService/getCardListByBatchId", batchId));

        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<Card>>() {
        }.getType());
    }

    /**
     * 通过礼品卡条件获取礼品卡列表
     *
     * @param card
     * @return
     */
    public static List<Card> getCardListByCard(Card card) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("cardService/getCardListByCard", card));

        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<Card>>() {
        }.getType());
    }

    /**
     * 激活卡动作
     *
     * @param dto
     * @return
     */
    public static Map<String, Object> activeCard(CardParmsDTO dto) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("cardService/updateActiveCard", dto));
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Map<String, Object> map = gson.fromJson(obj.get("resultObj").getAsString(), new TypeToken<Map<String, Object>>() {
        }.getType());
        return map;
    }

    /**
     * 冻结或者解冻礼品卡
     *
     * @param dto
     * @return
     */
    public static String freezeCard(CardParmsDTO dto) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("cardService/updateFreezeCard", dto));
        return obj.get("resultObj").getAsString();
    }

    /**
     * 批量作废礼品卡
     *
     * @param cardIds
     * @param userId
     * @param userName
     * @param descp
     * @return
     */
    public static boolean deleteCard(String[] cardIds, int userId, String userName, String descp) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("cardService/updateDeleteCard", cardIds, userId, userName, descp));
        return obj.get("resultObj").getAsBoolean();
    }

    /**
     * 修改销售状态，针对电子卡操作
     *
     * @param dto
     * @return
     */
    public static boolean sellCardByEp(CardParmsDTO dto) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("cardService/updateSellCardByEP", dto));
        return obj.get("resultObj").getAsBoolean();
    }

    /**
     * 通过批次号获取礼品卡批次信息
     *
     * @param batchId
     * @return
     */
    public static CardBatch getBatchById(String batchId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("cardService/getBatchById", batchId));
        return new Gson().fromJson(obj.get("resultObj"), CardBatch.class);
    }

    /**
     * 通过礼品卡ID获取礼品卡信息
     *
     * @param cardId
     * @return
     */
    public static Card getCardById(String cardId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("cardService/getCardById", cardId));
        return new Gson().fromJson(obj.get("resultObj"), Card.class);
    }

    /**
     * 获取礼品卡详情，包括卡对应的批次信息，以及相关操作记录
     *
     * @param cardId
     * @return
     */
    public static Card getCardDetailById(String cardId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("cardService/getCardDetailById", cardId));
        return new Gson().fromJson(obj.get("resultObj"), Card.class);
    }

    /**
     * 通过导入批次号查找导入批次结果
     *
     * @param batchId
     * @return
     */
    public static List<CardImportResult> getImportResultByBatchId(String batchId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("cardService/getImportResultByBatchId", batchId));

        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<CardImportResult>>() {
        }.getType());
    }

    /**
     * 导入购买者信息
     * 修改对应的库存以及致相关的状态
     *
     * @param list
     * @param dto
     * @return
     */
    public static boolean importCardByBuyer(List<Card> list, CardParmsDTO dto) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("cardService/importCardByBuyer", list, dto));
        return obj.get("resultObj").getAsBoolean();
    }

    /**
     * 定时扫描批次表提前提醒时间是否到期
     * 到期后，发送邮件，并且对持有该批次卡的状态为已销售、未激活、未过期的用户发送邮件或者短信
     *
     * @return
     */
    public static boolean getScheduledCard() {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("cardService/getScheduledCard"));
        return obj.get("resultObj").getAsBoolean();
    }
}

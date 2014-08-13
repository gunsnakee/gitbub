package com.meiliwan.emall.pms.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.base.client.BaseMailAndMobileClient;
import com.meiliwan.emall.base.dto.MailEntity;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.util.RegexUtil;
import com.meiliwan.emall.pms.bean.*;
import com.meiliwan.emall.pms.constant.Constant;
import com.meiliwan.emall.pms.dao.*;
import com.meiliwan.emall.pms.dto.CardParmsDTO;
import com.meiliwan.emall.pms.util.*;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

/**
 * 礼品卡业务层实现
 * User: wuzixin
 * Date: 13-12-25
 * Time: 下午4:02
 */
@Service
public class CardService extends DefaultBaseServiceImpl {
    private static final MLWLogger LOGGER = MLWLoggerFactory.getLogger(CardService.class);

    @Autowired
    private CardBatchDao cardBatchDao;
    @Autowired
    private CardDao cardDao;
    @Autowired
    private CardOptLogDao cardOptLogDao;
    @Autowired
    private CardImportLogDao cardImportLogDao;
    @Autowired
    private CardImportResultDao cardImportResultDao;


    /**
     * 增加礼品卡
     * 这个方法关联到每一个卡的生成业务
     *
     * @param resultObj
     * @param batch
     */
    public void addCard(JsonObject resultObj, CardBatch batch) {
        List<Card> cards = new ArrayList<Card>();
        List<CardOptLog> optLogs = new ArrayList<CardOptLog>();
        //系统生成礼品卡批次号
        String batchNo = "";
        String flag = "suc";
        try {
            batchNo = getBatchNO();
            int initNum = batch.getInitNum();
            for (int i = 0; i < initNum; i++) {
                Card card = new Card();
                //创建卡号
                String cardNo = getCardNo(batch.getCardType());
                card.setCardId(cardNo);
                card.setPassword(getCardPwd());
                card.setCardPrice(batch.getCardPrice());
                card.setBatchId(batchNo);
                card.setState(Constant.CARDUNSELL);
                card.setIsFreeze(Constant.CARDUNFREEZE);
                card.setIsDel(Constant.CARDUNDEL);
                card.setIsSell(Constant.CARDUNSELL);
                card.setActNum(batch.getActNum());

                CardOptLog log = new CardOptLog();
                log.setCardId(cardNo);
                log.setBatchId(batchNo);
                log.setOptType(CardOptType.CREATE.getCode());
                log.setUserId(batch.getAdminId());
                log.setUserName(batch.getAdminName());
                log.setDescp(CardOptType.CREATE.getDesc());

                cards.add(card);
                optLogs.add(log);
            }
            batch.setBatchId(batchNo);
            //增加批次表
            int count = cardBatchDao.insert(batch);
            if (count > 0) {
                //批量生成卡
                cardDao.insertByBatch(cards);
                //生成卡对应的记录
                cardOptLogDao.insertByBatch(optLogs);
            }
        } catch (Exception e) {
            flag = "error";
            //出现异常，需要批量作废礼品卡
            if (!StringUtils.isEmpty(batchNo)) {
                cardBatchDao.updateCardNumByDel(batchNo);
                cardDao.updateFreezeByBatchId(batchNo);
            }
            LOGGER.error(e, "生成礼品卡出现问题:对应批次号为" + batchNo, "");
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("batchNo", batchNo);
        map.put("flag", flag);
        addToResult(map.toString(), resultObj);
    }

    /**
     * 修改批次表
     *
     * @param resultObj
     * @param batch
     */
    public void updateBatch(JsonObject resultObj, CardBatch batch) {
        int count = cardBatchDao.update(batch);
        boolean suc = false;
        if (count > 0) {
            suc = true;
        }
        addToResult(suc, resultObj);
    }

    /**
     * 修改提前到期提醒时间
     *
     * @param resultObj
     * @param batchId
     */
    public void updateWarnDate(JsonObject resultObj, String batchId, int preDate) {
        //根据批次号获取对应批次的截止日期
        CardBatch batch = cardBatchDao.getEntityById(batchId);
        boolean suc = false;
        if (batch != null) {
            Date date = batch.getEndTime();
            String warnTime = DateUtil.formatDate(DateUtil.timeAddByDays(date, -preDate), DateUtil.FORMAT_DATETIME);
            int count = cardBatchDao.updateWarnDate(batchId, preDate, warnTime);
            if (count > 0) {
                suc = true;
            }
        }
        addToResult(suc, resultObj);
    }

    /**
     * 激活卡动作
     *
     * @param resultObj
     */
    public void updateActiveCard(JsonObject resultObj, CardParmsDTO dto) {
        Map<String, Object> map = getActiveCardResult(dto);
        addToResult(map.toString(), resultObj);
    }

    /**
     * 冻结或者解冻礼品卡
     *
     * @param resultObj
     * @param dto
     */
    public void updateFreezeCard(JsonObject resultObj, CardParmsDTO dto) {
        addToResult(getFreezeCardResult(dto), resultObj);
    }

    /**
     * 作废礼品卡
     *
     * @param resultObj
     * @param cardIds
     */
    public void updateDeleteCard(JsonObject resultObj, String[] cardIds, int userId, String userName, String descp) {
        List<Card> list = cardDao.getCardsByIds(cardIds);
        boolean suc = false;
        if (list != null && list.size() > 0) {
            Map<String, Object> map = getBatchWithCardsMap(list);
            //获取排除已激活状态的礼品卡对应的卡号list
            List<String> cards = (List<String>) map.get("cards");
            if (cards != null && cards.size() > 0) {
                //批量作废礼品卡
                int count = cardDao.updateDeleteCard(getCardIdByArray(cards));
                if (count > 0) {
                    //修改批次表的对应的作废数量
                    Map<String, Integer> cardMap = (Map<String, Integer>) map.get("cardMap");
                    Iterator iterator = cardMap.keySet().iterator();
                    while (iterator.hasNext()) {
                        String key = iterator.next().toString();
                        cardBatchDao.updateNumsAndStock(key, cardMap.get(key), CardOptFiled.DELNUM.getCode());
                    }
                    //增加卡的操作记录
                    List<CardOptLog> logs = new ArrayList<CardOptLog>();
                    for (String id : cards) {
                        CardOptLog log = new CardOptLog();
                        log.setCardId(id);
                        log.setOptType(CardOptType.DELETE.getCode());
                        log.setUserId(userId);
                        log.setUserName(userName);
                        log.setDescp(CardOptType.DELETE.getDesc() + "," + descp);

                        logs.add(log);
                    }
                    cardOptLogDao.insertByBatch(logs);
                }
            }
            suc = true;
        }
        addToResult(suc, resultObj);
    }

    /**
     * 修改销售状态，针对电子卡操作
     *
     * @param resultObj
     * @param dto
     */
    public void updateSellCardByEP(JsonObject resultObj, CardParmsDTO dto) {
        Card card = cardDao.getEntityById(dto.getCardId());
        boolean suc = false;
        if (card != null) {
            try {
                CardBatch batch = cardBatchDao.getEntityById(card.getBatchId());
                boolean flag = false;
                String sendC = "";
                //对密码进行解密操作，获取密码明文
                String pwd = CardEncryptUtil.decrypt(card.getPassword());
                String sendContent = "您好！感谢您购买" + batch.getCardPrice() + "元面额的美丽湾电子礼品卡。卡号" + card.getCardId() + ",卡密码" + pwd + "，请尽快登录www.meiliwan.com激活，激活截止日期:" + DateUtil.formatDate(batch.getEndTime(), DateUtil.FORMAT_DATE) + "，请尽快激活，激活后永久有效。客服电话4006887887。";
                if (dto.getState() == 0) {
                    //发送邮箱
                    sendC = "邮箱";
                    if (RegexUtil.isEmail(dto.getPassword())) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("cardid", card.getCardId());
                        map.put("cardpwd", pwd);
                        map.put("endtime", DateUtil.formatDate(batch.getEndTime(), DateUtil.FORMAT_DATE));
                        map.put("lasturl", "http://account.meiliwan.com/ucenter/giftcard/actview");
                        map.put("price", String.valueOf(card.getCardPrice()));
                        map.put("curryear", DateUtil.formatDate(new Date(), "yyyy"));
                        map.put("eid", card.getCardId());
                        int[] a = {70};
                        flag = BaseMailAndMobileClient.sendMailByDM(dto.getPassword().trim(), map, 54, 2, a);
                    }
                } else {
                    sendC = "手机";
                    //发送手机
                    if (RegexUtil.isPhone(dto.getPassword())) {
                        //发送短信
                        flag = BaseMailAndMobileClient.sendMobile(dto.getPassword().trim(), sendContent);
                    }
                }
                if (flag) {
                    cardDao.updateSellCard(card.getCardId(), dto.getState() == 0 ? CardOptFiled.EMAIL.getCode() : CardOptFiled.PHONE.getCode(), dto.getPassword());
                    //判断是否已经销售过
                    if (card.getIsSell() == Constant.CARDUNSELL) {
                        cardBatchDao.updateNumsAndStock(card.getBatchId(), 1, CardOptFiled.SELLNNUM.getCode());
                    }
                    CardOptLog log = new CardOptLog();
                    log.setCardId(card.getCardId());
                    log.setOptType(CardOptType.SELL.getCode());
                    log.setUserId(dto.getUserId());
                    log.setUserName(dto.getUserName());
                    log.setDescp(CardOptType.SELL.getDesc() + "发送" + sendC + ":" + dto.getPassword());
                    cardOptLogDao.insert(log);
                    suc = true;
                }
            } catch (Exception e) {
                LOGGER.error(e, "礼品卡发送邮件或者手机失败，params:" + dto, "");
            }
        }
        addToResult(suc, resultObj);
    }

    /**
     * 通过批次号获取礼品卡批次信息
     *
     * @param resultObj
     * @param batchId
     */
    public void getBatchById(JsonObject resultObj, String batchId) {
        addToResult(cardBatchDao.getEntityById(batchId), resultObj);
    }

    /**
     * 查询批次表，获取分页情况
     *
     * @param resultObj
     * @param batch
     * @param pageInfo
     * @param order_name
     * @param order_form
     */
    public void getBatchPageByObj(JsonObject resultObj, CardBatch batch, PageInfo pageInfo, String order_name, String order_form) {
        if (!StringUtils.isEmpty(batch.getCardName())) {
            batch.setCardName("%" + batch.getCardName() + "%");
        }

        StringBuilder orderBySql = new StringBuilder();
        if (!StringUtils.isEmpty(order_name) && !StringUtils.isEmpty(order_form)) {
            orderBySql.append(" order by ").append(order_name)
                    .append(" ").append(order_form);
        }
        PagerControl<CardBatch> pc = cardBatchDao.getPagerByObj(batch, pageInfo, null, orderBySql.toString());
        addToResult(pc, resultObj);
    }

    /**
     * 查询礼品卡列表，进行分页
     *
     * @param resultObj
     * @param card
     * @param pageInfo
     * @param order_name
     * @param order_form
     */
    public void getCardPageByObj(JsonObject resultObj, Card card, PageInfo pageInfo, String order_name, String order_form) {
        if (StringUtils.isNotEmpty(card.getUserName())) {
            card.setUserName("%" + card.getUserName() + "%");
        }

        if (StringUtils.isNotEmpty(card.getSellerName())) {
            card.setSellerName("%" + card.getSellerName() + "%");
        }

        StringBuilder orderBySql = new StringBuilder();
        if (StringUtils.isNotEmpty(order_name) && StringUtils.isNotEmpty(order_form)) {
            orderBySql.append(" order by ").append(order_name)
                    .append(" ").append(order_form);
        }

        PagerControl<Card> pc = cardDao.getPagerByObj(card, pageInfo, null, orderBySql.toString());
        addToResult(pc, resultObj);
    }

    /**
     * 获取礼品卡导入记录列表
     *
     * @param resultObj
     * @param result
     * @param pageInfo
     * @param order_name
     * @param order_form
     */
    public void getImportResultByPager(JsonObject resultObj, CardImportResult result, PageInfo pageInfo, String order_name, String order_form) {
        StringBuilder orderBySql = new StringBuilder();
        if (StringUtils.isNotEmpty(order_name) && StringUtils.isNotEmpty(order_form)) {
            orderBySql.append(" order by ").append(order_name)
                    .append(" ").append(order_form);
        }

        PagerControl<CardImportResult> pc = cardImportResultDao.getPagerByObj(result, pageInfo, null, orderBySql.toString());
        addToResult(pc, resultObj);
    }

    /**
     * 获取礼品卡导入记录列表,无分页情况
     *
     * @param resultObj
     * @param result
     */
    public void getImportResultListByResult(JsonObject resultObj, CardImportResult result) {
        addToResult(cardImportResultDao.getListByObj(result), resultObj);
    }

    /**
     * 获取礼品卡导入日志明细记录
     *
     * @param resultObj
     * @param log
     */
    public void getImportLogsByLog(JsonObject resultObj, CardImportLog log) {
        addToResult(cardImportLogDao.getListByObj(log), resultObj);
    }

    /**
     * 通过卡条件获取礼品卡列表
     *
     * @param resultObj
     * @param card
     */
    public void getCardListByCard(JsonObject resultObj, Card card) {
        List<Card> list = cardDao.getListByObj(card);
        addToResult(list, resultObj);
    }

    /**
     * 通过礼品卡ID获取礼品卡信息
     *
     * @param resultObj
     * @param cardId
     */
    public void getCardById(JsonObject resultObj, String cardId) {
        Card card = cardDao.getEntityById(cardId);
        addToResult(card, resultObj);
    }

    /**
     * 获取礼品卡详情，包括卡对应的批次信息，以及相关操作记录
     *
     * @param resultObj
     * @param cardId
     */
    public void getCardDetailById(JsonObject resultObj, String cardId) {
        Card card = cardDao.getEntityById(cardId);
        if (card != null) {
            //获取批次号
            CardBatch batch = cardBatchDao.getEntityById(card.getBatchId());
            //获取卡的相关操作记录
            CardOptLog log = new CardOptLog();
            log.setCardId(cardId);
            List<CardOptLog> logs = cardOptLogDao.getListByObj(log);
            card.setBatch(batch);
            card.setLogs(logs);
        }
        addToResult(card, resultObj);
    }

    /**
     * 通过批次ID获取礼品列表，并对密码解密
     * 排除过期和作废礼品卡
     *
     * @param resultObj
     * @param batchId
     */
    public void getCardListByBatchId(JsonObject resultObj, String batchId) throws Exception {
        Card card = new Card();
        card.setBatchId(batchId);
        List<Card> list = cardDao.getListByObj(card);
        if (list != null) {
            for (Card cd : list) {
                //对卡进行解密
                String pwd = CardEncryptUtil.decrypt(cd.getPassword());
                cd.setPassword(pwd);
            }
        }
        addToResult(list, resultObj);
    }

    /**
     * 根据批量礼品卡号获取礼品列表并且包括相关的礼品卡批次信息
     *
     * @param resultObj
     * @param ids
     */
    public void getCardWithBatchByIds(JsonObject resultObj, String[] ids) {
        List<Card> cards = null;
        if (ids != null && ids.length > 0) {
            cards = cardDao.getCardWithBatchByIds(ids);
        }
        addToResult(cards, resultObj);
    }

    /**
     * 通过导入批次号，获取导入结果信息
     *
     * @param resultObj
     * @param batchId
     */
    public void getImportResultByBatchId(JsonObject resultObj, String batchId) {
        CardImportResult result = new CardImportResult();
        result.setBatchId(batchId);
        List<CardImportResult> results = cardImportResultDao.getListByObj(result);
        addToResult(results, resultObj);
    }

    /**
     * 导入购买者信息
     * 导入成功后，更新相关的销售信息，并且记录相关的操作记录
     *
     * @param resultObj
     * @param list
     * @param dto
     */
    public void importCardByBuyer(JsonObject resultObj, List<Card> list, CardParmsDTO dto) {
        //导入记录
        List<CardImportResult> results = new ArrayList<CardImportResult>();
        List<CardImportLog> logs = new ArrayList<CardImportLog>();
        List<CardOptLog> optLogs = new ArrayList<CardOptLog>();
        Map<String, Integer> batchMap = new HashMap<String, Integer>();
        CardImportResult EP = new CardImportResult();
        CardImportResult LP = new CardImportResult();
        String batchId = dto.getBatchId().split("\\.")[0];
        boolean suc = true;
        try {
            for (Card card : list) {
                Card cd = cardDao.getEntityById(card.getCardId());
                CardImportLog log = new CardImportLog();
                log.setBatchId(batchId);
                log.setCardId(card.getCardId());
                log.setSellerName(card.getSellerName());
                if (StringUtils.isNotEmpty(card.getBuyerEmail())) {
                    log.setBuyerName(card.getBuyerEmail());
                } else {
                    log.setBuyerName(card.getBuyerPhone());
                }
                if (cd == null) {
                    if (card.getCardType() == Constant.LPCARD) {
                        LP.setCardType(Constant.LPCARD);
                        LP.setDismatchNum(LP.getDismatchNum() + 1);
                        LP.setTotalNum(LP.getTotalNum() + 1);
                        log.setCardType(Constant.LPCARD);
                    } else {
                        EP.setCardType(Constant.EPCARD);
                        EP.setTotalNum(EP.getTotalNum() + 1);
                        EP.setDismatchNum(EP.getDismatchNum() + 1);
                        log.setCardType(Constant.EPCARD);
                    }
                    log.setState(2);
                    log.setDescp("未找到匹配礼品卡");
                    logs.add(log);
                    continue;
                }
                //实体卡
                CardBatch batch = cardBatchDao.getEntityById(cd.getBatchId());
                if (card.getCardType() == Constant.LPCARD) {
                    LP.setCardType(Constant.LPCARD);
                    LP.setTotalNum(LP.getTotalNum() + 1);
                    try {
                        if (StringUtils.isNotEmpty(card.getBuyerEmail())) {
                            cardDao.updateSellByImport(card.getCardId(), CardOptFiled.EMAIL.getCode(), card.getBuyerEmail(), card.getSellerName());
                        } else {
                            cardDao.updateSellByImport(card.getCardId(), CardOptFiled.PHONE.getCode(), card.getBuyerPhone(), card.getSellerName());
                        }
                        log.setState(1);
                        log.setDescp("导入成功");
                    } catch (Exception e) {
                        LP.setErrorNum(LP.getErrorNum() + 1);
                        log.setState(0);
                        log.setDescp("导入失败");
                    }
                    log.setCardType(Constant.LPCARD);
                } else {
                    EP.setCardType(Constant.EPCARD);
                    EP.setTotalNum(EP.getTotalNum() + 1);
                    //对密码进行解密操作，获取密码明文
                    String pwd = CardEncryptUtil.decrypt(cd.getPassword());
                    String sendContent = "您好！感谢您购买" + batch.getCardPrice() + "元面额的美丽湾电子礼品卡。卡号" + card.getCardId() + "，卡密码" + pwd + "，请尽快登录www.meiliwan.com激活，激活截止日期:" + DateUtil.formatDate(batch.getEndTime(), DateUtil.FORMAT_DATE) + "，请尽快激活，激活后永久有效。客服电话4006887887。";
                    if (StringUtils.isNotEmpty(card.getBuyerEmail())) {
                        try {
                            int count = cardDao.updateSellByImport(card.getCardId(), CardOptFiled.EMAIL.getCode(), card.getBuyerEmail(), card.getSellerName());
                            if (count > 0) {
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("cardid", cd.getCardId());
                                map.put("cardpwd", pwd);
                                map.put("endtime", DateUtil.formatDate(batch.getEndTime(), DateUtil.FORMAT_DATE));
                                map.put("lasturl", "http://account.meiliwan.com/ucenter/giftcard/actview");
                                map.put("price", String.valueOf(batch.getCardPrice()));
                                map.put("curryear", DateUtil.formatDate(new Date(), "yyyy"));
                                map.put("eid", cd.getCardId());
                                try {
                                    int[] a = {70};
                                    boolean sendSuc = BaseMailAndMobileClient.sendMailByDM(card.getBuyerEmail().trim(), map, 54, 2, a);
                                    if (sendSuc) {
                                        EP.setSendSucNum(EP.getSendSucNum() + 1);
                                    } else {
                                        EP.setSendErrorNum(EP.getSendErrorNum() + 1);
                                    }
                                } catch (Exception e) {
                                    EP.setSendErrorNum(EP.getSendErrorNum() + 1);
                                    log.setIsSend(0);
                                    LOGGER.error(e, "导入购买者信息，发送邮件失败，batchId:" + batch + ",card:" + card, "");
                                }
                                log.setState(1);
                                log.setDescp("导入成功");
                            } else {
                                log.setState(0);
                                log.setDescp("导入失败");
                            }
                        } catch (Exception e) {
                            log.setState(0);
                            log.setDescp("导入失败");
                        }
                    } else {//发送手机
                        try {
                            int count = cardDao.updateSellByImport(card.getCardId(), CardOptFiled.PHONE.getCode(), card.getBuyerPhone(), card.getSellerName());
                            if (count > 0) {
                                try {
                                    boolean sendSuc = BaseMailAndMobileClient.sendMobile(card.getBuyerPhone().trim(), sendContent);
                                    if (sendSuc) {
                                        EP.setSendSucNum(EP.getSendSucNum() + 1);
                                    } else {
                                        EP.setSendErrorNum(EP.getSendErrorNum() + 1);
                                    }
                                } catch (Exception e) {
                                    EP.setSendErrorNum(EP.getSendErrorNum() + 1);
                                    log.setIsSend(0);
                                    LOGGER.error(e, "导入购买者信息，发送手机失败，batchId:" + batch + ",card:" + card, "");
                                }
                                log.setState(1);
                                log.setDescp("导入成功");
                            } else {
                                log.setState(0);
                                log.setDescp("导入失败");
                            }
                        } catch (Exception e) {
                            log.setState(0);
                            log.setDescp("导入失败");
                        }
                    }
                    log.setCardType(Constant.EPCARD);
                }
                //增加礼品卡相关的记录信息
                log.setCardBatchId(batch.getBatchId());
                log.setCardPrice(batch.getCardPrice());
                log.setCardName(batch.getCardName());
                logs.add(log);

                //卡的操作记录
                CardOptLog optLog = new CardOptLog();
                optLog.setCardId(cd.getCardId());
                optLog.setBatchId(cd.getBatchId());
                optLog.setUserId(dto.getUserId());
                optLog.setUserName(dto.getUserName());
                optLog.setOptType(CardOptType.SELL.getCode());
                optLog.setDescp(CardOptType.SELL.getDesc() + ",导入购买者信息");
                optLogs.add(optLog);
                //排除作废情况
                if (cd.getIsDel() == Constant.CARDUNDEL && cd.getIsSell() == Constant.CARDUNSELL) {
                    Integer bhct = batchMap.get(cd.getBatchId());
                    batchMap.put(cd.getBatchId(), bhct == null ? 1 : bhct + 1);
                }
            }

            if (LP.getCardType() != null) {
                LP.setBatchId(batchId);
                LP.setFileName(dto.getBatchId());
                LP.setAdminId(dto.getUserId());
                LP.setAdminName(dto.getUserName());
                results.add(LP);
            }
            if (EP.getCardType() != null) {
                EP.setBatchId(batchId);
                EP.setFileName(dto.getBatchId());
                EP.setAdminId(dto.getUserId());
                EP.setAdminName(dto.getUserName());
                results.add(EP);
            }

            //修改库存，修改批次表对应的信息
            Iterator iterator = batchMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next().toString();
                cardBatchDao.updateNumsAndStock(key, batchMap.get(key), CardOptFiled.SELLNNUM.getCode());
            }
            //增加卡操作记录
            if (optLogs != null && optLogs.size() > 0) {
                cardOptLogDao.insertByBatch(optLogs);
            }
            //增加导入记录
            if (results != null && results.size() > 0) {
                cardImportResultDao.insertByBatch(results);
            }
            if (logs != null && logs.size() > 0) {
                cardImportLogDao.insertByBatch(logs);
            }
        } catch (Exception e) {
            LOGGER.error(e, "导入购买者信息失败,List:" + list.toString() + ",dto:" + dto.toString(), "");
            suc = false;
        }
        addToResult(suc, resultObj);
    }

    /**
     * 定时扫描批次表提前提醒时间是否到期
     * 到期后，发送邮件，并且对持有该批次卡的状态为已销售、未激活、未过期的用户发送邮件或者短信
     *
     * @param resultObj
     */
    public void getScheduledCard(JsonObject resultObj) {
        String date = DateUtil.formatDate(new Date(), DateUtil.FORMAT_DATETIME);
        String whereSql = "warn_time <='" + date + "' and end_time >= '" + date + "'";
        boolean suc = true;
        try {
            List<CardBatch> batches = cardBatchDao.getListByObj(null, null, whereSql, null);
            if (batches != null && batches.size() > 0) {
                for (CardBatch batch : batches) {

                    Card card = new Card();
                    card.setBatchId(batch.getBatchId());
                    card.setIsSell(Constant.CARDSELL);
                    card.setState(Constant.CARDUNACTIVE);
                    List<Card> cards = cardDao.getListByObj(card);
                    if (cards != null && cards.size() > 0) {
                        for (Card cd : cards) {
                            if (StringUtils.isNotEmpty(cd.getBuyerPhone())) {
                                String sendContent = "您在美丽湾（www.meiliwan.com）购买的礼品卡即将过期，礼品卡密码：" + CardEncryptUtil.decrypt(cd.getPassword()) + "，请尽快到网站激活，激活后存入礼品卡账户，余额可永久使用";
                                BaseMailAndMobileClient.sendMobile(cd.getBuyerPhone().trim(), sendContent);
                            }
                            if (StringUtils.isNotEmpty(cd.getBuyerEmail())) {
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("cardid", cd.getCardId());
                                map.put("cardpwd", CardEncryptUtil.decrypt(cd.getPassword()));
                                map.put("endtime", DateUtil.formatDate(batch.getEndTime(), DateUtil.FORMAT_DATE));
                                map.put("lasturl", "http://account.meiliwan.com/ucenter/giftcard/actview");
                                map.put("price", String.valueOf(cd.getCardPrice()));
                                map.put("curryear", DateUtil.formatDate(new Date(), "yyyy"));
                                map.put("eid", cd.getCardId());
                                int[] a = {70};
                                BaseMailAndMobileClient.sendMailByDM(cd.getBuyerEmail().trim(), map, 55, 2, a);
                            }
                        }
                    }
                    //发送邮件提醒给卡制作人
                    String cardType = batch.getCardType() == Constant.LPCARD ? "实体卡" : "电子卡";
                    String sc = "美丽湾礼品卡过期提醒，批次为:" + batch.getBatchId() + ",礼品卡名称:" + batch.getCardName() + ",类型为:" + cardType + ",即将过期，请知悉";
                    MailEntity mail = new MailEntity();
                    mail.setSender("admin@meiliwan.com");
                    mail.setSenderName("美丽湾管理员");
                    mail.setReceivers(batch.getAdminEmail());
                    mail.setTitle("美丽湾礼品卡即将过期提醒");
                    mail.setContent(sc);
                    BaseMailAndMobileClient.sendMail(mail);
                }
            }
        } catch (Exception e) {
            LOGGER.error(e, "礼品卡截止提前提醒日期到期提醒失败,date:" + date, "");
            suc = false;
        }
        addToResult(suc, resultObj);
    }

    /**
     * 获取礼品卡批次号
     *
     * @return
     */
    private String getBatchNO() {
        boolean first = true;
        String batchNo = RandomUtils.getDateStrAndNum(4, "yyyyMMddHHmm");
        while (first) {
            CardBatch batch = cardBatchDao.getEntityById(batchNo);
            if (batch == null) {
                first = false;
            } else {
                batchNo = RandomUtils.getDateStrAndNum(4, "yyyyMMddHHmm");
            }
        }
        return batchNo;
    }

    /**
     * 获取礼品卡的卡号
     *
     * @return
     */
    private String getCardNo(int type) throws JedisClientException {
        boolean first = true;
        String cardNo = RandomUtils.getCardNoStr(type, new Date());
        while (first) {
            String redsKdy = DateUtil.formatDate(new Date(), "HHmm");
            //判读是否已经存在卡号
            boolean suc = ShardJedisTool.getInstance().sismember(JedisKey.pms$cardNo, redsKdy, cardNo);
            if (suc) {
                cardNo = RandomUtils.getCardNoStr(type, new Date());
            } else {
                //添加到缓存
                first = false;
                ShardJedisTool.getInstance().sadd(JedisKey.pms$cardNo, redsKdy, cardNo);
            }
        }
        return cardNo;
    }

    /**
     * 获取礼品卡密码
     *
     * @return
     * @throws JedisClientException
     */
    private String getCardPwd() throws Exception {
        boolean first = true;
        String cardPwd = CardEncryptUtil.encrypt(RandomUtils.getStrAndNum(16));
        while (first) {
            //先查找缓存
            boolean suc = ShardJedisTool.getInstance().sismember(JedisKey.pms$cardpwd, "", cardPwd);
            if (suc) {
                cardPwd = CardEncryptUtil.encrypt(RandomUtils.getStrAndNum(16));
            } else {
                first = false;
                ShardJedisTool.getInstance().sadd(JedisKey.pms$cardpwd, "", cardPwd);
            }
        }
        return cardPwd;
    }

    private Map<String, Object> getActiveCardResult(CardParmsDTO dto) {
        //对密码进行加密
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String pwdStr = CardEncryptUtil.encrypt(dto.getPassword());
            //获取卡，走主库
            Card card = cardDao.getCardByPwd(pwdStr);
            if (card != null) {
                //判断是否是活动礼品卡
                if (card.getActNum() > 0) {
                    int actNum = cardDao.getCountByUserId(dto.getUserId(),card.getBatchId())+1;
                    if (actNum > card.getActNum().intValue()){
                        map.put("status", CardStatus.LIMITACTNUM);
                        map.put("actNum",card.getActNum());
                        LOGGER.warn("激活礼品卡，改礼品卡为活动礼品卡，对应用户已激活到上限，无法再激活", card, "");
                        return map;
                    }
                }
                //是否激活
                if (card.getState() == Constant.CARDACTIVE) {
                    map.put("status", CardStatus.ACTIVE);
                    LOGGER.warn("激活礼品卡，礼品卡已激活", card, "");
                    return map;
                }
                //是否作废
                if (card.getIsDel() == Constant.CARDDEL) {
                    map.put("status", CardStatus.DELETE);
                    LOGGER.warn("激活礼品卡，礼品卡已作废", card, "");
                    return map;
                }
                CardBatch batch = cardBatchDao.getEntityById(card.getBatchId());
                //判断是否过期
                if (batch.getEndTime().compareTo(new Date()) < 0) {
                    map.put("status", CardStatus.EXPIRED);
                    LOGGER.warn("激活礼品卡，礼品卡已过期", card, "");
                    return map;
                }

                //是否冻结
                if (card.getIsFreeze() == Constant.CARDFREEZE) {
                    map.put("status", CardStatus.FREEZE);
                    LOGGER.warn("激活礼品卡，礼品卡已冻结", card, "");
                    return map;
                }

                //激活卡动作
                int count = cardDao.updateActiveCard(card.getCardId(), dto.getUserId(), dto.getUserName());
                if (count > 0) {
                    //修改批次表的激活数量
                    cardBatchDao.updateNumsByAdd(batch.getBatchId(), 1, CardOptFiled.ACTIVENUM.getCode());
                    //记录操作记录
                    CardOptLog log = new CardOptLog();
                    log.setCardId(card.getCardId());
                    log.setBatchId(batch.getBatchId());
                    log.setUserId(dto.getUserId());
                    log.setUserName(dto.getUserName());
                    log.setDescp(CardOptType.ACTIVE.getDesc() + ",绑定账号:" + dto.getUserName());
                    log.setOptType(CardOptType.ACTIVE.getCode());
                    cardOptLogDao.insert(log);
                    map.put("status", CardStatus.SUCCESS);
                    map.put("cardId", card.getCardId());
                    map.put("price", card.getCardPrice());
                    return map;
                }
            } else {
                LOGGER.warn("激活礼品卡，礼品卡密码错误", card, "");
            }
        } catch (Exception e) {
            LOGGER.error(e, "激活卡失败", "");
        }
        map.put("status", CardStatus.FAILURE);
        return map;
    }

    private String getFreezeCardResult(CardParmsDTO dto) {
        Card card = cardDao.getEntityById(dto.getCardId(), true);
        if (card != null) {
            CardBatch batch = cardBatchDao.getEntityById(card.getBatchId());
            //判断是否过期
            if (batch.getEndTime().compareTo(new Date()) < 0) {
                LOGGER.warn("冻结或者解冻礼品卡，礼品卡已过期", card, "");
                return CardStatus.EXPIRED.getCode();
            }
            //是否作废
            if (card.getIsDel() == Constant.CARDDEL) {
                LOGGER.warn("冻结或者解冻礼品卡，礼品卡已作废", card, "");
                return CardStatus.DELETE.getCode();
            }
            //判断是冻结还是解冻
            if (Constant.CARDFREEZE == dto.getState()) {
                //是否激活
                if (card.getState() == Constant.CARDACTIVE) {
                    LOGGER.warn("冻结或者解冻礼品卡，礼品卡已激活", card, "");
                    return CardStatus.ACTIVE.getCode();
                }
            }
            //执行冻结或者解冻操作
            int count = cardDao.updateFreezeCard(dto.getCardId(), dto.getState());
            if (count > 0) {
                //记录操作记录
                CardOptLog log = new CardOptLog();
                log.setCardId(dto.getCardId());
                log.setBatchId(batch.getBatchId());
                log.setUserId(dto.getUserId());
                log.setUserName(dto.getUserName());
                log.setDescp(dto.getDescp());
                //判断是冻结还是解冻
                if (Constant.CARDFREEZE == dto.getState()) {
                    //批次表冻结数量增加1
                    cardBatchDao.updateNumsByAdd(batch.getBatchId(), 1, CardOptFiled.FREEZENUM.getCode());
                    log.setOptType(CardOptType.FREEZE.getCode());
                } else {
                    //批次表解冻数量减少1
                    cardBatchDao.updateNumsBySub(batch.getBatchId(), 1, CardOptFiled.FREEZENUM.getCode());
                    log.setOptType(CardOptType.UNFREEZE.getCode());
                }
                cardOptLogDao.insert(log);
                return CardStatus.SUCCESS.getCode();
            }
        }
        return CardStatus.FAILURE.getCode();
    }

    private Map<String, Object> getBatchWithCardsMap(List<Card> list) {
        Map<String, Object> map = new HashMap<String, Object>();
        //用于保存每个批次礼品卡作废的数量
        Map<String, Integer> cardMap = new HashMap<String, Integer>();
        //用于批量操作礼品
        List<String> cards = new ArrayList<String>();
        for (Card card : list) {
            //排除激活的礼品卡
            if (card.getState() != Constant.CARDACTIVE) {
                //已经销售过不能再减库存
                if (card.getIsSell() == Constant.CARDUNSELL) {
                    Integer count = cardMap.get(card.getBatchId());
                    cardMap.put(card.getBatchId(), count == null ? 1 : count + 1);
                } else {
                    cardBatchDao.updateNumsByAdd(card.getBatchId(), 1, CardOptFiled.DELNUM.getCode());
                }
                cards.add(card.getCardId());
            }
        }
        map.put("cardMap", cardMap);
        map.put("cards", cards);
        return map;
    }

    private String[] getCardIdByArray(List<String> cards) {
        String[] cardStr = new String[cards.size()];
        String[] cardIds = cards.toArray(cardStr);
        return cardIds;
    }
}

package com.meiliwan.emall.pms.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.pms.BaseTest;
import com.meiliwan.emall.pms.bean.Card;
import com.meiliwan.emall.pms.bean.CardBatch;
import com.meiliwan.emall.pms.constant.Constant;
import com.meiliwan.emall.pms.dto.CardParmsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * User: wuzixin
 * Date: 13-12-29
 * Time: 下午12:36
 */
public class CardServiceTest extends BaseTest {
    @Autowired
    private CardService cardService;

    private JsonObject resultObj = new JsonObject();

    @Test
    public void testAddCard() throws Exception {
        CardBatch batch = new CardBatch();
        batch.setCardName("元旦祝福卡");
        batch.setCardPrice(new BigDecimal("100"));
        batch.setInitNum(10);
        batch.setCardNum(10);
        batch.setCardType(Constant.LPCARD);
        batch.setState(Constant.CARDUNExport);
        batch.setValidMonth(36);
        batch.setPreWarnDay(50);
        batch.setAdminId(11111);
        batch.setAdminName("伍小西");
        batch.setAdminEmail("1032222@qq.com");
        Date endDate = DateUtil.timeAddByMonth(new Date(), 36);
        batch.setEndTime(endDate);
        batch.setWarnTime(DateUtil.timeAddByDays(endDate, -50));
        cardService.addCard(resultObj, batch);
        String batchNo = resultObj.get("resultObj").getAsString();
        System.out.println(batchNo);
    }


    @Test
    public void testUpdateWarnDate() throws Exception {
       cardService.updateWarnDate(resultObj,"2013122912404430",30);
    }

    @Test
    public void testUpdateActiveCard() throws Exception {
        CardParmsDTO dto = new CardParmsDTO();
        dto.setPassword("HQ323WKSCSPSYOW2");
        dto.setUserId(11111);
        dto.setUserName("伍小西");
        dto.setState(Constant.CARDACTIVE);

        cardService.updateActiveCard(resultObj,dto);
    }

    @Test
    public void testUpdateFreezeCard() throws Exception {
        CardParmsDTO dto = new CardParmsDTO();
        dto.setCardId("LP0007131229124059");
        dto.setUserId(11111);
        dto.setUserName("伍小西");
        dto.setState(Constant.CARDFREEZE);
        dto.setDescp("冻结冻结");

        cardService.updateFreezeCard(resultObj,dto);
    }

    @Test
    public void testUpdateDeleteCard() throws Exception {
        String[] strings = new String[]{"LP3908131229124059","LP5590131229124059","LP5131131229132509","LP0337131229132508"};
        cardService.updateDeleteCard(resultObj,strings,1111,"伍小西","作废一些卡");
    }

    public void testGetBatchById() throws Exception {

    }

    public void testGetBatchPageByObj() throws Exception {

    }

    public void testGetCardById() throws Exception {

    }

    public void testGetCardDetailById() throws Exception {

    }

    @Test
    public void testGetCardPageByObj(){
        cardService.getCardPageByObj(resultObj,new Card(),new PageInfo(1,40),null,null);
    }

    @Test
    public void testUpdateSellCardByEP(){
        CardParmsDTO dto = new CardParmsDTO();
        dto.setCardId("EP4035131229191357");
        dto.setState(0);
        dto.setUserId(1111);
        dto.setUserName("伍小西");
        dto.setPassword("420014425@qq.com");
        cardService.updateSellCardByEP(resultObj,dto);
    }

    @Test
    public void testGetScheduledCard(){
        cardService.getScheduledCard(resultObj);
    }

    @Test
    public void testCardPwd() throws Exception {
        cardService.getCardListByBatchId(resultObj,"2014010711218057");
        List<Card> list = new Gson().fromJson(resultObj.get("resultObj"), new TypeToken<List<Card>>() {
        }.getType());
        List<String> set = new ArrayList<String>();
        for (Card card : list){
            set.add(card.getPassword());
        }
        Collections.sort(set);
        for (String pwd:set){
            System.out.println(pwd);
        }
    }

}

package com.meiliwan.emall.pms.client;

import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.pms.bean.CardBatch;
import com.meiliwan.emall.pms.constant.Constant;
import com.meiliwan.emall.pms.dto.CardParmsDTO;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * User: wuzixin
 * Date: 13-12-29
 * Time: 下午2:41
 */
public class CardClientTest {
    @Test
    public void testAddCard() throws Exception {
        CardBatch batch = new CardBatch();
        batch.setCardName("元旦祝福卡");
        batch.setCardPrice(new BigDecimal("100"));
        batch.setInitNum(5);
        batch.setCardNum(5);
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

        Map<String, String> map = CardClient.addCard(batch);
        System.out.println("#########:"+map.toString());
    }

    @Test
    public void testUpdateWarnDate() throws Exception {
       CardClient.updateWarnDate("2013122912404430",30);
    }

    @Test
    public void testGetBatchPageByObj() throws Exception {

    }

    @Test
    public void testActiveCard() throws Exception {
        CardParmsDTO dto = new CardParmsDTO();
        dto.setCardId("LP9154131229191724");
        dto.setUserId(11111);
        dto.setUserName("伍小西");
        dto.setState(Constant.CARDFREEZE);
        dto.setDescp("冻结冻结");
         Map<String,Object> map = CardClient.activeCard(dto);
        System.out.println("#####:"+map.toString());
    }

    @Test
    public void testFreezeCard() throws Exception {

    }

    @Test
    public void testDeleteCard() throws Exception {

    }

    @Test
    public void testGetBatchById() throws Exception {

    }

    @Test
    public void testGetCardById() throws Exception {

    }

    @Test
    public void testGetCardDetailById() throws Exception {

    }
}

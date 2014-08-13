package com.meiliwan.emall.bkstage.web.controller.giftcard.template;

import com.meiliwan.emall.account.bean.CardOptRecord;
import com.meiliwan.emall.account.bean.WalletOptRecord;
import com.meiliwan.emall.account.client.GiftCardClient;
import com.meiliwan.emall.account.client.WalletOptRecordClient;
import com.meiliwan.emall.bkstage.web.util.PoiExcelUtil;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.mms.bean.UserPassportSimple;
import org.apache.poi.hssf.usermodel.HSSFRow;

import java.util.*;

/**
 * 钱包使用类
 * User: wenlepeng
 * Date: 13-11-27
 * Time: 上午11:19
 * To change this template use File | Settings | File Templates.
 */
public class CardPayReports extends Export<CardOptRecord> {
    public static final MLWLogger logger = MLWLoggerFactory.getLogger(CardPayReports.class);

    public CardPayReports(String sheetName, String title[], String fileName){
        super(sheetName,title,fileName);
        this.fileName = fileName;
    }
    @Override
    public void fillRowData(List<CardOptRecord> subList, Map<Integer, UserPassportSimple> map) {
        for(int inner=0;inner<subList.size();inner++){
            HSSFRow rowdata = sheet.createRow(rowNum);
            //填充数据
            CardOptRecord record = subList.get(inner);
            UserPassportSimple userPassportSimple = map.get(record.getUid());
            if(userPassportSimple != null){
                rowdata.createCell(0).setCellValue(record.getOrderId());
                rowdata.createCell(1).setCellValue(userPassportSimple.getUserName());
                rowdata.createCell(2).setCellValue(userPassportSimple.getNickName());
                rowdata.createCell(3).setCellValue(DateUtil.getDateTimeStr(record.getOptTime()));
                double money = record.getMoney().doubleValue() ;
                String payType="购物";
                if("REFUND_FROM_GATEWAY".equals(record.getOptType())){
                    payType = "退款";
                    money = -money;
                }

                rowdata.createCell(4).setCellValue(money);
                rowdata.createCell(5).setCellValue(payType);
                rowdata.createCell(6).setCellValue(userPassportSimple.getEmail());
                rowdata.createCell(7).setCellValue(userPassportSimple.getMobile());

                PoiExcelUtil.dealWithCellsNullString(rowdata, 0, 7);
                rowNum++;
            }else{
                logger.warn("uid "+record.getUid()+" 异常,此id 存在于礼品卡操作记录表，但是不存在于用户表",null,null);
            }
        }
    }

    @Override
    public List<CardOptRecord> getDataFromDB(String startDate, String endDate) {
        String whereSql = new StringBuilder("opt_time >='").append(startDate).append("' and opt_time <='").append(endDate).append("' and (opt_type='SUB_MONEY'  or opt_type='REFUND_FROM_GATEWAY') and success_flag =1").toString();
        PagerControl<CardOptRecord> page = GiftCardClient.getOptRecordByPage(pageInfo, new CardOptRecord(), whereSql);
        if(page == null) return Collections.EMPTY_LIST;
        pageInfo = page.getPageInfo();
        return page.getEntityList();
    }

    @Override
    public Integer[] getUids(List<CardOptRecord> list) {
        if(list == null && list.size() == 0) return null;
        Set<Integer> uidsSet = new HashSet<Integer>();
        for(CardOptRecord cardOptRecord:list){
            uidsSet.add(cardOptRecord.getUid());
        }
        return uidsSet.toArray(new Integer[0]);
    }


}

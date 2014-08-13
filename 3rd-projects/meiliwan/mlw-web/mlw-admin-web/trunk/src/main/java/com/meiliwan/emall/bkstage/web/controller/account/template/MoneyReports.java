package com.meiliwan.emall.bkstage.web.controller.account.template;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFRow;

import com.meiliwan.emall.account.bean.AccountWallet;
import com.meiliwan.emall.account.client.AccountWalletClient;
import com.meiliwan.emall.bkstage.web.util.PoiExcelUtil;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.mms.bean.UserPassportSimple;

/**
 * 余额总览表
 * User: wenlepeng
 * Date: 13-11-27
 * Time: 上午11:41
 * To change this template use File | Settings | File Templates.
 */
public class MoneyReports extends Export<AccountWallet> {

    public static final MLWLogger logger = MLWLoggerFactory.getLogger(MoneyReports.class);

    public MoneyReports(String sheetName,String title[],String fileName){
        super(sheetName,title,fileName);
        this.fileName = fileName;
    }

    @Override
    public void fillRowData(List<AccountWallet> sublist, Map<Integer, UserPassportSimple> map) {
        for(int inner=0;inner<sublist.size();inner++){
            HSSFRow rowdata = sheet.createRow(rowNum);
            //填充数据
            AccountWallet record = sublist.get(inner);
            UserPassportSimple userPassportSimple = map.get(record.getUid());
            if(userPassportSimple != null){
                rowdata.createCell(0).setCellValue(userPassportSimple.getUserName());
                rowdata.createCell(1).setCellValue(record.getMlwCoin().doubleValue()+record.getFreezeAmount().doubleValue());
                String userState = "正常";
                if(userPassportSimple.getState() == -1){
                    userState = "封锁";
                }
                rowdata.createCell(2).setCellValue(userState);
                rowdata.createCell(3).setCellValue(userPassportSimple.getNickName());
                rowdata.createCell(4).setCellValue(userPassportSimple.getEmail());
                rowdata.createCell(5).setCellValue(userPassportSimple.getMobile());
                PoiExcelUtil.dealWithCellsNullString(rowdata, 0, 6);
                rowNum++;
            }else{
                logger.warn("uid "+record.getUid()+" 异常,此id 存在于钱包操作记录表，但是不存在于用户表",null,null);
            }
        }
    }

    @Override
    public List<AccountWallet> getDataFromDB(String startDate, String endDate) {
        String whereSql = "mlw_coin > 0 or freeze_amount > 0";
        PagerControl<AccountWallet> page = AccountWalletClient.getRecordByPage(pageInfo,null,whereSql);
        if(page == null) return Collections.EMPTY_LIST;
        pageInfo = page.getPageInfo();
        return page.getEntityList();
    }

    @Override
    public Integer[] getUids(List<AccountWallet> list) {
        if(list == null && list.size() == 0) return null;
        Set<Integer> uidsSet = new HashSet<Integer>();
        for(AccountWallet accountWallet:list){
            uidsSet.add(accountWallet.getUid());
        }
        return uidsSet.toArray(new Integer[0]);
    }
}

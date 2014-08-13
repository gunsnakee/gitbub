package com.meiliwan.emall.bkstage.web.Scheduled;

import com.meiliwan.emall.base.util.MailUtils;
import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.stock.bean.SafeStockItem;
import com.meiliwan.emall.stock.client.ProStockClient;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.zookeeper.KeeperException;

import javax.mail.MessagingException;

import java.io.*;
import java.util.Date;
import java.util.List;

/**
 * 安全库存 定时工具线程程序
 * User: wuzixin
 * Date: 13-10-25
 * Time: 下午2:23
 */
public class SafeStockUtil {
    private final static MLWLogger logger = MLWLoggerFactory.getLogger(SafeStockUtil.class);

    private static volatile int safeTime;

    public static int getSafeTime() {
        return safeTime;
    }

    public static void setSafeTime(int safeTime) {
        SafeStockUtil.safeTime = safeTime;
    }

    public static void safeStockCheck() throws IOException {
        String mailUrl = null;
        try {
            mailUrl = MailUtils.getSafeStockMail();
        } catch (KeeperException e) {
            logger.error(new BaseException("安全库存报警-获取安全库存对应邮件人失败",e), "mailUrl", "");
        } catch (InterruptedException e) {
            logger.error(new BaseException("安全库存报警-获取安全库存对应邮件人失败",e), "mailUrl", "");
        } catch (IOException e) {
            logger.error(new BaseException("安全库存报警-获取安全库存对应邮件人失败",e), "mailUrl", "");
        }
        if (mailUrl != null && !mailUrl.equals("")) {
            List<SafeStockItem> list = ProStockClient.getSafeStockList();
            if (list != null && list.size() > 0) {
                //生成表格开始
                // 创建一个EXCEL
                HSSFWorkbook wb = new HSSFWorkbook();
                // 创建一个SHEET
                HSSFSheet sheet1 = wb.createSheet("安全库存校验");
                String[] title = {"商品ID", "商品条形码", "商品名称", "所属馆名称", "美丽价", "可用库存", "安全库存"};
                int i = 0;
                // 创建一行
                HSSFRow row = sheet1.createRow((short) 0);
                // 填充标题
                for (String s : title) {
                    HSSFCell cell = row.createCell(i);
                    cell.setCellValue(s);
                    i++;
                }
                int rowNum = 1;//新行号
                for (SafeStockItem item : list) {
                    HSSFRow rowdata = sheet1.createRow(rowNum);
                    // 下面是填充数据
                    rowdata.createCell(0).setCellValue(item.getProId() + "");
                    rowdata.createCell(1).setCellValue(item.getBarCode() + "");
                    rowdata.createCell(2).setCellValue(item.getProName() + "");
                    rowdata.createCell(3).setCellValue(item.getStoreName() + "");
                    rowdata.createCell(4).setCellValue(item.getMlwPrice() + " ");
                    rowdata.createCell(5).setCellValue(item.getSellStock() + "");
                    rowdata.createCell(6).setCellValue(item.getSafeStock() + "");
                    rowNum++;
                }
                String exportPath = "/data/www/safe/stock";
                File file = new File(exportPath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                //生成Excel文件
                String exportName = new Date().getTime() + "";
                String path = exportPath + exportName + "_safeStock.xls";
                FileOutputStream fileOut = null;
                try {
                    fileOut = new FileOutputStream(path);
                } catch (FileNotFoundException e) {
                    logger.error(new BaseException("安全库存报警-写入文件失败",e), "mailUrl", "");
                }
                wb.write(fileOut);
                try {
                    fileOut.close();
                } catch (IOException e) {
                    logger.error(new BaseException("安全库存报警-写入文件失败",e), "mailUrl", "");
                }

                //发送邮件
                String subject = "美丽湾管理后台-安全库存报警";
                String body = "hi,all!目前存在相关的商品可使用库存低于安全库存，请查收邮件，商品列表见附件，谢谢!";
                try {
                    MailUtils.sendMail("admin@meiliwan.com", mailUrl, subject, body, "美丽湾管理后台", path);
                } catch (UnsupportedEncodingException e) {
                    logger.error(new BaseException("安全库存报警-发送邮件失败",e), "mailUrl", "");
                } catch (MessagingException e) {
                    logger.error(new BaseException("安全库存报警-发送邮件失败",e), "mailUrl", "");
                }
            }
        }

    }


}

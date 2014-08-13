package com.meiliwan.emall.bkstage.web.controller.card;

import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.bkstage.web.util.PoiExcelUtil;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.exception.IceServiceRuntimeException;
import com.meiliwan.emall.pms.bean.Card;
import com.meiliwan.emall.pms.bean.CardBatch;
import com.meiliwan.emall.pms.client.CardClient;
import com.meiliwan.emall.pms.constant.Constant;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 卡批次管理
 * User: wuzixin
 * Date: 13-12-29
 * Time: 下午4:00
 */
@Controller("batchController")
@RequestMapping("/card/batch")
public class BatchController {

    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());
    private final static String exportPath = "/data/www/exportfiles/";

    @RequestMapping(value = "/list")
    public String list(HttpServletRequest request, Model model) {
        String batchId = ServletRequestUtils.getStringParameter(request, "batchId", null);
        String cardName = ServletRequestUtils.getStringParameter(request, "cardName", null);
        int cardType = ServletRequestUtils.getIntParameter(request, "cardType", 99);
        int state = ServletRequestUtils.getIntParameter(request, "state", 99);

        String createTimeMin = ServletRequestUtils.getStringParameter(request, "createTimeMin", null);
        String createTimeMax = ServletRequestUtils.getStringParameter(request, "createTimeMax", null);

        String endTimeMin = ServletRequestUtils.getStringParameter(request, "endTimeMin", null);
        String endTimeMax = ServletRequestUtils.getStringParameter(request, "endTimeMax", null);

        String warnTimeMin = ServletRequestUtils.getStringParameter(request, "warnTimeMin", null);
        String warnTimeMax = ServletRequestUtils.getStringParameter(request, "warnTimeMax", null);


        CardBatch batch = new CardBatch();
        if (!StringUtils.isEmpty(batchId)) {
            batch.setBatchId(batchId);
        }
        if (!StringUtils.isEmpty(cardName)) {
            batch.setCardName(cardName);
        }
        if (cardType != 99) {
            batch.setCardType(cardType);
        }
        if (state != 99) {
            batch.setState(state);
        }

        if (!StringUtils.isEmpty(createTimeMin) && !StringUtils.isEmpty(createTimeMax)) {
            batch.setCreateTimeMin(DateUtil.parseTimestamp(createTimeMin));
            batch.setCreateTimeMax(DateUtil.parseTimestamp(createTimeMax));
        }

        if (!StringUtils.isEmpty(endTimeMin) && !StringUtils.isEmpty(endTimeMax)) {
            batch.setEndTimeMin(DateUtil.parseTimestamp(endTimeMin));
            batch.setEndTimeMax(DateUtil.parseTimestamp(endTimeMax));
        }

        if (!StringUtils.isEmpty(warnTimeMin) && !StringUtils.isEmpty(warnTimeMax)) {
            batch.setWarnTimeMin(DateUtil.parseTimestamp(warnTimeMin));
            batch.setWarnTimeMax(DateUtil.parseTimestamp(warnTimeMax));
        }
        PageInfo pageInfo = StageHelper.getPageInfo(request, "update_time", "desc");
        PagerControl<CardBatch> pc = CardClient.getBatchPageByObj(batch, pageInfo, pageInfo.getOrderField(), pageInfo.getOrderDirection());
        batch.setCardType(cardType);
        batch.setState(state);
        model.addAttribute("pc", pc);
        model.addAttribute("batch", batch);
        model.addAttribute("currentTime", new Date());
        return "/card/batch/list";
    }

    /**
     * 创建礼品卡
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/add")
    public String add(HttpServletRequest request, HttpServletResponse response, Model model) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", 0);
        if (isHandle > 0) {
            int cardType = ServletRequestUtils.getIntParameter(request, "cardType", 0);
            String cardName = ServletRequestUtils.getStringParameter(request, "cardName", null);
            String cardPrice = ServletRequestUtils.getStringParameter(request, "cardPrice", "0");
            int vaildNum = ServletRequestUtils.getIntParameter(request, "vaildNum", 0);
            int initNum = ServletRequestUtils.getIntParameter(request, "initNum", 0);
            int preWarnDay = ServletRequestUtils.getIntParameter(request, "preWarnDay", 0);
            int actNum = ServletRequestUtils.getIntParameter(request,"actNum",0);
            String adminEmail = ServletRequestUtils.getStringParameter(request, "adminEmail", null);

            CardBatch batch = new CardBatch();
            batch.setCardType(cardType);
            batch.setCardName(cardName);
            batch.setCardPrice(new BigDecimal(cardPrice));
            batch.setValidMonth(vaildNum);
            batch.setInitNum(initNum);
            batch.setCardNum(initNum);
            batch.setPreWarnDay(preWarnDay);
            batch.setActNum(actNum);
            batch.setAdminId(StageHelper.getLoginUser(request).getBksAdmin().getAdminId());
            batch.setAdminName(StageHelper.getLoginUser(request).getBksAdmin().getAdminName());
            batch.setAdminEmail(adminEmail);
            Date endDate = DateUtil.timeAddByMonth(new Date(), vaildNum);
            batch.setEndTime(endDate);
            batch.setWarnTime(DateUtil.timeAddByDays(endDate, -preWarnDay));
            try {
                Map<String, String> map = CardClient.addCard(batch);
                if (map.get("flag").equals("suc")) {
                    return StageHelper.writeDwzSignal("200", "创建礼品卡成功", "10247", StageHelper.DWZ_CLOSE_CURRENT, "/card/batch/list", response);
                } else {
                    return StageHelper.writeDwzSignal("200", "创建礼品卡失败，请联系管理员", "10247", StageHelper.DWZ_FORWARD, "/card/batch/add", response);
                }
            } catch (Exception e) {
                logger.error(e, "创建礼品卡失败,batch=" + batch.toString(), WebUtils.getIpAddr(request));
                return StageHelper.writeDwzSignal("200", "创建礼品卡失败，请联系管理员", "115", StageHelper.DWZ_FORWARD, "/card/batch/add", response);
            }

        }
        return "/card/batch/add";
    }

    /**
     * 查看礼品卡批次详情
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/detail")
    public String detail(HttpServletRequest request, Model model) {
        String batchId = ServletRequestUtils.getStringParameter(request, "batchId", "0");
        CardBatch batch = CardClient.getBatchById(batchId);
        model.addAttribute("currentTime",new Date());
        model.addAttribute("batch", batch);
        return "/card/batch/detail";
    }

    /**
     * 修改截止提前提醒时间
     *
     * @param request
     * @param response
     * @throws java.io.IOException
     */
    @RequestMapping(value = "/update")
    public void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String batchId = ServletRequestUtils.getStringParameter(request, "batchId", "0");
        int preWarnDay = ServletRequestUtils.getIntParameter(request, "preWarnDay", 0);
        boolean suc = CardClient.updateWarnDate(batchId, preWarnDay);
        if (suc) {
            response.getWriter().print(1);
        } else {
            response.getWriter().print(0);
        }
    }

    @RequestMapping(value = "/batch-export")
    public void batchExport(HttpServletRequest request, HttpServletResponse response, Model model) {
        String batchId = ServletRequestUtils.getStringParameter(request, "batchId", "0");
        List<Card> list = CardClient.getCardListByBatchId(batchId);
        try {
            if (list != null && list.size() > 0) {
                String sheet1Name = "礼品卡-实体卡";
                // 创建一个EXCEL
                HSSFWorkbook wb = new HSSFWorkbook();
                // 创建一个SHEET
                HSSFSheet sheet1 = wb.createSheet(sheet1Name);
                String[] titles = {"礼品卡卡号", "礼品卡密码", "面额", "礼品卡名称", "礼品卡批次"};
                PoiExcelUtil.buildTitles(sheet1, titles);
                int rowNum = 1;//新行号
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    Card card = list.get(i);
                    HSSFRow rowdata = sheet1.createRow(rowNum);
                    // 下面是填充数据
                    rowdata.createCell(0).setCellValue(card.getCardId());// 礼品卡卡号
                    rowdata.createCell(1).setCellValue(card.getPassword());//礼品卡密码
                    rowdata.createCell(2).setCellValue(card.getCardPrice().toString());//面额
                    rowdata.createCell(3).setCellValue(card.getCardName());// 礼品卡名称
                    rowdata.createCell(4).setCellValue(card.getBatchId());//礼品卡批次
                    rowNum++;
                }
                String writeName = "实体卡-" + batchId;
                PoiExcelUtil.createFile(exportPath, writeName, wb);  //在服务器端写文件
                PoiExcelUtil.download(request, response, "xls", exportPath + writeName, writeName + ".xls");  //在服务器端下载指定文件，完成后删除该文件

                //修改导出批次状态
                CardBatch batch = new CardBatch();
                batch.setBatchId(batchId);
                batch.setState(Constant.CARDExport);
                CardClient.updateBatch(batch);
            }
        } catch (IceServiceRuntimeException e) {
            logger.error(e, "按批次导出礼品出现异常,batchId:" + batchId, WebUtils.getIpAddr(request));
            StageHelper.writeDwzSignal("300", "操作失败，请联系管理员", "", StageHelper.DWZ_FORWARD, "/card/card/send", response);
        } catch (Exception e) {
            logger.error(e, "按批次导出礼品出现异常,batchId:" + batchId, WebUtils.getIpAddr(request));
            StageHelper.writeDwzSignal("300", "操作失败，请联系管理员", "", StageHelper.DWZ_FORWARD, "/card/card/send", response);
        }
    }

//    /**
//     * 定期扫描截止日提前提醒时间，然后发送相关邮件
//     */
//    @Scheduled(cron = "0 0 9 * * ?")
//    private void warnScheduledDate() {
//        boolean suc = CardClient.getScheduledCard();
//        if (suc) {
//            System.out.println("扫描邮件成功");
//        }else {
//            System.out.println("扫描邮件失敗");
//        }
//    }
}

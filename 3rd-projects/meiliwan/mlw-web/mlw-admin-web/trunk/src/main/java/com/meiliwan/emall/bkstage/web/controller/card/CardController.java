package com.meiliwan.emall.bkstage.web.controller.card;

import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.bkstage.web.util.PoiExcelUtil;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.util.RegexUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.exception.IceServiceRuntimeException;
import com.meiliwan.emall.pms.bean.Card;
import com.meiliwan.emall.pms.bean.CardImportLog;
import com.meiliwan.emall.pms.bean.CardImportResult;
import com.meiliwan.emall.pms.client.CardClient;
import com.meiliwan.emall.pms.constant.Constant;
import com.meiliwan.emall.pms.dto.CardParmsDTO;
import com.meiliwan.emall.pms.util.CardOptType;
import com.meiliwan.emall.pms.util.CardStatus;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * 礼品卡管理
 * User: wuzixin
 * Date: 13-12-30
 * Time: 上午11:37
 */
@Controller("cardController")
@RequestMapping("/card/card")
public class CardController {

    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());
    private final static String exportPath = "/data/www/card/exportfiles/";

    @RequestMapping(value = "/list")
    public String list(HttpServletRequest request, Model model) {
        Card card = getCard(request);
        int isFreeze = ServletRequestUtils.getIntParameter(request, "isFreeze", 99);
        int isSell = ServletRequestUtils.getIntParameter(request, "isSell", 99);
        int state = ServletRequestUtils.getIntParameter(request, "state", 99);
        int cardType = ServletRequestUtils.getIntParameter(request, "cardType", 99);
        int isDel = ServletRequestUtils.getIntParameter(request, "isDel", 99);

        if (isFreeze != 99) {
            card.setIsFreeze(isFreeze);
        }
        if (isSell != 99) {
            card.setIsSell(isSell);
        }
        if (state != 99) {
            card.setState(state);
        }
        if (cardType != 99) {
            card.setCardType(cardType);
        }
        if (isDel != 99) {
            card.setIsDel(isDel);
        }


        PageInfo pageInfo = StageHelper.getPageInfo(request, "create_time", "desc");
        PagerControl<Card> pc = CardClient.getCardPageByObj(card, pageInfo, pageInfo.getOrderField(), pageInfo.getOrderDirection());

        Map<String, Integer> countMap = new HashMap<String, Integer>();
        countMap.put("total", 0);
        countMap.put("expired", 0);
        countMap.put("unexpired", 0);
        countMap.put("sell", 0);
        countMap.put("del", 0);
        countMap.put("active", 0);
        countMap.put("freeze", 0);
        if (pc.getEntityList() != null && pc.getEntityList().size() > 0) {
            List<Card> list = CardClient.getCardListByCard(card);
            if (list != null && list.size() > 0) {
                countMap.put("total", countMap.get("total") + list.size());
                for (Card cd : list) {
                    if (cd.getEndTime().compareTo(new Date()) < 0) {
                        countMap.put("expired", countMap.get("expired") + 1);
                    } else {
                        countMap.put("unexpired", countMap.get("unexpired") + 1);
                    }
                    if (cd.getIsSell() == Constant.CARDSELL) {
                        countMap.put("sell", countMap.get("sell") + 1);
                    }
                    if (cd.getIsDel() == Constant.CARDDEL) {
                        countMap.put("del", countMap.get("del") + 1);
                    }
                    if (cd.getState() == Constant.CARDACTIVE) {
                        countMap.put("active", countMap.get("active") + 1);
                    }
                    if (cd.getIsFreeze() == Constant.CARDFREEZE) {
                        countMap.put("freeze", countMap.get("freeze") + 1);
                    }
                }
            }
        }

        card.setState(state);
        card.setIsFreeze(isFreeze);
        card.setIsSell(isSell);
        card.setIsDel(isDel);
        card.setCardType(cardType);
        model.addAttribute("pc", pc);
        model.addAttribute("card", card);
        model.addAttribute("currentTime", DateUtil.getCurrentDateTimeStr());
        model.addAttribute("countMap", countMap);
        return "/card/card/list";
    }

    /**
     * 查看礼品卡详情
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/detail")
    public String detail(HttpServletRequest request, Model model) {
        String cardId = ServletRequestUtils.getStringParameter(request, "cardId", "0");
        Card card = CardClient.getCardDetailById(cardId);
        model.addAttribute("card", card);
        return "/card/card/detail";
    }

    /**
     * 冻结或者解冻礼品卡
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/freeze")
    public String freeze(HttpServletRequest request, HttpServletResponse response, Model model) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", 0);
        String cardId = ServletRequestUtils.getStringParameter(request, "cardId", "0");
        int state = ServletRequestUtils.getIntParameter(request, "state", 0);
        if (isHandle > 0) {
            String descp = ServletRequestUtils.getStringParameter(request, "descp", "");
            CardParmsDTO dto = new CardParmsDTO();
            dto.setCardId(cardId);
            dto.setState(state);
            dto.setUserId(StageHelper.getLoginUser(request).getBksAdmin().getAdminId());
            dto.setUserName(StageHelper.getLoginUser(request).getBksAdmin().getAdminName());
            if (state == 0) {
                dto.setDescp(CardOptType.UNFREEZE.getDesc() + ",原因是:" + descp);
            } else {
                dto.setDescp(CardOptType.FREEZE.getDesc() + ",原因是:" + descp);
            }
            try {
                String result = CardClient.freezeCard(dto);
                if (result.equals(CardStatus.SUCCESS.getCode())) {
                    return StageHelper.writeDwzSignal("200", "操作成功", "10251", StageHelper.DWZ_CLOSE_CURRENT, "/card/card/list", response);
                } else {
                    return StageHelper.writeDwzSignal("300", "操作失败，请联系管理员", "10253", StageHelper.DWZ_FORWARD, "/card/card/freeze", response);
                }
            } catch (IceServiceRuntimeException e) {
                logger.error(e, "冻结或解冻礼品卡失败，" + dto.toString(), WebUtils.getIpAddr(request));
                return StageHelper.writeDwzSignal("300", "操作失败，请联系管理员", "10253", StageHelper.DWZ_FORWARD, "/card/card/freeze", response);
            } catch (Exception e) {
                logger.error(e, "冻结或解冻礼品卡失败，" + dto.toString(), WebUtils.getIpAddr(request));
                return StageHelper.writeDwzSignal("300", "操作失败，请联系管理员", "10253", StageHelper.DWZ_FORWARD, "/card/card/freeze", response);
            }
        }
        model.addAttribute("cardId", cardId);
        model.addAttribute("state", state);
        return "/card/card/freeze";
    }

    /**
     * 作废礼品卡
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/del")
    public String del(HttpServletRequest request, HttpServletResponse response, Model model) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", 0);
        if (isHandle > 0) {
            String idStr = ServletRequestUtils.getStringParameter(request, "idStr", null);
            if (StringUtils.isNotEmpty(idStr)) {
                try {
                    String[] ids = idStr.split(",");
                    String descp = ServletRequestUtils.getStringParameter(request, "descp", "");
                    boolean suc = CardClient.deleteCard(ids, StageHelper.getLoginUser(request).getBksAdmin().getAdminId(), StageHelper.getLoginUser(request).getBksAdmin().getAdminName(), descp);
                    if (suc) {
                        return StageHelper.writeDwzSignal("200", "操作成功", "10251", StageHelper.DWZ_CLOSE_CURRENT, "/card/card/list", response);
                    } else {
                        return StageHelper.writeDwzSignal("300", "操作失败，请联系管理员", "", StageHelper.DWZ_FORWARD, "/card/card/del", response);
                    }
                } catch (IceServiceRuntimeException e) {
                    logger.error(e, "作废礼品卡失败，ids=" + idStr, WebUtils.getIpAddr(request));
                    return StageHelper.writeDwzSignal("300", "商品修改失败", "119", StageHelper.DWZ_FORWARD, "/pms/product/update", response);
                } catch (Exception e) {
                    logger.error(e, "作废礼品卡失败，ids=" + idStr, WebUtils.getIpAddr(request));
                    return StageHelper.writeDwzSignal("300", "操作失败，请联系管理员", "", StageHelper.DWZ_FORWARD, "/card/card/del", response);
                }

            }
            return StageHelper.writeDwzSignal("300", "请选择礼品信息", "", StageHelper.DWZ_FORWARD, "/card/card/del", response);
        }
        String type = ServletRequestUtils.getStringParameter(request, "type", "batch");
        if (type.equals("single")) {
            String cardId = ServletRequestUtils.getStringParameter(request, "cardId", "");
            model.addAttribute("cardId", cardId);
        }
        model.addAttribute("type", type);
        return "/card/card/delete";
    }

    @RequestMapping(value = "/send")
    public String send(HttpServletRequest request, HttpServletResponse response, Model model) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", 0);
        String cardId = ServletRequestUtils.getStringParameter(request, "cardId", "0");
        if (isHandle > 0) {
            int type = ServletRequestUtils.getIntParameter(request, "type", 0);
            String email = ServletRequestUtils.getStringParameter(request, "email", null);
            String phone = ServletRequestUtils.getStringParameter(request, "phone", null);
            CardParmsDTO dto = new CardParmsDTO();
            dto.setCardId(cardId);
            dto.setUserId(StageHelper.getLoginUser(request).getBksAdmin().getAdminId());
            dto.setUserName(StageHelper.getLoginUser(request).getBksAdmin().getAdminName());
            dto.setPassword(type == 0 ? email : phone);
            dto.setState(type);
            try {
                boolean suc = CardClient.sellCardByEp(dto);
                if (suc) {
                    return StageHelper.writeDwzSignal("200", "操作成功", "10251", StageHelper.DWZ_CLOSE_CURRENT, "/card/card/list", response);
                } else {
                    return StageHelper.writeDwzSignal("300", "操作失败，请联系管理员", "", StageHelper.DWZ_FORWARD, "/card/card/send", response);
                }
            } catch (IceServiceRuntimeException e) {
                logger.error(e, "礼品卡发送邮箱或者手机失败,cardId:" + cardId, WebUtils.getIpAddr(request));
                return StageHelper.writeDwzSignal("300", "操作失败，请联系管理员", "", StageHelper.DWZ_FORWARD, "/card/card/send", response);
            } catch (Exception e) {
                logger.error(e, "礼品卡发送邮箱或者手机失败,cardId:" + cardId, WebUtils.getIpAddr(request));
                return StageHelper.writeDwzSignal("300", "操作失败，请联系管理员", "", StageHelper.DWZ_FORWARD, "/card/card/send", response);
            }
        }
        Card card = CardClient.getCardById(cardId);
        int flag = 1;
        if (StringUtils.isNotEmpty(card.getBuyerPhone()) && StringUtils.isEmpty(card.getBuyerEmail())) {
            flag = 2;
        }
        model.addAttribute("flag", flag);
        model.addAttribute("card", card);
        return "/card/card/send";
    }

    /**
     * 导出礼品卡统计表
     *
     * @param request
     * @param response
     * @param model
     */
    @RequestMapping(value = "/get-count-export")
    public void getCountExport(HttpServletRequest request, HttpServletResponse response, Model model) {
        Card card = getCard(request);
        int isFreeze = ServletRequestUtils.getIntParameter(request, "isFreeze", 99);
        int isSell = ServletRequestUtils.getIntParameter(request, "isSell", 99);
        int state = ServletRequestUtils.getIntParameter(request, "state", 99);
        int cardType = ServletRequestUtils.getIntParameter(request, "cardType", 99);
        int isDel = ServletRequestUtils.getIntParameter(request, "isDel", 99);

        if (isFreeze != 99) {
            card.setIsFreeze(isFreeze);
        }
        if (isSell != 99) {
            card.setIsSell(isSell);
        }
        if (state != 99) {
            card.setState(state);
        }
        if (cardType != 99) {
            card.setCardType(cardType);
        }
        if (isDel != 99) {
            card.setIsDel(isDel);
        }
        try {
            List<Card> list = CardClient.getCardListByCard(card);
            if (list != null && list.size() > 0) {
                if (list.size() > 100000){
                    StageHelper.writeDwzSignal("300", "导出数量大于100000条，请重新赛选", "10251", StageHelper.DWZ_CLOSE_CURRENT, "/card/card/list", response);
                }
                String sheet1Name = "礼品卡统计表";
                // 创建一个EXCEL
                HSSFWorkbook wb = new HSSFWorkbook();
                // 创建一个SHEET
                HSSFSheet sheet1 = wb.createSheet(sheet1Name);
                String[] titles = {"礼品卡批次号", "礼品卡卡号", "礼品卡名称", "面额(￥)", "礼品卡状态", "销售状态", "销售时间", "冻结状态", "激活状态", "激活时间"};
                PoiExcelUtil.buildTitles(sheet1, titles);
                int rowNum = 1;//新行号
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    Card cd = list.get(i);
                    HSSFRow rowdata = sheet1.createRow(rowNum);
                    // 下面是填充数据
                    rowdata.createCell(0).setCellValue(cd.getBatchId());// 礼品卡批次号
                    rowdata.createCell(1).setCellValue(cd.getCardId());//礼品卡卡号
                    rowdata.createCell(2).setCellValue(cd.getCardName());// 礼品卡名称
                    rowdata.createCell(3).setCellValue(cd.getCardPrice().toString());// 面额
                    if (cd.getEndTime().compareTo(new Date()) < 0) { //过期状态
                        rowdata.createCell(4).setCellValue("已过期");
                    } else {
                        rowdata.createCell(4).setCellValue("未过期");
                    }
                    if (cd.getIsSell() == Constant.CARDSELL) { //销售状态
                        rowdata.createCell(5).setCellValue("已销售");
                        rowdata.createCell(6).setCellValue(DateUtil.formatDate(cd.getSellTime(), DateUtil.FORMAT_DATETIME));
                    } else {
                        rowdata.createCell(5).setCellValue("未销售");
                        rowdata.createCell(6).setCellValue("--");
                    }

                    if (cd.getIsFreeze() == Constant.CARDFREEZE) { //冻结状态
                        rowdata.createCell(7).setCellValue("已冻结");
                    } else {
                        rowdata.createCell(7).setCellValue("未冻结");
                    }

                    if (cd.getState() == Constant.CARDACTIVE) { //激活状态
                        rowdata.createCell(8).setCellValue("已激活");
                        rowdata.createCell(9).setCellValue(DateUtil.formatDate(cd.getActiveTime(), DateUtil.FORMAT_DATETIME));
                    } else {
                        rowdata.createCell(8).setCellValue("未激活");
                        rowdata.createCell(9).setCellValue("--");
                    }
                    rowNum++;
                }
                String writeName = "礼品统计表-" + DateUtil.formatDate(new Date(), "yyyyMMddHHmmss");
                PoiExcelUtil.createFile(exportPath, writeName, wb);  //在服务器端写文件
                boolean suc = PoiExcelUtil.download(request, response, "xls", exportPath + writeName, writeName + ".xls");  //在服务器端下载指定文件，完成后删除该文件
                if (!suc) {
                    StageHelper.dwzFailForward("导出" + writeName + "失败，请联系技术部门", "143", "/oms/order/list", response);
                }
            }
        } catch (IceServiceRuntimeException e) {
            logger.error(e, "导出统计表失败,card:" + card.toString(), WebUtils.getIpAddr(request));
            StageHelper.writeDwzSignal("300", "操作失败，请联系管理员", "10251", StageHelper.DWZ_CLOSE_CURRENT, "/card/card/list", response);
        } catch (Exception e) {
            logger.error(e, "导出统计表失败,card:" + card.toString(), WebUtils.getIpAddr(request));
            StageHelper.writeDwzSignal("300", "操作失败，请联系管理员", "10251", StageHelper.DWZ_CLOSE_CURRENT, "/card/card/list", response);
        }
    }

    /**
     * 导出礼品卡
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/export")
    public void export(HttpServletRequest request, HttpServletResponse response) {
        Card card = getCard(request);
        int isFreeze = ServletRequestUtils.getIntParameter(request, "isFreeze", 99);
        int isSell = ServletRequestUtils.getIntParameter(request, "isSell", 99);
        int state = ServletRequestUtils.getIntParameter(request, "state", 99);
        int cardType = ServletRequestUtils.getIntParameter(request, "cardType", 99);
        int isDel = ServletRequestUtils.getIntParameter(request, "isDel", 99);

        if (isFreeze != 99) {
            card.setIsFreeze(isFreeze);
        }
        if (isSell != 99) {
            card.setIsSell(isSell);
        }
        if (state != 99) {
            card.setState(state);
        }
        if (cardType != 99) {
            card.setCardType(cardType);
        }
        if (isDel != 99) {
            card.setIsDel(isDel);
        }

        String[] ids = StageHelper.getOptSelectSaveState(request, "cardExport").toArray(new String[0]);
        try {
            List<Card> list = CardClient.getCardListByCard(card);
            if (list != null && list.size() > 0) {
                if (list.size() > 100000){
                    StageHelper.writeDwzSignal("300", "导出数量大于100000条，请重新赛选", "10251", StageHelper.DWZ_CLOSE_CURRENT, "/card/card/list", response);
                }
                String sheet1Name = "礼品卡表";
                // 创建一个EXCEL
                HSSFWorkbook wb = new HSSFWorkbook();
                // 创建一个SHEET
                HSSFSheet sheet1 = wb.createSheet(sheet1Name);
                String[] titles = {"礼品卡卡号", "礼品卡类型", "礼品卡名称", "面额(￥)"};
                PoiExcelUtil.buildTitles(sheet1, titles);
                int rowNum = 1;//新行号
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    Card cd = list.get(i);
                    //排除已销售的卡
                    if(cd.getIsSell() == Constant.CARDSELL){
                        continue;
                    }
                    //排除已作废的卡
                    if (cd.getIsDel() == Constant.CARDDEL){
                        continue;
                    }
                    HSSFRow rowdata = sheet1.createRow(rowNum);
                    // 下面是填充数据
                    rowdata.createCell(0).setCellValue(cd.getCardId());// 礼品卡卡号
                    if (cd.getCardType() == Constant.EPCARD) {
                        rowdata.createCell(1).setCellValue("电子卡");//礼品卡类型
                    } else {
                        rowdata.createCell(1).setCellValue("实体卡");//礼品卡类型
                    }
                    rowdata.createCell(2).setCellValue(cd.getCardName());// 礼品卡名称
                    rowdata.createCell(3).setCellValue(cd.getCardPrice().toString());// 面额
                    rowNum++;
                }
                String writeName = "礼品卡-" + DateUtil.formatDate(new Date(), "yyyyMMddHHmmss");
                PoiExcelUtil.createFile(exportPath, writeName, wb);  //在服务器端写文件
                PoiExcelUtil.download(request, response, "xls", exportPath + writeName, writeName + ".xls");  //在服务器端下载指定文件，完成后删除该文件
            }
        } catch (IceServiceRuntimeException e) {
            logger.error(e, "导出统计表失败,card:" + ids.toString(), WebUtils.getIpAddr(request));
            StageHelper.writeDwzSignal("300", "操作失败，请联系管理员", "10251", StageHelper.DWZ_CLOSE_CURRENT, "/card/card/list", response);
        } catch (Exception e) {
            logger.error(e, "导出统计表失败,card:" + ids.toString(), WebUtils.getIpAddr(request));
            StageHelper.writeDwzSignal("300", "操作失败，请联系管理员", "10251", StageHelper.DWZ_CLOSE_CURRENT, "/card/card/list", response);
        }
    }


    @RequestMapping(value = "/index")
    public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", 0);
        if (isHandle <= 0) {
            return "/card/card/import_card";
        } else if (isHandle == 1) { //
            String batchId = ServletRequestUtils.getStringParameter(request, "batchNo", "");
            CardImportResult result = new CardImportResult();
            result.setBatchId(batchId);
            List<CardImportResult> results = CardClient.getImportResultListByResult(result);
            if (results != null && results.size() > 0) {
                CardImportLog log = new CardImportLog();
                log.setBatchId(batchId);
                List<CardImportLog> logs = CardClient.getImportLogsByLog(log);
                model.addAttribute("logs",logs);
            }
            model.addAttribute("result",results);
            return "/card/card/import_detail";
        } else {//下载文件
            String url = request.getSession().getServletContext().getRealPath("/");
            try {
                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;
                String path = url + File.separator + "WEB-INF" + File.separator + "views" + File.separator + "card" + File.separator + "card" + File.separator + "card-20140102-1.xls";
                File file = new File(path);
                if (file.isFile() && file.exists()) {
                    long fileLength = file.length();
                    response.setContentType("xls");
                    response.setHeader("Content-disposition", "attachment; filename="
                            + new String("card-20140102-1.xls".getBytes("utf-8"), "ISO8859-1"));
                    response.setHeader("Content-Length", String.valueOf(fileLength));
                    bis = new BufferedInputStream(new FileInputStream(path));
                    bos = new BufferedOutputStream(response.getOutputStream());
                    byte[] buff = new byte[2048];
                    int bytesRead;
                    while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                        bos.write(buff, 0, bytesRead);
                    }
                    bis.close();
                    bos.close();
                }
            } catch (Exception e) {
                Map map = new HashMap();
                map.put("reason", " 文件下载出错！");
                logger.error(e, map, WebUtils.getIpAddr(request));
            }
            return null;
        }
    }

    /**
     * 导入购买者信息
     *
     * @param request
     * @param response
     * @param file
     * @return
     */
    @RequestMapping(value = "/import")
    public String cardImport(HttpServletRequest request, HttpServletResponse response, @RequestParam("file") MultipartFile file) {
        try {
            CommonsMultipartFile multipartFile = (CommonsMultipartFile) file;
            String fileName = multipartFile.getFileItem().getName();
            //判断是否已经导入过
            List<CardImportResult> results = CardClient.getImportResultByBatchId(fileName.split("\\.")[0]);
            if (results != null && results.size() > 0) {
                return StageHelper.writeDwzSignal("300", "该批次已经导入，不能重复操作", "edbe81f7292b53cbc97d4c7afeb2893b", StageHelper.DWZ_FORWARD, "/card/card/index", response);
            } else {
                List<Card> list = getImportCards(request, file);
                if (list == null) {
                    return StageHelper.writeDwzSignal("300", "导入失败，请联系管理员", "edbe81f7292b53cbc97d4c7afeb2893b", StageHelper.DWZ_FORWARD, "/card/card/index", response);
                } else {
                    CardParmsDTO dto = new CardParmsDTO();
                    dto.setUserId(StageHelper.getLoginUser(request).getBksAdmin().getAdminId());
                    dto.setUserName(StageHelper.getLoginUser(request).getBksAdmin().getAdminName());
                    dto.setBatchId(fileName);
                    boolean suc = CardClient.importCardByBuyer(list, dto);
                    if (suc) {
                        return StageHelper.writeDwzSignal("200", "导入数据成功", "bee1420ef18b0f62780bee3005712849", StageHelper.DWZ_CLOSE_CURRENT, "card/card/import-list?batchNo=" + fileName.split("\\.")[0], response);
                    } else {
                        return StageHelper.writeDwzSignal("300", "导入失败，请联系管理员", "edbe81f7292b53cbc97d4c7afeb2893b", StageHelper.DWZ_FORWARD, "/card/card/index", response);
                    }
                }
            }
        } catch (IceServiceRuntimeException e) {
            logger.error(e, "导入购买这信息失败", WebUtils.getIpAddr(request));
            return StageHelper.writeDwzSignal("300", "导入失败，请联系管理员", "edbe81f7292b53cbc97d4c7afeb2893b", StageHelper.DWZ_FORWARD, "/card/card/index", response);
        } catch (Exception e) {
            logger.error(e, "导入购买这信息失败", WebUtils.getIpAddr(request));
            return StageHelper.writeDwzSignal("300", "导入失败，请联系管理员", "edbe81f7292b53cbc97d4c7afeb2893b", StageHelper.DWZ_FORWARD, "/card/card/index", response);
        }

    }

    /**
     * 查看导入记录
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/import-list")
    public String importList(HttpServletRequest request, Model model) {
        CardImportResult result = new CardImportResult();
        //获取批次号
        String batchNo = ServletRequestUtils.getStringParameter(request, "batchNo", null);
        if (!StringUtils.isEmpty(batchNo)) {
            result.setBatchId(batchNo);
        }

        //获取两个时间
        String createTimeMin = ServletRequestUtils.getStringParameter(request, "createTimeMin", null);
        String createTimeMax = ServletRequestUtils.getStringParameter(request, "createTimeMax", null);
        if (StringUtils.isNotEmpty(createTimeMin) && StringUtils.isNotEmpty(createTimeMax)) {
            result.setCreateTimeMin(DateUtil.parseTimestamp(createTimeMin));
            result.setCreateTimeMax(DateUtil.parseTimestamp(createTimeMax));
        }
        PageInfo pageInfo = StageHelper.getPageInfo(request, "create_time", "desc");
        PagerControl<CardImportResult> pc = CardClient.getImportResultByPager(result, pageInfo, pageInfo.getOrderField(), pageInfo.getOrderDirection());

        model.addAttribute("result", result);
        model.addAttribute("pc", pc);
        return "/card/card/import_list";
    }

    private Card getCard(HttpServletRequest request) {
        Card card = new Card();
        String batchId = ServletRequestUtils.getStringParameter(request, "batchId", null);
        String userName = ServletRequestUtils.getStringParameter(request, "userName", null);
        String buyerPhone = ServletRequestUtils.getStringParameter(request, "buyerPhone", "");
        String buyerEmail = ServletRequestUtils.getStringParameter(request, "buyerEmail", "");
        String cardId = ServletRequestUtils.getStringParameter(request, "cardId", null);
        String sellerName = ServletRequestUtils.getStringParameter(request, "sellerName", null);
        String cardPrice = ServletRequestUtils.getStringParameter(request, "cardPrice", null);
        String createTimeMin = ServletRequestUtils.getStringParameter(request, "createTimeMin", null);
        String createTimeMax = ServletRequestUtils.getStringParameter(request, "createTimeMax", null);
        String endTimeMin = ServletRequestUtils.getStringParameter(request, "endTimeMin", null);
        String endTimeMax = ServletRequestUtils.getStringParameter(request, "endTimeMax", null);
        String activeTimeMin = ServletRequestUtils.getStringParameter(request, "activeTimeMin", null);
        String activeTimeMax = ServletRequestUtils.getStringParameter(request, "activeTimeMax", null);

        if (StringUtils.isNotEmpty(batchId)) {
            card.setBatchId(batchId);
        }
        if (StringUtils.isNotEmpty(userName)) {
            card.setUserName(userName);
        }
        if (StringUtils.isNotEmpty(cardId)) {
            card.setCardId(cardId);
        }
        if (StringUtils.isNotEmpty(sellerName)) {
            card.setSellerName(sellerName);
        }
        if (StringUtils.isNotEmpty(cardPrice)) {
            card.setCardPrice(new BigDecimal(cardPrice));
        }
        if (StringUtils.isNotEmpty(createTimeMin) && StringUtils.isNotEmpty(createTimeMax)) {
            card.setCreateTimeMin(DateUtil.parseTimestamp(createTimeMin));
            card.setCreateTimeMax(DateUtil.parseTimestamp(createTimeMax));
        }
        if (StringUtils.isNotEmpty(endTimeMin) && StringUtils.isNotEmpty(endTimeMax)) {
            card.setEndTimeMin(DateUtil.parseTimestamp(endTimeMin));
            card.setEndTimeMax(DateUtil.parseTimestamp(endTimeMax));
        }
        if (StringUtils.isNotEmpty(activeTimeMin) && StringUtils.isNotEmpty(activeTimeMax)) {
            card.setActiveTimeMin(DateUtil.parseTimestamp(activeTimeMin));
            card.setActiveTimeMax(DateUtil.parseTimestamp(activeTimeMax));
        }
        if (StringUtils.isNotEmpty(buyerPhone.trim())){
            card.setBuyerPhone(buyerPhone.trim());
        }
        if (StringUtils.isNotEmpty(buyerEmail.trim())){
            card.setBuyerEmail(buyerEmail.trim());
        }
        return card;
    }

    private List<Card> getImportCards(HttpServletRequest request, MultipartFile file) {
        InputStream is = null;
        List<Card> list = new ArrayList<Card>();
        try {
            is = file.getInputStream();
        } catch (IOException e) {
            logger.error(new BaseException("导入礼品卡购买者信息，获取数据流失败", e), "", WebUtils.getIpAddr(request));
            return list;
        }
        HSSFWorkbook hssfWorkbook = null;
        try {
            hssfWorkbook = new HSSFWorkbook(is);
        } catch (IOException e) {
            logger.error(new BaseException("导入礼品卡购买者信息,Excel转换出错", e), "", WebUtils.getIpAddr(request));
            return list;
        }
        Card xlsDto = null;
        // 循环工作表Sheet
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // 循环行Row
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow == null) {
                    continue;
                }
                xlsDto = new Card();
                // 循环列Cell
                // 礼品卡卡号
                HSSFCell cardId = hssfRow.getCell(0);
                if (cardId == null || "".equals(getValue(cardId))) {
                    continue;
                }
                xlsDto.setCardId(getValue(cardId));

                //礼品卡类型
                if (StringUtils.substring(xlsDto.getCardId(), 0, 2).equals("EP")) {
                    xlsDto.setCardType(Constant.EPCARD);
                } else {
                    xlsDto.setCardType(Constant.LPCARD);
                }

                //获取邮件或者手机
                HSSFCell emph = hssfRow.getCell(4);
                if (emph != null && StringUtils.isNotEmpty(getValue(emph))) {
                    String ep = getValue(emph);
                    //判断是邮件还是手机
                    if (RegexUtil.isEmail(ep)) {
                        xlsDto.setBuyerEmail(ep);
                    } else {
                        xlsDto.setBuyerPhone(ep);
                    }
                }

                //获取销售人
                HSSFCell sellerName = hssfRow.getCell(5);
                if (sellerName != null && StringUtils.isNotEmpty(getValue(sellerName))) {
                    xlsDto.setSellerName(getValue(sellerName));
                }
                list.add(xlsDto);
            }
        }
        return list;
    }

    @SuppressWarnings("static-access")
    private static String getValue(HSSFCell hssfCell) {
        if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
            // 返回布尔类型的值
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
            // 返回数值类型的值
            return String.valueOf(String.format("%.0f", hssfCell.getNumericCellValue()));
        } else {
            // 返回字符串类型的值
            return String.valueOf(hssfCell.getStringCellValue().trim());
        }
    }
}
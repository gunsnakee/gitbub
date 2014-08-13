package com.meiliwan.emall.bkstage.web.controller.pms;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.exception.IceServiceRuntimeException;
import com.meiliwan.emall.stock.bean.StockImportLog;
import com.meiliwan.emall.stock.bean.StockImportResult;
import com.meiliwan.emall.stock.client.ProStockClient;

/**
 * 商品相关库存操作
 * User: wuzixin
 * Date: 13-12-2
 * Time: 下午4:35
 */
@Controller("proStockController")
@RequestMapping("/pms/stock")
public class ProStockController {
    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());

    @RequestMapping("/index")
    public String importIndex(HttpServletRequest request, HttpServletResponse response, Model model) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", 0);
        if (isHandle <= 0) {
            return "/pms/stock/import_stock";
        } else if (isHandle == 1) { //查看导入明细
            //获取批次号
            String batchNo = ServletRequestUtils.getStringParameter(request, "batchNo", null);
            StockImportResult result = ProStockClient.getResultByBatchNo(batchNo);
            List<StockImportLog> logs = null;
            if (result != null) {
                StockImportLog log = new StockImportLog();
                log.setBatchNo(batchNo);
                logs = ProStockClient.getImportLogsByEntity(log);
            }
            model.addAttribute("result", result);
            model.addAttribute("list", logs);
            return "/pms/stock/detail";
        } else {//下载文件
            String url = request.getSession().getServletContext().getRealPath("/");
            try {
                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;
                String path = url + File.separator + "WEB-INF" + File.separator + "views" + File.separator + "pms" + File.separator + "stock" + File.separator + "stock_import_template.xls";
                File file = new File(path);
                if (file.isFile() && file.exists()) {
                    long fileLength = file.length();
                    response.setContentType("xls");
                    response.setHeader("Content-disposition", "attachment; filename="
                            + new String("stock_import_template.xls".getBytes("utf-8"), "ISO8859-1"));
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

    @RequestMapping(value = "/import")
    public String importStock(HttpServletRequest request, HttpServletResponse response, @RequestParam("file") MultipartFile file) {
        Map<String, Object> map = getImportStocks(request, file);
        int isRight = (Integer) map.get("isRight");
        List<StockImportLog> list = (List<StockImportLog>) map.get("list");
        try {
            if (isRight == 1) {
                return StageHelper.writeDwzSignal("300", "导入库存文件数据格式不正确", "e0666dde1efa3d4724831ae1776f4857", StageHelper.DWZ_FORWARD, "/pms/stock/index", response);
            }
            CommonsMultipartFile multipartFile = (CommonsMultipartFile) file;
            String fileName = multipartFile.getFileItem().getName();
            if (isChinese(fileName)) {
                return StageHelper.writeDwzSignal("300", "导入库存文件名称存在中文", "e0666dde1efa3d4724831ae1776f4857", StageHelper.DWZ_FORWARD, "/pms/stock/index", response);
            }
            if (list != null && list.size() > 0) {
                List<String> valids = validateData(list);
                if (valids != null && valids.size() > 0) {
                    logger.warn("导入商品库存，数据不完整，出现重复数据", valids, WebUtils.getIpAddr(request));
                    return StageHelper.writeDwzSignal("300", "本次导入数据失败,商品数据有问题,对应条形码:" + valids.toString(), "e0666dde1efa3d4724831ae1776f4857", StageHelper.DWZ_FORWARD, "/pms/stock/index", response);
                } else {
                    StockImportResult result = ProStockClient.getResultByBatchNo(fileName.split("\\.")[0]);
                    if (result != null) {
                        return StageHelper.writeDwzSignal("300", "该批次已经导入，不能重复操作", "e0666dde1efa3d4724831ae1776f4857", StageHelper.DWZ_FORWARD, "/pms/stock/index", response);
                    } else {
                        ProStockClient.importExcel(list, fileName, StageHelper.getLoginUser(request).getBksAdmin().getAdminId(), StageHelper.getLoginUser(request).getBksAdmin().getAdminName());
                        return StageHelper.writeDwzSignal("200", "导入数据成功", "ab83fcfc18ebc765ff35593c40e730c2", StageHelper.DWZ_CLOSE_CURRENT, "/pms/stock/list", response);
                    }
                }
            }else {
                return StageHelper.writeDwzSignal("300", "本次导入数据失败,商品数据有问题,导入文件为空或者库存字段数据为空" + list, "e0666dde1efa3d4724831ae1776f4857", StageHelper.DWZ_FORWARD, "/pms/stock/index", response);
            }

        } catch (IceServiceRuntimeException e) {
            logger.error(e, list, WebUtils.getIpAddr(request));
            return StageHelper.writeDwzSignal("300", "操作失败请联系管理员", "e0666dde1efa3d4724831ae1776f4857", StageHelper.DWZ_FORWARD, "/pms/stock/index", response);
        } catch (Exception e) {
            logger.error(e, list, WebUtils.getIpAddr(request));
            return StageHelper.writeDwzSignal("300", "操作失败请联系管理员", "e0666dde1efa3d4724831ae1776f4857", StageHelper.DWZ_FORWARD, "/pms/stock/index", response);
        }
    }

    /**
     * 查看导入明细
     */
    @RequestMapping(value = "/list")
    public String list(HttpServletRequest request, Model model) {
        StockImportResult bean = new StockImportResult();
        //获取批次号
        String batchNo = ServletRequestUtils.getStringParameter(request, "batchNo", null);
        if (!StringUtils.isEmpty(batchNo)) {
            bean.setBatchNo(batchNo);
        }
        //获取两个时间
        String updateTimeMin = ServletRequestUtils.getStringParameter(request, "updateTimeMin", null);
        if (!StringUtils.isEmpty(updateTimeMin)) {
            bean.setUpdateTimeMin(DateUtil.parseTimestamp(updateTimeMin));
        }

        String updateTimeMax = ServletRequestUtils.getStringParameter(request, "updateTimeMax", "");
        if (!StringUtils.isEmpty(updateTimeMax)) {
            bean.setUpdateTimeMax(DateUtil.parseTimestamp(updateTimeMax));
        }

        PagerControl<StockImportResult> pc = ProStockClient.getPageImportResult(bean, StageHelper.getPageInfo(request));
        model.addAttribute("bean", bean);
        model.addAttribute("pc", pc);
        return "/pms/stock/list";
    }

    private Map<String, Object> getImportStocks(HttpServletRequest request, MultipartFile file) {
        Map<String, Object> map = new HashMap<String, Object>();
        int isRight = 0;
        InputStream is = null;
        try {
            is = file.getInputStream();
        } catch (IOException e) {
            logger.error(new BaseException("获取导入商品库存表数据流失败", e), "", WebUtils.getIpAddr(request));
        }
        HSSFWorkbook hssfWorkbook = null;
        try {
            hssfWorkbook = new HSSFWorkbook(is);
        } catch (IOException e) {
            logger.error(new BaseException("获取导入商品库存表,Excel转换出错", e), "", WebUtils.getIpAddr(request));
        }
        StockImportLog xlsDto = null;
        List<StockImportLog> list = new ArrayList<StockImportLog>();
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
                xlsDto = new StockImportLog();
                // 循环列Cell
                // 商品ID
                HSSFCell barCode = hssfRow.getCell(0);
                if (barCode == null || "".equals(getValue(barCode))) {
                    continue;
                }
                xlsDto.setBarCode(getValue(barCode));

                //美丽价格
                HSSFCell stock = hssfRow.getCell(1);
                if (stock == null || "".equals(getValue(stock))) {
                    continue;
                }
                try {
                    xlsDto.setChangeStock(Integer.valueOf(getValue(stock)));
                } catch (Exception e) {
                    logger.warn("导入商品库存-库存字段格式不对", "barCode:" + barCode, "");
                    isRight = 1;
                }
                list.add(xlsDto);
            }
        }
        map.put("isRight", isRight);
        map.put("list", list);
        return map;
    }

    private List<String> validateData(List<StockImportLog> logs) {
        Map<String, String> map = new HashMap<String, String>();
        List<String> strings = new ArrayList<String>();
        for (StockImportLog log : logs) {
            if (map.get(log.getBarCode()) != null) {
                strings.add(log.getBarCode());
            } else {
                map.put(log.getBarCode(), log.getBarCode());
            }
        }
        return strings;
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


    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    private static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        boolean suc = false;
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c) == true) {
                suc = true;
                break;
            }
        }
        return suc;
    }
}

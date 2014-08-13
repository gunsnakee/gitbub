package com.meiliwan.emall.bkstage.web.controller.pms;

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

import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.exception.IceServiceRuntimeException;
import com.meiliwan.emall.pms.bean.ProLocation;
import com.meiliwan.emall.pms.client.ProLocationClient;

/**
 * 商品存储位置管理
 * User: wuzixin
 * Date: 13-11-15
 * Time: 下午5:44
 */
@Controller("proLocationController")
@RequestMapping("/pms/location")
public class ProLocationController {
    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/list")
    public String list(HttpServletRequest request, Model model) {
        String barCode = ServletRequestUtils.getStringParameter(request, "barCode", null);
        String locationName = ServletRequestUtils.getStringParameter(request, "locationName", null);
        String proName = ServletRequestUtils.getStringParameter(request, "proName", null);

        ProLocation location = new ProLocation();
        if (!StringUtils.isEmpty(barCode)) {
            location.setBarCode(barCode);
        }
        if (!StringUtils.isEmpty(locationName)) {
            location.setLocationName(locationName);
        }
        if (!StringUtils.isEmpty(proName)) {
            location.setProName(proName);
        }
        PageInfo pageInfo = StageHelper.getPageInfo(request, "update_time", "desc");

        PagerControl<ProLocation> pc = ProLocationClient.getPageByObj(location, pageInfo, pageInfo.getOrderField(), pageInfo.getOrderDirection());
        model.addAttribute("location", location);
        model.addAttribute("pc", pc);
        return "/pms/location/list";
    }

    @RequestMapping(value = "/update")
    public String update(HttpServletRequest request, HttpServletResponse response, Model model) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        int locationId = ServletRequestUtils.getIntParameter(request, "locationId", 0);
        if (isHandle > 0) {
            try {
                ProLocation location = new ProLocation();
                String locationName = ServletRequestUtils.getStringParameter(request, "locationName", null);
                String barCode = ServletRequestUtils.getStringParameter(request, "barCode", null);
                location.setLocationId(locationId);
                location.setLocationName(locationName);
                boolean suc = ProLocationClient.updateProLocation(location);
                if (suc) {
                    return StageHelper.writeDwzSignal("200", "修改成功", "10214", StageHelper.DWZ_CLOSE_CURRENT, "/pms/location/list?barCode=" + barCode, response);
                } else {
                    return StageHelper.writeDwzSignal("300", "操作失败请联系管理员", "10214", StageHelper.DWZ_FORWARD, "/pms/location/update", response);
                }
            } catch (IceServiceRuntimeException e) {
                logger.error(e, "isHandle:" + isHandle + ",proId:" + locationId, WebUtils.getIpAddr(request));
                return StageHelper.writeDwzSignal("300", "操作失败请联系管理员", "10214", StageHelper.DWZ_FORWARD, "/pms/location/update", response);
            } catch (Exception e) {
                logger.error(e, "isHandle:" + isHandle + ",proId:" + locationId, WebUtils.getIpAddr(request));
                return StageHelper.writeDwzSignal("300", "操作失败请联系管理员", "10214", StageHelper.DWZ_FORWARD, "/pms/location/update", response);
            }
        }
        ProLocation location = ProLocationClient.getLocationById(locationId);
        model.addAttribute("location", location);
        model.addAttribute("locationId", locationId);
        return "/pms/location/update";
    }

    @RequestMapping(value = "/export")
    public String export(HttpServletRequest request, HttpServletResponse response, @RequestParam("file") MultipartFile file) {
        List<ProLocation> list = getListLocation(request, file);
        try {
            List<String> valids = validateData(list);
            if (valids != null && valids.size() > 0) {
                logger.warn("导入商品储位，数据不完整，出现重复数据",valids,WebUtils.getIpAddr(request));
                return StageHelper.writeDwzSignal("300", "本次导入数据失败,储位数据有问题,对应条形码:" + valids.toString(), "47c72c8314a1c36c462f89de78409bca", StageHelper.DWZ_FORWARD, "/pms/location/list", response);
            } else {
                ProLocationClient.updateLocationByBarcode(list);
                return StageHelper.writeDwzSignal("200", "导入数据成功", "47c72c8314a1c36c462f89de78409bca", StageHelper.DWZ_FORWARD, "/pms/location/list", response);
            }
        } catch (IceServiceRuntimeException e) {
            logger.error(e, list, WebUtils.getIpAddr(request));
            return StageHelper.writeDwzSignal("300", "操作失败请联系管理员", "47c72c8314a1c36c462f89de78409bca", StageHelper.DWZ_FORWARD, "/pms/location/list", response);
        } catch (Exception e) {
            logger.error(e, list, WebUtils.getIpAddr(request));
            return StageHelper.writeDwzSignal("300", "操作失败请联系管理员", "47c72c8314a1c36c462f89de78409bca", StageHelper.DWZ_FORWARD, "/pms/location/list", response);
        }
    }

    private List<ProLocation> getListLocation(HttpServletRequest request, MultipartFile file) {
        InputStream is = null;
        try {
            is = file.getInputStream();
        } catch (IOException e) {
            logger.error(new BaseException("获取商品导入储位表数据流失败", e), "", WebUtils.getIpAddr(request));
        }
        HSSFWorkbook hssfWorkbook = null;
        try {
            hssfWorkbook = new HSSFWorkbook(is);
        } catch (IOException e) {
            logger.error(new BaseException("获取商品导入储位表,Excel转换出错", e), "", WebUtils.getIpAddr(request));
        }
        ProLocation xlsDto = null;
        List<ProLocation> list = new ArrayList<ProLocation>();
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
                xlsDto = new ProLocation();
                // 循环列Cell
                // 商品ID
                HSSFCell barCode = hssfRow.getCell(0);
                if (barCode == null || "".equals(getValue(barCode))) {
                    continue;
                }
                xlsDto.setBarCode(getValue(barCode));

                //美丽价格
                HSSFCell locationName = hssfRow.getCell(1);
                if (barCode == null) {
                    continue;
                }
                xlsDto.setLocationName(getValue(locationName));
                list.add(xlsDto);
            }
        }
        return list;
    }

    private List<String> validateData(List<ProLocation> locations) {
        Map<String, String> map = new HashMap<String, String>();
        List<String> strings = new ArrayList<String>();
        for (ProLocation location : locations) {
            if (map.get(location.getBarCode()) != null) {
                strings.add(location.getBarCode());
            } else {
                map.put(location.getBarCode(), location.getBarCode());
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
}

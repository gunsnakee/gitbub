package com.meiliwan.emall.bkstage.web.controller.account;

import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.bkstage.web.controller.account.template.LimitException;
import com.meiliwan.emall.bkstage.web.controller.account.template.MoneyReports;
import com.meiliwan.emall.bkstage.web.controller.account.template.PayReports;
import com.meiliwan.emall.bkstage.web.controller.account.template.RechargerReports;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.web.WebUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;

/**
 * 导出财务报表
 * User: wenlepeng
 * Date: 13-11-26
 * Time: 下午2:56
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/account/export")
public class ExportWalletReports {

    private final static MLWLogger logger = MLWLoggerFactory.getLogger(ExportWalletReports.class);

    @RequestMapping("/port")
    public String exportPort(HttpServletRequest request){
      return "/account/port";
    }

    @RequestMapping("/getrechargerexcel")
    public void getRechargerExcel(HttpServletRequest request,HttpServletResponse response){
        String beginDate = ServletRequestUtils.getStringParameter(request, "recharge_start", null);
        String endDate = ServletRequestUtils.getStringParameter(request,"recharge_end",null);
        String childType = ServletRequestUtils.getStringParameter(request,"rechargeType",null);

        if(beginDate == null || endDate == null || "".equals(beginDate) || "".equals(endDate)){
            StageHelper.dwzFailForward("请选择起始时间和终止时间!", "143", "/account/export/port", response);
            return;
        }

        String[] titles = {"流水号","充值时间","用户名","充值方式","金额","昵称","邮箱","手机"};
        RechargerReports rechargerReports = new RechargerReports("充值记录明细表",titles,"_recharge.xls");
        if(childType != null && childType.equals("ADD_MONEY_HB")){
            rechargerReports.setHB(true);
        }

        try {
            rechargerReports.export(beginDate+" 00:00:00",endDate+" 23:59:59");
            download(request, response, "xls", rechargerReports.getFilePath(), "充值流水表.xls");
        }catch (LimitException e){
            logger.error(e, "导出的总记录条数超过5万条", WebUtils.getIpAddr(request));
            StageHelper.dwzFailForward("导出的总记录条数超过5万条，请联系技术部门", "143", "/account/export/port", response);
        }catch (Exception e) {
            logger.error(e, "充值流水表导出失败,导出时间form " + beginDate + "to " + endDate, WebUtils.getIpAddr(request));
            StageHelper.dwzFailForward("充值流水表失败，请联系技术部门", "143", "/account/export/port", response);
        }

    }


    @RequestMapping("/getpayexcel")
    public void getPayExcel(HttpServletRequest request,HttpServletResponse response){
        String sheetName = "美丽湾钱包使用明细表";
        String[] title ={"订单号","用户名","昵称","发生时间","使用金额","使用类型","邮箱","手机"};

        String beginDate = ServletRequestUtils.getStringParameter(request,"pay_start",null);
        String endDate = ServletRequestUtils.getStringParameter(request,"pay_end",null);
        if(beginDate == null || endDate == null || "".equals(beginDate) || "".equals(endDate)){
            StageHelper.dwzFailForward("请选择起始时间和终止时间!", "143", "/account/export/port", response);
            return;
        }

        PayReports payReports = new PayReports(sheetName,title,"_payExcel");


        try {
            payReports.export(beginDate+" 00:00:00",endDate+" 23:59:59");
            download(request, response, "xls", payReports.getFilePath(), "钱包使用记录表.xls");
        }catch (LimitException e){
            logger.error(e, "导出的总记录条数超过5万条", WebUtils.getIpAddr(request));
            StageHelper.dwzFailForward("导出的总记录条数超过5万条，请联系技术部门", "143", "/account/export/port", response);
        }catch (Exception e) {
                logger.error(e, "钱包使用记录表导出失败,导出时间form " + beginDate + "to " + endDate, WebUtils.getIpAddr(request));
                StageHelper.dwzFailForward("导出美丽湾钱包使用明细表失败，请联系技术部门", "143", "/account/export/port", response);
        }
    }


    @RequestMapping("/getmoneyexcel")
    public void getMoneyExcel(HttpServletRequest request,HttpServletResponse response){
        String sheetName = "美丽湾余额总览表";
        String[] title ={"用户名","钱包余额","用户状态","昵称","邮箱","手机"};

        MoneyReports moneyReports = new MoneyReports(sheetName,title,"_moneyExcel");


        try {
            moneyReports.export(null,null);
            download(request, response, "xls", moneyReports.getFilePath(), "余额总览表.xls");
        }catch (LimitException e){
            logger.error(e, "导出的总记录条数超过5万条", WebUtils.getIpAddr(request));
            StageHelper.dwzFailForward("导出的总记录条数超过5万条，请联系技术部门", "143", "/account/export/port", response);
        }
        catch (Exception e) {
            logger.error(e, "余额总览表导出失败", WebUtils.getIpAddr(request));
            StageHelper.dwzFailForward("导出余额总览表失败，请联系技术部门", "143", "/account/export/port", response);
        }
    }


    /**
     * 文件下载
     *
     * @param request
     * @param response
     * @param contentType
     * @param path
     * @param fileName
     * @throws Exception
     */
    public static void download(HttpServletRequest request,
                                HttpServletResponse response, String contentType,
                                String path, String fileName) throws Exception {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        long fileLength = new File(path).length();
        response.setContentType(contentType);
        response.setHeader("Content-disposition", "attachment; filename="
                + new String(fileName.getBytes("utf-8"), "ISO8859-1"));
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

}

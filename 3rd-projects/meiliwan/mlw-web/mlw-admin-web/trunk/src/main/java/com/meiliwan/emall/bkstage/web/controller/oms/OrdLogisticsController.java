package com.meiliwan.emall.bkstage.web.controller.oms;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.util.StringUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.oms.bean.OrdLogistics;
import com.meiliwan.emall.oms.client.OrdLogisticsClient;
import com.meiliwan.emall.oms.constant.TransportCompany;

@Controller
@RequestMapping("/oms/logistics")
public class OrdLogisticsController {

	private static final MLWLogger logger = MLWLoggerFactory
			.getLogger(ExceptionController.class);

	/**
	 * 订单货运ID
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/ordLogisticslist")
	public String list(HttpServletRequest request, 
			HttpServletResponse response,Model model) {
		OrdLogistics bean =null;
		try{
			bean = setSearchParams(request);
			model.addAttribute("search", bean);
			String excel = ServletRequestUtils.getStringParameter(request,
					"excel", "");
			if(!StringUtil.checkNull(excel)){
				List<OrdLogistics> list = OrdLogisticsClient.getOrderLogisticsNumberList(bean);
				genExcel(list,request,response);
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e, bean, WebUtils.getIpAddr(request));
			return StageHelper.dwzFailForward("操作失败请联系管理员", "",
					"", response);
		}
		return "/oms/logistics/ordLogisticslist";
	}
	
	/**
	 * 生成excel文件
	 */
	private void genExcel(List<OrdLogistics> list,HttpServletRequest request, 
			HttpServletResponse response){
		try {
            //生成表格开始
            // 创建一个EXCEL
            HSSFWorkbook wb = new HSSFWorkbook();
            // 创建一个SHEET
            HSSFSheet sheet1 = wb.createSheet("订单货运单号");
            String[] title = { "序号", "订单", "物流公司","货运单号","创建时间"};
            
            int rowNum=0;
            // 创建一行
            HSSFRow row = sheet1.createRow(rowNum);
            rowNum++;
            // 填充标题
            for (int i = 0; i < title.length; i++) {
				HSSFCell cell = row.createCell(i);
	            cell.setCellValue(title[i]);
			}
            
            for(int j =0 ; j < list.size() ; j ++){
                    HSSFRow rowdata = sheet1.createRow(rowNum);
                    rowdata.createCell(0).setCellValue(rowNum);
                    rowNum++;
                    // 下面是填充数据
                    rowdata.createCell(1).setCellValue(list.get(j).getOrderId());
                    
                    String company="";
                    if(list.get(j).getLogisticsCompany().equals(TransportCompany.SF.name())||
                    		list.get(j).getLogisticsCompany().equals(TransportCompany.EMS.name())){
    						TransportCompany tc = TransportCompany.valueOf(list.get(j).getLogisticsCompany());
    						company=tc.getDesc();
	    				}else{
	    					company=list.get(j).getLogisticsCompany();
	    			    }
                    
                    rowdata.createCell(2).setCellValue(company);
                    rowdata.createCell(3).setCellValue(list.get(j).getLogisticsNumber());
                    rowdata.createCell(4).setCellValue(DateUtil.getDateTimeStr(list.get(j).getUpdateTime()));
            }

          //获取程序当前路径
            String strDir = System.getProperty("user.dir");
            //将路径分隔符更换
            String folderpath = strDir.replace('/',File.separatorChar);
            File file = new File(folderpath);
            // Create temp file
            File temp = File.createTempFile("josp", ".xls", file);
            System.out.println(file.getAbsolutePath());
            // Delete temp file when program exits.
            temp.deleteOnExit();
            
            FileOutputStream fos = new FileOutputStream(temp);
            wb.write(fos);
            
            fos.close();
            String fileId = DateUtil.formatDate(new Date(), "yyyyMMddHHmmss");
            download(request,response,temp,"货运单号"+fileId+".xls");
        }catch (Exception e) {
        		logger.error(e, list,WebUtils.getIpAddr(request));
            StageHelper.dwzFailForward("导出订单失败，请联系技术部门", "", "", response);
        }
	}
	private OrdLogistics setSearchParams(HttpServletRequest request) {
		OrdLogistics bean = new OrdLogistics();
		String updateTimeMin = ServletRequestUtils.getStringParameter(request,
				"updateTimeMin", "");
		if (!StringUtil.checkNull(updateTimeMin)) {
			Date min = DateUtil.parseDateTime(updateTimeMin);
			bean.setUpdateTime(min);
		}
		return bean;
	}
	
	/**
     * 文件下载
     * @param request
     * @param response
     * @param fileName
     * @throws Exception
     */
      public static void download(HttpServletRequest request,
                                                HttpServletResponse response,
                                                File file,String fileName) throws Exception {
        response.setContentType("text/html;charset=UTF-8");
               request.setCharacterEncoding("UTF-8");

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        long fileLength=file.length();
        response.setContentType("xls");
        response.setHeader("Content-disposition", "attachment; filename="
                     + new String(fileName.getBytes("utf-8"), "ISO8859-1"));
        response.setHeader("Content-Length", fileLength+"");

        bis = new BufferedInputStream(new FileInputStream(file));
        bos = new BufferedOutputStream(response.getOutputStream());
        byte[] buff = new byte[2048];
        int bytesRead=-1;
        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
              bos.write(buff, 0, bytesRead);
        }
        bis.close();
        bos.close();
    }
}

package com.meiliwan.emall.bkstage.web.controller.mms;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.meiliwan.emall.base.bean.BaseSms;
import com.meiliwan.emall.base.bean.BaseSmsItem;
import com.meiliwan.emall.base.client.BaseMailAndMobileClient;
import com.meiliwan.emall.base.client.BaseSmsClient;
import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.bkstage.web.controller.bkstage.view.LoginUser;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.util.RegexUtil;
import com.meiliwan.emall.commons.web.WebUtils;

@Controller("bkstageSmsController")
@RequestMapping("/mms/sms")
public class SmsController {
	private final MLWLogger logger = MLWLoggerFactory.getLogger(SmsController.class);
	
	@RequestMapping("/list")
    public String list(HttpServletRequest request, Model model,HttpServletResponse response) {
		PageInfo pageInfo = StageHelper.getPageInfo(request, "create_time", "desc");
		BaseSms sms = new BaseSms() ;
		String startTime = ServletRequestUtils.getStringParameter(request, "startTime", null);
		if(!StringUtils.isBlank(startTime)){
			sms.setStartTime(DateUtil.parseDateTime(startTime));
		}
        String endTime = ServletRequestUtils.getStringParameter(request, "endTime", null);
        if(!StringUtils.isBlank(endTime)){
        	sms.setEndTime(DateUtil.parseDateTime(endTime));
        }
        
        try{
        	PagerControl<BaseSms> pc = BaseSmsClient.getSmsListByObject(sms, pageInfo);
        	model.addAttribute("startTime", !StringUtils.isBlank(startTime) ? DateUtil.parseDateTime(startTime) : "");
        	model.addAttribute("endTime", !StringUtils.isBlank(endTime) ? DateUtil.parseDateTime(endTime) : "");
        	model.addAttribute("pc", pc);
        	
        }catch (Exception e) {
			logger.error(e, "操作失败", WebUtils.getIpAddr(request)) ;
		}
        
		return "/mms/sms/list" ;
	}
	
	
	@RequestMapping(value = "/tosend")
    public String toSendMsg(Model model, HttpServletRequest request, HttpServletResponse response){
		return "/mms/sms/addSms";
	}
	
	/**
	 * 发送并保存短信方法
	 * @param model
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/send")
    public void sendMsg(Model model, HttpServletRequest request, HttpServletResponse response){
		String mobiles = ServletRequestUtils.getStringParameter(request, "mobiles", null);
		String content = ServletRequestUtils.getStringParameter(request, "content", null);
		String fileName = ServletRequestUtils.getStringParameter(request, "fileName", null);
		
		if(StringUtils.isBlank(content) || StringUtils.isBlank(mobiles)){
			StageHelper.writeDwzSignal("300", "输入有错！", "148", "", "/mms/sms/list", response);
		}
		
		if(!checkMobileStr(mobiles)){
			StageHelper.writeDwzSignal("300", "接收号码有误，请检查！", "148", "", "/mms/sms/list", response);
		}
		
		boolean isUc = false ;
		if(!StringUtils.isBlank(mobiles)){
			LoginUser admin = StageHelper.getLoginUser(request);
			BaseSms sms = new BaseSms();
			BaseSmsItem smsi = new BaseSmsItem() ;
			List<BaseSmsItem> smsiList = new ArrayList<BaseSmsItem>() ;
			if(!StringUtils.isBlank(mobiles) && mobiles.trim().length() == 11){
				smsi = new BaseSmsItem() ;
				smsi.setCreateTime(new Date());
				smsi.setMobile(mobiles.trim());
				//如果单条的话直接在这里发送了
				boolean isSend = BaseMailAndMobileClient.sendMobile(mobiles.trim(), content) ;
				smsi.setStatus(isSend ? 1 : 0);
				smsiList.add(smsi);
				
			}else{
				boolean isSend = false ;
				if(mobiles.split(",").length < 500){
					isSend = BaseMailAndMobileClient.sendMobile(mobiles.trim(), content) ;
				}
				for(String mobile : mobiles.split(",")){
					smsi = new BaseSmsItem() ;
					smsi.setCreateTime(new Date());
					smsi.setMobile(mobile.trim());
					if(isSend){
						smsi.setStatus(1);
					}
					smsiList.add(smsi);
				}
			}
			sms.setAdminId(admin.getBksAdmin().getAdminId());
			sms.setAdminName(admin.getBksAdmin().getAdminName());
			sms.setContent(content);
			sms.setCreateTime(new Date());
			sms.setFileName(fileName);
			sms.setSmsItemList(smsiList);
			isUc = BaseSmsClient.addSms(sms);
		}
		StageHelper.writeDwzSignal(isUc ? "200" : "300", isUc ? "消息发送成功！" : "短信发送失败！", "148", isUc ? StageHelper.DWZ_CLOSE_CURRENT : "", "/mms/sms/list", response);
	}
	
	/**
	 * 实现导出功能
	 * @param model
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/export")
	public void mobileExport(Model model, HttpServletRequest request, HttpServletResponse response){
		String exportPath = "/data/file/sms/";
		String fileName = ServletRequestUtils.getStringParameter(request, "fileName", null);
		if(StringUtils.isBlank(fileName)){
			StageHelper.dwzFailForward("导出" + fileName + "失败，请联系技术部门", "143", "/mms/sms/list", response);
			return ;
		}
		
		try{
			BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
			File file = new File(exportPath+fileName);
			//如果文件夹不存在  
    		if (!file.exists() && !file.isDirectory()) {       
    			StageHelper.dwzFailForward("文件已不存在", "143", "/mms/sms/list", response);
    			return ;
    		}
    		
    		long fileLength = file.length();
            response.setContentType("txt");
            response.setHeader("Content-disposition", "attachment; filename="
                    + new String(fileName.getBytes("utf-8"), "ISO8859-1"));
            response.setHeader("Content-Length", String.valueOf(fileLength));
            bis = new BufferedInputStream(new FileInputStream(exportPath + fileName));
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
            bis.close();
            bos.close();
			
//			PoiExcelUtil.download(request, response, "txt", exportPath + fileName, fileName); 
			
		}catch (Exception e) {
			StageHelper.dwzFailForward("导出" + fileName + "失败，请联系技术部门", "143", "/mms/sms/list", response);
		}
		
	}
	
	/**
	 * 选择批量发送文件后经此方法解析，并返回结果
	 * @param request
	 * @param response
	 * @param file
	 */
	@RequestMapping(value = "/import")
    public void mobileImport(HttpServletRequest request, HttpServletResponse response, @RequestParam("file") MultipartFile file) {
            CommonsMultipartFile multipartFile = (CommonsMultipartFile) file;
            Map<String, String> retultMap = new HashMap<String, String>() ;
            if(multipartFile != null && !multipartFile.isEmpty()){
            	String filename = DateUtil.formatDate(new Date(), "yyyyMMddHHmm") + multipartFile.getOriginalFilename();
            	try{
            		File saveFile1 = new File("/data/file/") ;
            		//如果文件夹不存在则创建     
            		if  (!saveFile1.exists()  && !saveFile1.isDirectory()) {       
            			saveFile1.mkdir(); 
            		}
            		File saveFile2 = new File("/data/file/sms/") ;
            		//如果文件夹不存在则创建     
            		if  (!saveFile2.exists()  && !saveFile2.isDirectory()) {       
            			saveFile2.mkdir(); 
            		}
            		DataOutputStream out = new DataOutputStream(new FileOutputStream("/data/file/sms/" + filename));// 存放文件的绝对路径
            		InputStream is = multipartFile.getInputStream() ;
            		byte[] b = new byte[is.available()] ;
            		is.read(b);
            		out.write(b);
	            	File ffile = new File("/data/file/sms/" + filename) ;
	            	FileReader fr = new FileReader(ffile) ;
	            	int fileLen = (int)ffile.length();
	            	char[] chars = new char[fileLen];
	            	fr.read(chars);         
	            	String txt = String.valueOf(chars); 
	            	
	            	if(checkMobileStr(txt)){
	            		retultMap.put("numbers", txt.trim());
	            		retultMap.put("msg", "导入成功.");
	            		retultMap.put("fileN", filename);
	            		WebUtils.writeJsonByObj(retultMap, request, response);
	            	}else{
	            		retultMap.put("msg", "文件数据有错，请检查.");
	            		WebUtils.writeJsonByObj(retultMap, request, response);
	            	}
	            	
            	}catch (Exception e) {
					logger.error(e, "文件解析失败.", WebUtils.getIpAddr(request));
					try{
						retultMap.put("msg", "文件解析失败.");
	            		WebUtils.writeJsonByObj(retultMap, request, response);
						
					}catch (Exception ee) {
						logger.error(e, "response getWriter write erro .", WebUtils.getIpAddr(request));
					}
				}
            }
	}


	/**
	 * 校验接收号码是否正确
	 * @param txt
	 * @return
	 */
	private boolean checkMobileStr(String txt) {
		//只发送一个号码
		if(!StringUtils.isBlank(txt) && txt.trim().length() == 11){
			return RegexUtil.isPhone(txt.trim()) ? true : false ;
		}
		//批量号码
		if(!StringUtils.isBlank(txt) && txt.trim().length() > 11 && txt.split(",") != null && txt.split(",").length > 0 ){
			String[] str = txt.split(",");
			for(String mobile : str){
				if(!RegexUtil.isPhone(mobile)){
					return false ;
				}
			}
			return true ;
		}
		
		return false;
	}   
	
	/**  
	 * 删除单个文件  
	 * @param   sPath    被删除文件的文件名  
	 * @return 单个文件删除成功返回true，否则返回false  
	 */  
	public boolean deleteFile(String sPath) {   
	    boolean flag = false;   
	    File file = new File(sPath);   
	    // 路径为文件且不为空则进行删除   
	    if (file.isFile() && file.exists()) {   
	        file.delete();   
	        flag = true;   
	   }   
	   return flag;   
	}
	
}

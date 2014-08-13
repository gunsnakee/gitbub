package com.meiliwan.emall.monitor.web.ord;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.bean.JsonResult;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.util.StringUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.monitor.bean.RequestLogVO;
import com.meiliwan.emall.monitor.service.OrderStatisticsService;
import com.meiliwan.emall.monitor.service.StatisticsService;
import com.meiliwan.emall.monitor.service.oms.OMSExcel;
import com.meiliwan.emall.monitor.service.wms.ExcelInterface;
import com.meiliwan.emall.monitor.service.wms.OrdSheet;
import com.meiliwan.emall.monitor.service.wms.OrderExcelService;
import com.meiliwan.emall.monitor.service.wms.WMSBean;
import com.meiliwan.emall.monitor.service.wms.WMSExcel;
import com.meiliwan.emall.monitor.service.wms.WMSProduct;
import com.meiliwan.emall.oms.bean.OrdAddr;
import com.meiliwan.emall.oms.client.OrdClient;
import com.meiliwan.emall.oms.dto.OrdDTO;
import com.meiliwan.emall.oms.dto.OrdDetailDTO;
import com.meiliwan.emall.oms.dto.OrderQueryDTO;
import com.meiliwan.emall.oms.dto.OrdiDTO;

@Controller
@RequestMapping(value = "/statistics")
public class OrderExcelController {

	private final MLWLogger logger = MLWLoggerFactory.getLogger(getClass());

	@Autowired
	private OrderStatisticsService orderStatisticsService;
	@Autowired
	private StatisticsService statisticsService;
	
	
	
	
	@RequestMapping(value = "/orderExcel")
	public String orderCount(HttpServletRequest request, Model model,
			HttpServletResponse response) {
        //获取页面筛选条件数据
      
		return "/statistics/orderExcel";
	}

	
	@RequestMapping(value = "/upload")
	public void upload(MultipartHttpServletRequest request,
			HttpServletResponse response)  {
		String type = ServletRequestUtils.getStringParameter(request, "type","");
		ExcelInterface intr = OrderExcelService.getWMSInterface(type);
		if(intr==null){
			return ;
		}
		try {
			MultiValueMap<String, MultipartFile> map = request.getMultiFileMap();
			for (String key : map.keySet()) {
				
				List<MultipartFile> list = map.get(key);
				for (MultipartFile multipartFile : list) {
					OrdSheet sheet = OrderExcelService.readExcel(multipartFile.getInputStream(),intr);
					HSSFWorkbook workbook = WMSExcel.createExcel(sheet);
					write(response,workbook);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e, null, WebUtils.getIpAddr(request));
			WebUtils.writeJsonByObj(new JsonResult(false), response);
		}
	}
	
	@RequestMapping(value = "/multiUpload")
	public void multiUpload(MultipartHttpServletRequest request,
			HttpServletResponse response)  {
		String type = ServletRequestUtils.getStringParameter(request, "type","");
		ExcelInterface intr = OrderExcelService.getWMSInterface(type);
		if(intr==null){
			return ;
		}
		try {
			List<OrdSheet> sheets = new ArrayList<OrdSheet>();
			
			MultiValueMap<String, MultipartFile> map = request.getMultiFileMap();
			for (String key : map.keySet()) {
				List<MultipartFile> list = map.get(key);
				for (MultipartFile multipartFile : list) {
					OrdSheet sheet = OrderExcelService.readExcel(multipartFile.getInputStream(),intr);
					sheets.add(sheet);
				}
			}
			HSSFWorkbook workbook = OMSExcel.createExcel(sheets);
			write(response,workbook);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e, null, WebUtils.getIpAddr(request));
			WebUtils.writeJsonByObj(new JsonResult(false), response);
		}
	}
	
	
	private void write(HttpServletResponse response, HSSFWorkbook workbook) {
		// 创建一个新文件
		    OutputStream os=null;
			try {
				String name = new Date().getTime()+"";
				response.setContentType("application/vnd.ms-excel");   
				response.setHeader("Content-disposition", "attachment;filename="+name+".xls");   
				os = response.getOutputStream();
				workbook.write(os);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error(e, null, null);
			}finally{
				try {
					os.flush();
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
	}
	
	
	
	private OrdSheet changeToWMSBean(List<OrdDTO> list){
		OrdSheet sheet = new OrdSheet();
		for (OrdDTO ordDTO : list) {
			String orderId = ordDTO.getOrderId();
			OrdDetailDTO detail = OrdClient.getDetail(orderId);
			
			OrdAddr addr = detail.getOrdAddr();
			
			WMSBean wms = new WMSBean();
			wms.setOrderId(ordDTO.getOrderId());
			wms.setBuyerMemo("");
			wms.setBuyerNickName(ordDTO.getUserName());
			wms.setBuyerPayment(ordDTO.getPayCode());
			if(addr!=null){
				wms.setCity(addr.getCity());
				wms.setTown(addr.getTown());
				wms.setProvince(addr.getProvince());
				wms.setMobile(addr.getMobile());
				wms.setPhone(addr.getPhone());
				wms.setReceiveAddress(addr.getDetailAddr());
				wms.setZipCode(addr.getZipCode());
				wms.setReceiver(addr.getRecvName());
			}
			wms.setCod(ordDTO.getOrderType());
			wms.setCodeOfDepot("");
			wms.setCodeOfTransport("");
			wms.setCreateTime(DateUtil.getDateStr(ordDTO.getCreateTime()));
			wms.setCustomerCode("");
			wms.setEmergency("");
			wms.setInvoice("");
			wms.setNameOfTransport("");
			wms.setNeedInvoice(ordDTO.getIsInvoice()+"");
			wms.setOrganization("");
			wms.setPostage(ordDTO.getTransportFee()+"");
			wms.setPreDeposit("");
			wms.setPreSaleType("");
			wms.setSellerMeno("");
			wms.setShopName("");
			sheet.addOrd(wms);
			for (OrdiDTO ordi : ordDTO.getOrdiList()) {
				WMSProduct pro = new WMSProduct();
				pro.setOrderId(ordi.getOrderId());
				pro.setName(ordi.getProName());
				pro.setPrice(ordi.getSaleUnit());
				pro.setNum(ordi.getSaleNum()+"");
				sheet.addPro(pro);
			}
		}
		return sheet;
	}
	/**
     * 查询条件返回页面
     *
     * @param request
     * @return
     */
    public static OrderQueryDTO setSearchParams(HttpServletRequest request, Integer os) {
        // TODO Auto-generated method stub
        OrderQueryDTO dto = new OrderQueryDTO();
        dto.setState(GlobalNames.STATE_VALID);
        
        String create_time_min = ServletRequestUtils.getStringParameter(
                request, "create_time_min", "");
        if (!StringUtil.checkNull(create_time_min)) {
            dto.setOrderTimeStart(DateUtil.parseToDate(create_time_min,
                    DateUtil.FORMAT_DATETIME));
        }
        String create_time_max = ServletRequestUtils.getStringParameter(
                request, "create_time_max", "");
        if (!StringUtil.checkNull(create_time_max)) {
            dto.setOrderTimeEnd(DateUtil.parseToDate(create_time_max,
                    DateUtil.FORMAT_DATETIME));
        }
        
        return dto;
    }
    
    private RequestLogVO setParams(HttpServletRequest request) {
		RequestLogVO vo = new RequestLogVO();
		
	    	String startTime = ServletRequestUtils.getStringParameter(request, "startTime", "");
	    	if(!StringUtil.checkNull(startTime)){
	    		Date sTime = DateUtil.parseDateTime(startTime);
	    		vo.setStartTime(sTime);
	    	}
	    	String endTime = ServletRequestUtils.getStringParameter(request, "endTime", "");
	    	if(!StringUtil.checkNull(endTime)){
	    		Date eTime = DateUtil.parseDateTime(endTime);
	    		vo.setEndTime(eTime);
	    	}
     	
	    	return vo;
	}
    
}

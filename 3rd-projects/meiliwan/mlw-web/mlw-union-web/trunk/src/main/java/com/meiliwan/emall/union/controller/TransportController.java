package com.meiliwan.emall.union.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.union.bean.SFPushData;
import com.meiliwan.emall.union.bean.Transport;
import com.meiliwan.emall.union.bean.TransportCode;
import com.meiliwan.emall.union.bean.WaybillRoute;
import com.meiliwan.emall.union.service.EMSService;
import com.meiliwan.emall.union.service.TransportService;
import com.meiliwan.emall.union.util.WebUtil;


@Controller
@RequestMapping("/transport")
public class TransportController {

	private final static MLWLogger logger = MLWLoggerFactory.getLogger(TransportController.class);
	
	@Autowired
	private TransportService transportService ;
	@Autowired
	private EMSService eMSService ;

	
	/**
	 * 单个请求的处理
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/rout")
	public void ems(HttpServletRequest request,
			HttpServletResponse response) {
		
		StringBuilder temp = new StringBuilder();
		try {
			InputStream stream=request.getInputStream();
			InputStreamReader isr=new InputStreamReader(stream,"utf-8");
			BufferedReader br=new BufferedReader(isr);
			
			String line;
			while ((line = br.readLine()) != null) {
				temp.append(line);
			}
			br.close();
			logger.info("接受参数EMS", temp.toString(), WebUtils.getIpAddr(request));
		} catch (IOException e) {
			logger.error(e, null, WebUtils.getIpAddr(request));
		}
		try {
			String answer = URLDecoder.decode(temp.toString(),"utf-8");
			TransportCode code = eMSService.validAndInsert(answer);
			logger.info("返回参数EMS", code.toMap(), WebUtils.getIpAddr(request));
			WebUtils.writeJsonByObj(code.toMap(), response);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			logger.error(e, null, WebUtils.getIpAddr(request));
		}
		
	}
	
	/**
	 * 单个请求的处理
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/sf")
	public void sf(HttpServletRequest request,
			HttpServletResponse response) {
		
		String answer;
		try {
			InputStream stream=request.getInputStream();
			InputStreamReader isr=new InputStreamReader(stream);
			BufferedReader br=new BufferedReader(isr);
			
			StringBuilder temp = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				temp.append(line);
			}
			br.close();
			logger.info("接收参数SF", temp.toString(), WebUtils.getIpAddr(request));
			answer = URLDecoder.decode(temp.toString(),"utf-8");
			if(StringUtils.isBlank(answer)){
				WebUtil.writeFailXmlToSF(TransportCode.REQUEST_DATA, response);
				return ;
			}
		} catch (Exception e) {
			logger.error(e, null, WebUtils.getIpAddr(request));
			WebUtil.writeFailXmlToSF(TransportCode.SYSTEM, response);
			return ;
		}  
		SFPushData sf=null;
		try {	 
			sf = SFPushData.parseToBean(answer);
			logger.debug(sf.toXml());
		} catch ( Exception e) {
			logger.error(e, sf, WebUtils.getIpAddr(request));
			WebUtil.writeFailXmlToSF(TransportCode.DATA_ERROR, response);
			return ;
		}
			
		List<WaybillRoute> list = sf.getList();
		TransportCode code=TransportCode.SUCCESS;
		for (WaybillRoute way : list) {
			Transport transport = new Transport();
			transport.setOrderId(way.getOrderid());
			transport.setCustDataId(way.getId());
			transport.setCreateTime(new Date());
			transport.setInfo(way.getRemark());
			transport.setLogisticsCompany("SF");
			transport.setTransportTime(new Date());
			transport.setLogisticsNumber(way.getMailno());
			TransportCode insertCode = transportService.insertTransport(transport);
			//sf的数据重复不处理
			if(!insertCode.isSuccess()&&!insertCode.isRepeat()){
				code=insertCode;
			}
		}
		logger.info("返回参数SF", code, WebUtils.getIpAddr(request));
		if(code.isSuccess()){
			WebUtil.writeSuccessXmlToSF(response);
		}else{
			logger.warn("顺丰处理没成功", code, null);
			WebUtil.writeFailXmlToSF(code,response);
		}
		
	}
	
	
	
}

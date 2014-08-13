package com.meiliwan.emall.monitor.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.util.StringUtil;
import com.meiliwan.emall.monitor.bean.MLWLog;
import com.meiliwan.emall.monitor.bean.MLWLogVO;
import com.meiliwan.emall.monitor.bean.RequestLog;
import com.meiliwan.emall.monitor.bean.RequestLogVO;
import com.meiliwan.emall.monitor.dao.MLWLogDao;
import com.meiliwan.emall.monitor.dao.RequestLogDao;

/**
 * 监控服务
 * @author rubi
 *
 */
@Service
public class MonitorService  {
	
	@Autowired
	private RequestLogDao requestLogDao;
	@Autowired
	private MLWLogDao mLWLogDao;
	
	public void setLogDao(RequestLogDao requestLogDao) {
		this.requestLogDao = requestLogDao;
	}

	/**
	 * 取得监控请求得分页
	 * @param resultObj
	 * @param dto
	 * @param pageInfo
	 * @return 
	 */
	public PagerControl<RequestLog> getPageRequest(RequestLogVO dto,PageInfo pageInfo){
		String order=null;
		if(dto.getTimeConsumeDesc()!=null){
			if(dto.getTimeConsumeDesc()){
				order=" order by time_consume desc ";
			}else{
				order=" order by time_consume asc ";
			}
		}
		if(dto.getStartTimeDesc()!=null){
			if(dto.getStartTimeDesc()){
				order=" order by start_time desc ";
			}else{
				order=" order by start_time asc ";
			}
		}
		
		if(!StringUtil.checkNull(dto.getName())){
			dto.setName(dto.getName()+"%");
		}
		PagerControl<RequestLog> pc = requestLogDao.getPageByDTO(dto, pageInfo, order);
		return pc;
	}
	
	/**
	 * 取得监控请求得数据根据ID
	 * @param resultObj
	 * @param id
	 * @return 
	 */
	public RequestLog getRequestById(int id){
		RequestLog log = requestLogDao.getEntityById(id);
		return log;
	}
	
	/**
	 * 取得日志分页
	 * @param resultObj
	 * @param dto
	 * @param pageInfo
	 * @return 
	 */
	public PagerControl<MLWLog> getPageMLWLog(MLWLogVO dto,PageInfo pageInfo){
		String order=null;
		if(dto.getOrder()!=null){
			if(dto.getOrder()){
				order=" order by id desc ";
			}else{
				order=" order by id asc ";
			}
		}
		PagerControl<MLWLog> pc = mLWLogDao.getPage(dto, pageInfo, order);
		return pc;
	}
	
	/**
	 * 取得监控请求得数据根据ID
	 * @param resultObj
	 * @param id
	 * @return 
	 */
	public MLWLog getMLWLogById(int id){
		MLWLog log = mLWLogDao.getEntityById(id);
		return log;
	}
	
	public List<MLWLog> getMLWLogListQueryBy10MinitueAgo(){
		MLWLogVO dto = new MLWLogVO();
		Calendar calen = Calendar.getInstance();
		//多少分钟之前
		calen.add(Calendar.MINUTE, -10);
		dto.setCreateTimeStart(calen.getTime());
		List<MLWLog> list = mLWLogDao.getListByMLWLogVO(dto);
		return list;
	}
	
	public List<RequestLog> getRequestLogListByNegativeMinitueAndType(int negativeHour){
		RequestLogVO dto = new RequestLogVO();
		Calendar calen = Calendar.getInstance();
		//多少分钟之前
		calen.add(Calendar.MINUTE, negativeHour);
		dto.setStartTime(calen.getTime());
		dto.setType(RequestLog.INTR);
		List<RequestLog> list = requestLogDao.getListByDTO(dto);
		return list;
	}
	
}

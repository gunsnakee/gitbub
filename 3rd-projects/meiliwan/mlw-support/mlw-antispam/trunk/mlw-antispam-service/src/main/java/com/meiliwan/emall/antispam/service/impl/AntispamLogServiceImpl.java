package com.meiliwan.emall.antispam.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.gson.JsonObject;
import com.meiliwan.emall.antispam.bean.AntispamLog;
import com.meiliwan.emall.antispam.dao.AntispamLogDao;
import com.meiliwan.emall.antispam.service.AntispamLogService;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.util.IPUtil;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;

@Service
public class AntispamLogServiceImpl extends DefaultBaseServiceImpl implements AntispamLogService {
	
	@Autowired
	AntispamLogDao dao;
	
	private static final MLWLogger LOG = MLWLoggerFactory.getLogger(AntispamLogServiceImpl.class);
	
	@Override
	public void getPageList(JsonObject resultObj, AntispamLog antispamLog,
			Date startTime, Date endTime, PageInfo pageInfo) {
		String whereSql = "audit_time > '" + DateUtil.getDateTimeStr(startTime) + "' and audit_time < '" 
			+ DateUtil.getDateTimeStr(endTime) + "' ";
		List<AntispamLog> list = null;
		try {
			list = dao.getListByObj(antispamLog, pageInfo, whereSql, " order by create_time desc ");
		} catch (Exception e) {
			Map<String, Object> remark = new HashMap<String, Object>();
			remark.put("antispamLog", antispamLog);
			remark.put("pageInfo", pageInfo);
			remark.put("startTime", startTime);
			remark.put("endTime", endTime);
			LOG.error(e, remark, IPUtil.getLocalIp());
		}
		if(list == null){
			list = new ArrayList<AntispamLog>();
		}
		PagerControl<AntispamLog> result = new PagerControl<AntispamLog>();
		result.setEntityList(list);
		result.setPageInfo(pageInfo);
		JSONTool.addToResult(result, resultObj);
	}

}

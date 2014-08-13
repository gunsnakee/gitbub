package com.meiliwan.emall.monitor.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.monitor.bean.Player;
import com.meiliwan.emall.monitor.bean.RequestExcludeSetting;
import com.meiliwan.emall.statistics.jdbc.ConnTool;
import com.mlcs.core.log.MLCSLogger;
import com.mlcs.core.log.MLCSLoggerFactory;

public class MonitorImitateICEDaoImpl {

	MLCSLogger logger = MLCSLoggerFactory.getLogger(getClass());
	@SuppressWarnings("unchecked")
	public List<Player> getAllPlayer() throws ServiceException {
		List<Player> list;
		Connection conn = ConnTool.getMonitorConnection();
		try {
			QueryRunner qRunner = new QueryRunner();
			String sql="select pid, name, mobile, email, state from player";
			list = (List<Player>) qRunner.query(conn,sql,new BeanListHandler(Player.class));
		} catch (SQLException e) {
			throw new ServiceException("getAllPlayer {} ",e);
		} finally{
			DbUtils.closeQuietly(conn);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<RequestExcludeSetting> getAllRequestExcludeSetting() throws ServiceException {
		List<RequestExcludeSetting> list;
		Connection conn = ConnTool.getMonitorConnection();
		try {
			QueryRunner qRunner = new QueryRunner();
			String sql="select rid, type, name,resume_time from request_exclude_setting";
			list = (List<RequestExcludeSetting>) qRunner.query(conn,sql,new BeanListHandler(RequestExcludeSetting.class));
		} catch (SQLException e) {
			throw new ServiceException("getAllRequestExcludeSetting {} ",e);
		} finally{
			DbUtils.closeQuietly(conn);
		}
		
		return list;
	}
	
	public void testLogger(){
		 Exception e = new RuntimeException("asdfsa");
		logger.error(e, null	, null);
		throw new RuntimeException("asdfsa");
		
	}
}

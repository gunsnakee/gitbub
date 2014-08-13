package com.meiliwan.emall.monitor.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.stereotype.Repository;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.monitor.dao.OrderDao;
import com.meiliwan.emall.statistics.jdbc.ConnTool;
import com.meiliwan.emall.statistics.jdbc.QueryHelper;

@Repository
public class OrderDaoImpl implements OrderDao {

	@Override
	public int getCount(String sql) throws ServiceException {
		Integer count=0;
		try {
			Connection conn = ConnTool.getOrderConnection();
			QueryRunner qRunner = new QueryRunner();
			count = (Integer) qRunner.query(conn,sql,QueryHelper.integerHandler);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new ServiceException("getCount {} ",e);
		}
		
		return count;
	}

	@Override
	public int getCount(String sql,Object[] params) throws ServiceException {
		Integer count=0;
		try {
			Connection conn = ConnTool.getOrderConnection();
			QueryRunner qRunner = new QueryRunner();
			count = (Integer) qRunner.query(conn,sql,QueryHelper.integerHandler,params);
		} catch (SQLException e) {
			throw new ServiceException("getCount {} ",e);
		} 
		
		return count;
	}
	
	
}

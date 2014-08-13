package com.meiliwan.emall.statistics.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.ConfigOnZk;

/**
 * 
 */
public class ConnTool {
	
	private static final MLWLogger logger = MLWLoggerFactory.getLogger(ConnTool.class);
	
	private static String dirverClassName = "com.mysql.jdbc.Driver";
	private static String url;
	private static String user;
	private static String password;
	private static String monitorUrl;
	private static String monitorUser;
	private static String monitorPassword;
	private static Connection orderConn;
	private static Connection monitorConn;
	
	static{
		try {
			ConfigOnZk  configOnZk  = ConfigOnZk.getInstance();
			url = configOnZk.getValue("monitor-service/orderJdbc.properties", "order.jdbc.url");
			user = configOnZk.getValue("monitor-service/orderJdbc.properties", "order.jdbc.username");
			password = configOnZk.getValue("monitor-service/orderJdbc.properties", "order.jdbc.password");
			
			monitorUrl = configOnZk.getValue("monitor-service/monitorJdbc.properties", "monitor.jdbc.url");
			monitorUser = configOnZk.getValue("monitor-service/monitorJdbc.properties", "monitor.jdbc.username");
			monitorPassword = configOnZk.getValue("monitor-service/monitorJdbc.properties", "monitor.jdbc.password");
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			logger.error(e, null, null);
		}
	}
	public static synchronized Connection getOrderConnection() {
		try {
			Class.forName(dirverClassName);
		} catch (ClassNotFoundException e) {
			logger.error(e, null, null);
		}
		try {
			orderConn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			logger.error(e, null, null);
		}
		return orderConn;
	}
	public static synchronized Connection getMonitorConnection() {
		try {
			Class.forName(dirverClassName);
		} catch (ClassNotFoundException e) {
			logger.error(e, null, null);
		}
		try {
			monitorConn = DriverManager.getConnection(monitorUrl, monitorUser, monitorPassword);
		} catch (SQLException e) {
			logger.error(e, null, null);
		}
		return monitorConn;
	}
	

}
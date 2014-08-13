package com.meiliwan.emall.union.dao.dbtool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.ConfigOnZk;

public class DBTool {
	
	private static final MLWLogger LOG = MLWLoggerFactory.getLogger(DBTool.class);
	private static BoneCP connPool = null;
	private static final String ZK_CONFIG_PATH = "bg-union-service/jdbc.properties";
	
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			ConfigOnZk config = ConfigOnZk.getInstance();
			String jdbcUrl = config.getValue(ZK_CONFIG_PATH, "jdbc.url.1");
			String userName = config.getValue(ZK_CONFIG_PATH, "jdbc.username.1");
			String passwd = config.getValue(ZK_CONFIG_PATH, "jdbc.password.1");
			int partitionCount = Integer.valueOf(config.getValue(ZK_CONFIG_PATH, "jdbc.partitionCount.1", "2"));
			int minConnPerPartition = Integer.valueOf(config.getValue(ZK_CONFIG_PATH, "jdbc.minConnectionsPerPartition.1", "3"));
			int maxConnPerPartition = Integer.valueOf(config.getValue(ZK_CONFIG_PATH, "jdbc.maxConnectionsPerPartition.1", "20"));
			int idleMaxAge = Integer.valueOf(config.getValue(ZK_CONFIG_PATH, "jdbc.idleMaxAgeInMinutes.1", "60"));
			int idleConnectionTestPeriod = Integer.valueOf(config.getValue(ZK_CONFIG_PATH, "jdbc.idleConnectionTestPeriodInMinutes.1", "30"));
			int releaseHelperThreads = Integer.valueOf(config.getValue(ZK_CONFIG_PATH, "jdbc.releaseHelperThreads.1", "2"));
			
			BoneCPConfig dbConfig = new BoneCPConfig();
			dbConfig.setJdbcUrl(jdbcUrl);
			dbConfig.setUsername(userName);
			dbConfig.setPassword(passwd);
			dbConfig.setPartitionCount(partitionCount);
			dbConfig.setMinConnectionsPerPartition(minConnPerPartition);
			dbConfig.setMaxConnectionAgeInSeconds(maxConnPerPartition);
			dbConfig.setIdleMaxAgeInMinutes(idleMaxAge);
			dbConfig.setReleaseHelperThreads(releaseHelperThreads);
			dbConfig.setIdleConnectionTestPeriodInMinutes(idleConnectionTestPeriod);
			
			connPool = new BoneCP(dbConfig);
		} catch (ClassNotFoundException e) {
			LOG.error(e, "load class com.mysql.jdbc.Driver error", null);
		} catch (BaseException e) {
			LOG.error(e, "get config param form zk failure", null);
		} catch (SQLException e) {
			LOG.error(e, "connect jdbc error", null);
		}
	}
	
	public static Connection getConn(){
		if(connPool != null){
			try {
				return connPool.getConnection();
			} catch (SQLException e) {
				LOG.error(e, "get jdbc connection  error", null);
			}
		}
		
		return null;
	}
	
	public static void closeConn(Connection conn){
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				LOG.error(e, "close jdbc connection  error", null);
			}
		}
	}
	
	public static void closeConn(Connection conn, Statement stmt){
		if(stmt != null){
			try {
				stmt.close();
			} catch (SQLException e) {
				LOG.error(e, "close jdbc Statement  error", null);
			}
		}
		
		closeConn(conn);
	}
	
	public static void closeConn(Connection conn, Statement stmt, ResultSet rs){
		if(rs != null){
			try {
				rs.close();
			} catch (SQLException e) {
				LOG.error(e, "close jdbc ResultSet  error", null);
			}
		}
		
		closeConn(conn, stmt);
	}

}

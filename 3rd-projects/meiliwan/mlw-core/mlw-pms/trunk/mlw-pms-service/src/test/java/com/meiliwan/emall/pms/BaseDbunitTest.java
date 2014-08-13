package com.meiliwan.emall.pms;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.dbunit.DatabaseTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;


public class BaseDbunitTest extends DatabaseTestCase {
	
	private String initDbFile = "";
	
	private String backupDbFile = "";
	
	protected Connection getJdbcConnection() throws SQLException {
		// obtain a jdbc connection to the database
		try{
    		// 本例使用postgresql 数据库 
    		Class.forName("com.mysql.jdbc.Driver");
    		// 连接DB 
    		Connection conn = DriverManager.getConnection("jdbc:mysql://10.249.15.196:3306/mlw_product?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=UTF-8","13mlw","mlwhappy543");
    		return conn;
    	}catch(Exception e){
    		throw new SQLException(e);
    	}
	}
	
	@Override
	protected IDatabaseConnection getConnection() throws Exception {
		return new DatabaseConnection(getJdbcConnection());
	}
	
	public IDatabaseConnection getConnection(Connection conn) throws Exception {
		return new DatabaseConnection(conn);
	}
	
	
	@Override
	protected IDataSet getDataSet() throws Exception {
		InputStream is = getClass().getResourceAsStream(initDbFile);
		IDataSet dataSet = new FlatXmlDataSetBuilder().build(is);
		return dataSet;
	}
	
	protected IDataSet getDataSet(String file) throws Exception {
		InputStream is = getClass().getResourceAsStream(file);
		IDataSet dataSet = new FlatXmlDataSetBuilder().build(is);
		return dataSet;
	}
	
	public IDataSet getBackupDataSet() throws Exception {
		InputStream is = new FileInputStream(backupDbFile);
		IDataSet dataSet = new FlatXmlDataSetBuilder().build(is);
		return dataSet;
	}
	
	/**
	 * 
	 * @param initDabfile
	 * 
	 * 设置dbunit的初始数据集文件名，默认从BaseDbunitTest所在目录开始
	 */
	protected void setInitDbFile(String initDbFile) {
		this.initDbFile = initDbFile;
	}
	
	/**
	 * 
	 * @param initDabfile
	 * 
	 * 设置dbunit的数据备份文件名
	 */
	protected void setBackupDbFile(String backupDbFile) {
		this.backupDbFile = backupDbFile;
	}
	
}

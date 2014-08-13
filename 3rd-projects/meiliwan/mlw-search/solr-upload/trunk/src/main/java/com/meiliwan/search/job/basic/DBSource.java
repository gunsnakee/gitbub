package com.meiliwan.search.job.basic;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import com.meiliwan.emall.commons.util.BaseConfig;



public class DBSource {
	Connection conn = null;
	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public Statement getStmt() {
		return stmt;
	}

	public void setStmt(Statement stmt) {
		this.stmt = stmt;
	}

	public ResultSet getRs() {
		return rs;
	}

	public void setRs(ResultSet rs) {
		this.rs = rs;
	}

	Statement stmt = null;
	ResultSet rs = null;
	
	
	public DBSource(String dbUrl, String userName, String password) throws Exception{
		Class.forName("com.mysql.jdbc.Driver");
		conn = java.sql.DriverManager.getConnection(
				dbUrl.trim(), userName, password);
		stmt = conn.createStatement();
		stmt.setFetchSize(Integer.MIN_VALUE);
	}
		
	
	public void close() throws Exception{
		if (rs!= null){
			rs.close();
		}
		if (stmt!= null){
			stmt.close();
		}
		if (conn!= null){
			conn.close();
		}
	}
}

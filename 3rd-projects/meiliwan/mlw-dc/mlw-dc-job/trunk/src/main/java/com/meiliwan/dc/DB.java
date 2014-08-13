package com.meiliwan.dc;

import java.sql.Connection;
import java.sql.SQLException;

import com.meiliwan.emall.commons.util.BaseConfig;

public class DB {

	Connection conn = null;
	
	public Connection getConn() {
		return conn;
	}

	public DB(String dburl, String user, String pass) throws ClassNotFoundException, SQLException{
		Class.forName("com.mysql.jdbc.Driver");
		conn = java.sql.DriverManager.getConnection(
				dburl, user, pass);
		
	}
	
	public DB() throws SQLException, ClassNotFoundException{
		Class.forName("com.mysql.jdbc.Driver");
		conn = java.sql.DriverManager.getConnection(
				BaseConfig.getValue("dburl"), BaseConfig.getValue("user"), BaseConfig.getValue("pass"));
	}
	
	public void close() throws SQLException{
		conn.close();
	}
	
}

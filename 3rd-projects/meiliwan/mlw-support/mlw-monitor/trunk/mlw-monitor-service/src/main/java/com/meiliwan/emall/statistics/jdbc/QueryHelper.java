package com.meiliwan.emall.statistics.jdbc;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

public class QueryHelper {


	public final static ResultSetHandler integerHandler = new ResultSetHandler() {

		public Integer handle(ResultSet rs) throws SQLException {
			if (rs.next()) {
				return rs.getInt(1); // 或者rs.getLong("count");
			}
			return 0;
		}

	};
}

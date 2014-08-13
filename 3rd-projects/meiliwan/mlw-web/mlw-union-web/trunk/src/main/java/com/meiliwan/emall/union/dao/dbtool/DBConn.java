package com.meiliwan.emall.union.dao.dbtool;

import com.jolbox.bonecp.BoneCPDataSource;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 数据库连接工具类
 * User: wuzixin
 * Date: 14-5-14
 * Time: 下午2:49
 */
public class DBConn {

    private static final MLWLogger LOG = MLWLoggerFactory.getLogger(DBConn.class);

    public static Connection getConn(BoneCPDataSource dataSource) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
        } catch (SQLException e) {
            LOG.error(e, "获取数据库连接失败", "");
        }
        return conn;
    }

    public static Statement getSt(Connection conn) {
        Statement st = null;
        try {
            st = conn.createStatement();
        } catch (SQLException e) {
            LOG.error(e, "获取数据库连接声明失败", "");
        }
        return st;
    }


    public static void closeConn(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                conn = null;
            } catch (SQLException e) {
                LOG.error(e, "关闭数据库连接失败", "");
            }
        }
    }

    public static void closeSt(Statement st) {
        if (st != null) {
            try {
                st.close();
                st = null;
            } catch (SQLException e) {
                LOG.error(e, "关闭数据库数据集连接失败", "");
            }
        }
    }

    public static void closeRs(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
                rs = null;
            } catch (SQLException e) {
                LOG.error(e, "关闭数据库数据集连接失败", "");
            }
        }
    }
}

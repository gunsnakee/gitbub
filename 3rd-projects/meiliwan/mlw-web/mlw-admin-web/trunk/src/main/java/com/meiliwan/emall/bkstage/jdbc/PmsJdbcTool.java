package com.meiliwan.emall.bkstage.jdbc;

import com.google.gson.Gson;
import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.ConfigOnZk;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * pms数据库工具类
 * Created with IntelliJ IDEA.
 * User: yyluo
 * Date: 14-7-28
 * Time: 下午2:31
 * To change this template use File | Settings | File Templates.
 */
public class PmsJdbcTool {

    private static final MLWLogger logger = MLWLoggerFactory.getLogger(PmsJdbcTool.class);

    private static String dirverClassName = "com.mysql.jdbc.Driver";
    private static String url;
    private static String user;
    private static String password;

    private static Connection conn;
    static{
        try {
            ConfigOnZk configOnZk  = ConfigOnZk.getInstance();
            url = configOnZk.getValue("bkstage-web/jdbc.properties", "pms.jdbc.url.1");
            user = configOnZk.getValue("bkstage-web/jdbc.properties", "pms.jdbc.username.1");
            password = configOnZk.getValue("bkstage-web/jdbc.properties", "pms.jdbc.password.1");
        } catch (BaseException e) {
            logger.error(e, "从ZK获取pms jdbc 连接信息配置文件异常", null);
        }
    }

    public static synchronized Connection getConnection() {
        try {
            Class.forName(dirverClassName);
        } catch (ClassNotFoundException e) {
            logger.error(e, null, null);
        }
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            logger.error(e, "获取pms库连接异常", null);
        }
        return conn;
    }

    /**
     * 使用例子
     * @param args
     */
    public final static void main(String[] args){
        Connection conn = PmsJdbcTool.getConnection();
        QueryRunner qRunner = new QueryRunner();
        try{
            String sql="SELECT p.pro_id AS \"商品id\",p.bar_code AS \"条形码\",p.pro_name AS \"商品名称\",p.mlw_price AS \"美丽价\",p.trade_price AS \"进货价\",s.store_name AS \"所属馆\",c.category_name AS \"所属类目\", p.state AS \"商品状态\", p.presale_end_time AS \"预售结束时间\", k.stock AS '库存' FROM mlw_product.pro_product p,mlw_product.pro_store_category s ,mlw_product.pro_category c, mlw_product.pro_stock k WHERE p.third_category_id = s.third_category_id AND p.third_category_id = c.category_id AND p.pro_id = k.pro_id AND p.state != '-1'";
            List<Map> resultMap = (List<Map> )qRunner.query(conn,sql,new MapListHandler());
            Gson gson = new Gson();
            System.out.print(gson.toJson(resultMap));
        }catch (Exception e){
            logger.error(e,"pms sql查询异常",null);
        }

    }
}

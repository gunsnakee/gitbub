package com.meiliwan.emall.commons.log;


import ch.qos.logback.core.db.DriverManagerConnectionSource;

import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.KeeperException;
import org.slf4j.LoggerFactory;

import com.meiliwan.emall.commons.plugin.zk.ZKClient;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 把logback数据库配置移到Zookeeper。
 *
 * User: jiawuwu
 * Date: 13-10-31
 * Time: 下午5:49
 * To change this template use File | Settings | File Templates.
 */
public class MLWDriverManagerConnectionSource extends DriverManagerConnectionSource {


    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());


    private String confPath ;

    public String getConfPath() {
        return confPath;
    }

    public void setConfPath(String confPath) {
        this.confPath = confPath;
    }



    @Override
    public Connection getConnection() throws SQLException {
        if(StringUtils.isBlank(confPath)){
            confPath = "/mlwconf/commons/logDb.properties";
        }
        String conf = null;
        try {
            conf = ZKClient.get().getStringData(confPath);
        } catch (KeeperException e) {
            logger.error("ZKClient.get().getStringData(confPath): {confPath:"+confPath+"}",e);
        } catch (InterruptedException e) {
            logger.error("ZKClient.get().getStringData(confPath): {confPath:"+confPath+"}",e);
        }
        if(StringUtils.isBlank(conf)){
            logger.error("获取不到配置信息. ZKClient.get().getStringData(confPath): {confPath:"+confPath+"}");
            return null;
        }
        Properties p = new Properties();
        try {
            p.load(new StringReader(conf));
        } catch (IOException e) {
            logger.error("尝试加载配置信息文本数据到properties时发生异常. {content:" + conf + "}");
        }

        return DriverManager.getConnection(p.get("url").toString(), p.get("user").toString(), p.get("password").toString());
    }




}

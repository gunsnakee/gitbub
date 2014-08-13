package com.meiliwan.emall.base.util;


import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.exception.BaseRuntimeException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.ConfigOnZk;
import com.meiliwan.emall.commons.util.HttpClientUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.http.protocol.HTTP;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.io.StringReader;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 悠普短信http接口
 * Created by jiawuwu on 13-8-23.
 *
 */
public class MobileMsgUtilShangh {

    private static final MLWLogger LOGGER = MLWLoggerFactory.getLogger(MobileMsgUtilShangh.class);
    private static final String PROPERTIES_ZK = "commons/messageShangh.properties";

    private MobileMsgUtilShangh(){

    }

    /**
     * 短信发送
     * @param sm
     * @param da
     * @param st
     * @return  605- 成功
     * @throws java.io.IOException
     * @throws BaseException
     */
    public static int send(String sm,String da,String st) throws IOException, BaseException {

        LOGGER.debug("prepare sendMsg to " + da + " , content is " + sm);

        if(StringUtils.isBlank(sm)){
            LOGGER.error(new BaseRuntimeException(GlobalNames.DEFAULT_SYS_ERROR_MSG), "短信内容不能为空.{phone:" + da + ",content:" + da + "}", null);
            return -100;
        }
        if(StringUtils.isBlank(da)){
            LOGGER.error(new BaseRuntimeException(GlobalNames.DEFAULT_SYS_ERROR_MSG), "手机号码不能为空.{phone:" + da + ",content:" + da + "}", null);
            return -102;
        }
        if(StringUtils.isNotBlank(st)){
            try {
                Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(st);
            }catch (Exception e){
                LOGGER.error(new BaseRuntimeException(GlobalNames.DEFAULT_SYS_ERROR_MSG), "发送时间格式不正确.{phone:" + da + ",content:" + da + ",sendTime:" + st + "}", null);
                return -103;
            }
        }

        String url = ConfigOnZk.getInstance().getValue(PROPERTIES_ZK, "url");
        String user = ConfigOnZk.getInstance().getValue(PROPERTIES_ZK, "user");
        String pw = ConfigOnZk.getInstance().getValue(PROPERTIES_ZK, "pw");

        if(StringUtils.isBlank(url)||StringUtils.isBlank(user)||StringUtils.isBlank(pw)){
            LOGGER.error(new BaseRuntimeException(GlobalNames.DEFAULT_SYS_ERROR_MSG), "无法从Zookepper中获取url,user,pw. ZK路径： " + PROPERTIES_ZK, null);
            return -1;
        }


        sm = sm.trim();
        da = da.trim();
        st = (st==null)?"":st;

        String[] data = new String[5];
        data[0]="user="+URLEncoder.encode(user,HTTP.UTF_8);
        data[1]="pw="+URLEncoder.encode(pw,HTTP.UTF_8);
        data[2]="sm="+URLEncoder.encode(sm, HTTP.UTF_8);
        data[3]="da="+URLEncoder.encode(da,HTTP.UTF_8);
        data[4]="st="+URLEncoder.encode(st,HTTP.UTF_8);


        String rs = HttpClientUtil.getInstance().doGet(url+"?"+StringUtils.join(data,'&'));

        LOGGER.debug("===> " + rs);

        if(StringUtils.isBlank(rs)){
            LOGGER.error(new BaseRuntimeException(GlobalNames.DEFAULT_SYS_ERROR_MSG), "短信接口收不到发送结果数据." + url + "?" + StringUtils.join(data, '&'), null);
            return -301;
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder= null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            LOGGER.error(e,"factory.newDocumentBuilder()",null);
            return -2;
        }

        Document doc=null;
        try {
             doc = builder.parse(new InputSource(new StringReader(rs)));
        } catch (SAXException e) {
            LOGGER.error(e, "无法解析短信接口返回数据成Document.{rs:" + rs + "}", null);
            return -302;
        }

        if(doc==null){
            LOGGER.error(new BaseRuntimeException(GlobalNames.DEFAULT_SYS_ERROR_MSG), "无法解析短信接口返回数据成Document.{rs:" + rs + "}", null);
            return -302;
        }

        NodeList result = doc.getElementsByTagName("result");

        if(result.getLength()<1){
            LOGGER.error(new BaseRuntimeException(GlobalNames.DEFAULT_SYS_ERROR_MSG), "xml中缺少result节点.{rs:" + rs + "}", null);
        }

        String rv = result.item(0).getTextContent();

        LOGGER.debug("接口原始回执状态码 " + rv);

        LOGGER.info("send msg to "+da,rs,null);

        if(StringUtils.isNotBlank(rv) && rv.equals("0")){
           return 605;
        }else{
           return -303;
        }


    }


}

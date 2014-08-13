package com.meiliwan.emall.commons.util;

import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.exception.BaseRuntimeException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.web.exception.ErrorPageCode;
import org.apache.commons.lang.StringUtils;
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
public class MobileMsgUtilGuodu {

    private static final MLWLogger LOGGER = MLWLoggerFactory.getLogger(MobileMsgUtilGuodu.class);
    private static final String PROPERTIES_ZK = "commons/message.properties";

    private MobileMsgUtilGuodu(){

    }

    /**
     * 短信发送
     * @param sm 短信内容
     * @param extraMsg 短信签名
     * @param da 手机号码，多个的话用，隔开 最多支持20个
     * @param st 发送时间 YYYYMMDDHHMMSS
     * @return  605- 成功
     * @throws java.io.IOException
     * @throws com.meiliwan.emall.commons.exception.BaseException
     */
    public static int send(String sm,String extraMsg,String da,String st) throws IOException, BaseException {

        LOGGER.debug("prepare sendMsg to " + da + " , content is " + sm);

        if(StringUtils.isBlank(sm)){
            LOGGER.error(new BaseRuntimeException(ErrorPageCode.EXCEPTION_SYSTEM_DEFAULT), "短信内容不能为空.{phone:" + da + ",content:" + da + "}", null);
            return -100;
        }
        if(StringUtils.isBlank(da)){
            LOGGER.error(new BaseRuntimeException(ErrorPageCode.EXCEPTION_SYSTEM_DEFAULT), "手机号码不能为空.{phone:" + da + ",content:" + da + "}", null);
            return -102;
        }
        if(StringUtils.isNotBlank(st)){
            try {
                Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(st);
            }catch (Exception e){
                LOGGER.error(new BaseRuntimeException(ErrorPageCode.EXCEPTION_SYSTEM_DEFAULT), "发送时间格式不正确.{phone:" + da + ",content:" + da + ",sendTime:" + st + "}", null);
                return -103;
            }
        }

        String url = ConfigOnZk.getInstance().getValue(PROPERTIES_ZK, "message.guodo.url");
        String user = ConfigOnZk.getInstance().getValue(PROPERTIES_ZK, "message.guodo.OperID");
        String pw = ConfigOnZk.getInstance().getValue(PROPERTIES_ZK, "message.guodo.OperPass");

        if(StringUtils.isBlank(url)||StringUtils.isBlank(user)||StringUtils.isBlank(pw)){
            LOGGER.error(new BaseRuntimeException(ErrorPageCode.EXCEPTION_SYSTEM_DEFAULT), "无法从Zookepper中获取url,OperID,OperPass. ZK路径： " + PROPERTIES_ZK, null);
            return -1;
        }


        sm = sm.trim()+(extraMsg==null?"":extraMsg);
        da = da.trim();
        st = (st==null)?"":st;

        int ContentType = sm.length()>=67 ? 8:15; // 8是长短信， 15是普通短信

        String[] data = new String[5];
        data[0]="OperID="+user;
        data[1]="OperPass="+pw;
        data[2]="Content="+URLEncoder.encode(sm, "GBK");
        data[3]="DesMobile="+da;
        data[4]="ContentType="+ContentType;


        String rs = HttpClientUtil.get().doGet(url+"?"+StringUtils.join(data,'&'));

        LOGGER.debug("===> " + rs);

        if(StringUtils.isBlank(rs)){
            LOGGER.error(new BaseRuntimeException(ErrorPageCode.EXCEPTION_SYSTEM_DEFAULT), "短信接口收不到发送结果数据." + url + "?" + StringUtils.join(data, '&'), null);
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
            LOGGER.error(new BaseRuntimeException(ErrorPageCode.EXCEPTION_SYSTEM_DEFAULT), "无法解析短信接口返回数据成Document.{rs:" + rs + "}", null);
            return -302;
        }

        NodeList result = doc.getElementsByTagName("code");

        if(result.getLength()<1){
            LOGGER.error(new BaseRuntimeException(ErrorPageCode.EXCEPTION_SYSTEM_DEFAULT), "xml中缺少result节点.{rs:" + rs + "}", null);
        }

        String rv = result.item(0).getTextContent();

        LOGGER.debug("接口原始回执状态码 " + rv);

        if(StringUtils.isNotBlank(rv) && ( "00".equals(rv) || "01".equals(rv) || "03".equals(rv)  )){
            LOGGER.info("success msg to "+da,"success send to "+da,null);
            return 605;
        }else{
           LOGGER.warn("fail msg to "+da,rs,null);
           return -303;
        }


    }


}

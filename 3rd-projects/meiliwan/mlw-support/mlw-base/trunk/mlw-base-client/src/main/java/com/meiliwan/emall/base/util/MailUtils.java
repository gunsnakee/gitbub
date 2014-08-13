package com.meiliwan.emall.base.util;


import com.meiliwan.emall.base.dto.MailEntity;
import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.plugin.zk.ZKClient;
import com.meiliwan.emall.commons.util.ConfigOnZk;
import com.meiliwan.emall.commons.util.RegexUtil;

import org.apache.zookeeper.KeeperException;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.URLDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * 邮件处理公共类
 * User: guangde.tang
 * Date: 13-5-20
 * Time: 上午11:42
 */
public class MailUtils {


    private static final MLWLogger LOGGER = MLWLoggerFactory.getLogger(MailUtils.class);
    
    // zookepper配置文件路径
    private   static final String PROPERTIES_ZK = "commons/mail.properties";
    
    // mail发送需要的KEY
    private   static final String SERVER = "mail.smtp.server";
    private   static final String USER_NAME = "mail.smtp.username";
    private   static final String SEND = "mail.smtp.send";
    private   static final String PASSWORD = "mail.smtp.password";

    public static final String SAFESTOCK = "/mlwconf/commons/mail.properties";
    public static final String SAFESTOCKKEY = "safe.stock.mail";


    private MailUtils(){

    }


    /**
     *
     * @param email
     * @return
     */
    public static String parseEmailLoginUrl(String email) {
        StringBuilder forwardEmailUrl = new StringBuilder();
        if(RegexUtil.isEmail(email))
        {
            forwardEmailUrl.append("http://");
            String suffix = email.split("@")[1];
            if("gmail.com".equals(suffix))
            {
                forwardEmailUrl.append("gmail.").append("google.com");
            } else if("outlook.com".equals(suffix)){
            	forwardEmailUrl.append(suffix).append("/?ocid=msnmail");
            } else {
                forwardEmailUrl.append("mail.").append(email.split("@")[1]);
            }
        }
        return forwardEmailUrl.toString();
    }

    /**
     * 获取邮件发送服务器地址
     * @return
     */
    public static  String getServer(){
    	
    	String server = null;
    	
    	try {
    		server = ConfigOnZk.getInstance().getValue(PROPERTIES_ZK, SERVER);
		} catch (BaseException e) {
            LOGGER.error(e,"ConfigOnZk.getInstance().getValue(PROPERTIES_ZK, SERVER):{PROPERTIES_ZK:"+PROPERTIES_ZK+",SERVER:"+SERVER+"}","");
		}
    	return server;
    }
    
    /**
     * 获取邮件发送地址
     * @return
     */
    public static String getSend(){
    	String value = null;
    	try {
            value = ConfigOnZk.getInstance().getValue(PROPERTIES_ZK, SEND);
		} catch (BaseException e) {
            LOGGER.error(e,"ConfigOnZk.getInstance().getValue(PROPERTIES_ZK, SEND):{PROPERTIES_ZK:"+PROPERTIES_ZK+",SEND:"+SEND+"}","");
		}
    	return value;
    }
    
    /**
     * 获取邮件发送用户名
     * @return
     */
    public static String getUserName(){
    	String value = null;
    	try {
            value = ConfigOnZk.getInstance().getValue(PROPERTIES_ZK, USER_NAME);
		} catch (BaseException e) {
            LOGGER.error(e,"ConfigOnZk.getInstance().getValue(PROPERTIES_ZK, USER_NAME):{PROPERTIES_ZK:"+PROPERTIES_ZK+",USER_NAME:"+USER_NAME+"}","");
		}
    	return value;
    }
    
    /**
     * 获取邮件发送密码
     * @return
     */
    public static String getPassword(){
    	String value = null;
    	try {
            value = ConfigOnZk.getInstance().getValue(PROPERTIES_ZK, PASSWORD);
		} catch (BaseException e) {
            LOGGER.error(e,"ConfigOnZk.getInstance().getValue(PROPERTIES_ZK, USER_NAME):{PROPERTIES_ZK:"+PROPERTIES_ZK+",PASSWORD:"+PASSWORD+"}","");
		}
    	return value;
    }
    
    

    /**
     * 发送邮件
     * @param from    发件人
     * @param to      收件人（多人用逗号隔开）
     * @param subject 邮件标题
     * @param body    邮件内容（一般情况是html邮件模板，然后替换内容）
     * @return
     * 
     */
    public static boolean sendMail(String from, String to, String subject, String body) throws MessagingException, UnsupportedEncodingException {
        return sendMail(from, to, subject, body,from);
    }

    /**
     *
     * @param to  收件人
     * @param body  邮件内容
     * @return
     */
    public static boolean sendMail( String to,String body) throws UnsupportedEncodingException, MessagingException {
         return  sendMail(getSend(),to,"美丽湾商城",body,"美丽湾商城");
    }

    public static boolean sendMail(MailEntity mailEntity) throws UnsupportedEncodingException, MessagingException {
        return  sendMail(mailEntity.getSender(),mailEntity.getReceivers(),mailEntity.getTitle(),mailEntity.getContent(),mailEntity.getSenderName(),mailEntity.getUrlDataSources());
    }
    
    public static boolean sendMail(MailEntity mailEntity,String... filePath) throws UnsupportedEncodingException, MessagingException{
    	return sendMail(mailEntity.getSender(), mailEntity.getReceivers(), mailEntity.getTitle(), mailEntity.getContent(), mailEntity.getSenderName(), filePath);
    }

    /**
     * 发送邮件
     * @param from     发件人
     * @param to       收件人（多人用逗号隔开）
     * @param subject  邮件标题
     * @param body     邮件内容（一般情况是html邮件模板，然后替换内容）
     * @param showName 邮件显示的发件人姓名
     * @param filePaths 附件
     * @return
     */
    public static boolean sendMail(String from, String to, String subject, String body, String showName,String... filePaths) throws UnsupportedEncodingException, MessagingException {
        return sendMail(from,to,subject,body,showName,null,filePaths);
    }

    public static boolean sendMail(String from, String to, String subject, String body, String showName,List<Map<String,String>> urlDataSources,String... filePaths){
        try {
            Properties props = System.getProperties();

            String server = getServer();

            props.put("mail.smtp.host", server);
            props.put("mail.smtp.localhost", server);

            Session session;
            String userName = getUserName();


            if (userName != null && !userName.equals("")) {
                props.put("mail.smtp.socketFactory.port",465);
                props.put("mail.smtp.starttls.enable","true");
                props.put("mail.smtp.auth", "true");
                String password = getPassword();
                SmtpAuthenticator sa = new SmtpAuthenticator(userName, password);
                session = Session.getInstance(props, sa);
            } else {
                props.put("mail.smtp.auth", "false");
                session = Session.getDefaultInstance(props, null);
            }

            session.setDebug(true);

            MimeMessage msg = new MimeMessage(session);
            InternetAddress ia = new InternetAddress(from, showName, "GBK");
            msg.setFrom(ia);
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            msg.setSubject(subject, "gb2312");
            msg.setSentDate(new Date());

            Multipart mp = new MimeMultipart("mixed");

            // 用于保存最终正文部分
            MimeBodyPart contentBody = new MimeBodyPart();

            Multipart contentMultipart = new MimeMultipart("related");
            // 正文的文本部分
            MimeBodyPart textBody = new MimeBodyPart();
            textBody.setContent(body,"text/html; charset=utf-8");

            contentMultipart.addBodyPart(textBody);

            if(urlDataSources!=null && urlDataSources.size()>0){
                for(Map<String,String> urlDataSource:urlDataSources){
                    contentMultipart.addBodyPart(createUrlPart(urlDataSource.get("cid"),urlDataSource.get("url")));
                }
            }

            contentBody.setContent(contentMultipart);


            mp.addBodyPart(contentBody);

            if(filePaths!=null && filePaths.length>0){
                for(String filePath:filePaths){
                    mp.addBodyPart(createAttachment(filePath));
                }
            }

            msg.setContent(mp);
            msg.saveChanges();

            Transport.send(msg);

            LOGGER.info("Send Mail to " + to, "Send Mail:from=" + from + "|to=" + to + "|subject=" + subject + "|showName=" + showName + "|bodyLength=" + body.length(), null);

        }catch (Exception e){
            LOGGER.error(e, "{from:" + from + ",to:" + to + ",subject:" + subject + ",showName:" + showName + ",body:" + body + ",urlDataSources:" + urlDataSources + ",filePaths:" + Arrays.asList(filePaths) + "}", "");
            return false;
        }

        return true;
    }


     /**
     * 根据传入的文件路径创建附件并返回
     */
     private static MimeBodyPart createAttachment(String fileName) throws MessagingException {
        MimeBodyPart attachmentPart = new MimeBodyPart();
        FileDataSource fds = new FileDataSource(fileName);
        attachmentPart.setDataHandler(new DataHandler(fds));
        attachmentPart.setFileName(fds.getName());
        return attachmentPart;
    }

    private static MimeBodyPart createUrlPart(String cid,String url) throws MalformedURLException, MessagingException {
        MimeBodyPart urlPart = new MimeBodyPart();
        URLDataSource uds = new URLDataSource(new URL(url));
        urlPart.setDataHandler(new DataHandler(uds));
        urlPart.setContentID(cid);
        return urlPart;
    }

    /**
     * 获取安全库存要发送到指定人的邮件
     * @return
     */
    public static String getSafeStockMail() throws KeeperException, InterruptedException, IOException {
        String value = null;
        byte[] values = ZKClient.get().getData(SAFESTOCK);
        ByteArrayInputStream stream = new ByteArrayInputStream(values);
        Properties properties = new Properties();
        properties.load(stream);
        value = properties.getProperty(SAFESTOCKKEY);
        return value;
    }

}

class SmtpAuthenticator extends javax.mail.Authenticator {
    private String userName;
    private String password;

    public SmtpAuthenticator(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
        return new javax.mail.PasswordAuthentication(userName, password);
    }
}

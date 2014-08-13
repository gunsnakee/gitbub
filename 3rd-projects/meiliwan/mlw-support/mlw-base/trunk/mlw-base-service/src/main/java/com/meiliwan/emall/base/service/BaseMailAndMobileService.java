package com.meiliwan.emall.base.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.meiliwan.emall.base.constant.Constants;
import com.meiliwan.emall.base.dto.MailEntity;
import com.meiliwan.emall.base.util.DmdeliverUtil;
import com.meiliwan.emall.base.util.MailUtils;
import com.meiliwan.emall.base.util.MessageUtils;
import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.ConfigOnZk;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

/**
 * 通过邮箱或者手机发送信息
 */
@Service
public class BaseMailAndMobileService extends DefaultBaseServiceImpl  {


    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());


    private static final String CID_LOGO = "MLW-LOGO";
    private static final String CID_PROMISE = "MLW-PROMISE";
    
    private static final String ZK_CONFIG = "commons/mail.properties";


    private static Configuration configuration = new Configuration();

    static {
        configuration.setClassForTemplateLoading(BaseMailAndMobileService.class,"/conf/mail");
    }

    /**
     * 通用短信发送
     * @param resultObj
     * @param mobileNum  手机号码
     * @param msgContent  短信内容
     * @param cacheMap  缓存数据
     * @param cacheId   缓存数据ID
     * @param jedisKey  缓存Key
     * @throws com.meiliwan.emall.commons.exception.BaseException
     * @throws java.io.IOException
     */
    public void sendMobile(JsonObject resultObj,String mobileNum,String msgContent,Map<String,Object> cacheMap,String cacheId,JedisKey jedisKey)  {
        if(cacheMap!=null && jedisKey!=null && StringUtils.isNotBlank(cacheId)){

            try {
                ShardJedisTool.getInstance().hmset(jedisKey,cacheId,cacheMap);
            } catch (JedisClientException e) {
                Map<String,Object> para = new HashMap<String, Object>();
                para.put("cacheMap",cacheMap);
                para.put("cacheId",cacheId);
                para.put("jedisKey",jedisKey.name());
                throw  new ServiceException("base-"+this.getClass().getSimpleName()+".sendMail:{}",new Gson().toJson(para),e);
            }
        }

        boolean b = false;
        try {
            b= MessageUtils.sendMessage(mobileNum, msgContent) == Constants.MOBILE_SUCESS_CODE ? true : false;
        } catch (Exception e) {
            Map<String,Object> para = new HashMap<String, Object>();
            para.put("mobileNum",mobileNum);
            para.put("msgContent",msgContent);
            throw  new ServiceException("base-"+this.getClass().getSimpleName()+".sendMobile:{}",new Gson().toJson(para),e);
        }

        addToResult(b ,resultObj);
    }



    /**
     * 通用邮件发送
     * @param resultObj
     * @param mailEntity  邮件参数
     * @param cacheMap 缓存数据
     * @param cacheId   缓存数据ID
     * @param jedisKey   缓存Key
     * @throws com.meiliwan.emall.commons.exception.JedisClientException
     */
	public void sendMail(JsonObject resultObj,MailEntity mailEntity,Map<String,Object> cacheMap,String cacheId,JedisKey jedisKey)  {
        addToResult(sendMailBase(mailEntity,cacheMap,cacheId,jedisKey), resultObj);
    }
	
	
	/**
     * @param josJsonObject
     * @param mailEntity  邮件参数
     * @param cacheMap 缓存数据
     * @param cacheId   缓存数据ID
     * @param jedisKey   缓存Key
     * @param filePath	发送的附件
     * @param delAfterHandle 发送的附件文件
	 * */
	public void sendMailWithAttachment(JsonObject josJsonObject,File[] files,MailEntity mailEntity,Map<String,Object> cacheMap,
    		String cacheId,JedisKey jedisKey){
		
	    if(cacheMap!=null && jedisKey!=null && StringUtils.isNotBlank(cacheId)){
            try {
                ShardJedisTool.getInstance().hmset(jedisKey,cacheId,cacheMap);
            } catch (JedisClientException e) {
                Map<String,Object> para = new HashMap<String, Object>();
                para.put("cacheMap",cacheMap);
                para.put("cacheId",cacheId);
                para.put("jedisKey",jedisKey.name());
                throw  new ServiceException("base-"+this.getClass().getSimpleName()+".sendMail-ShardJedisTool.hmset:{}",new Gson().toJson(para),e);
            }
        }

        try {
        	String[] filePaths = new String[files.length];
        	
        	for (int i = 0; i < files.length; i++) {
				filePaths[i] = files[i].getAbsolutePath();
			}

            addToResult(MailUtils.sendMail(mailEntity, filePaths), josJsonObject);
        } catch (Exception e) {
            throw  new ServiceException("base-"+this.getClass().getSimpleName()+".sendMail:{}",new Gson().toJson(mailEntity),e);
        }
	}
	
	/**
	 * 
	 * @param resultObj
	 * @param to
	 * @param fieldValue
	 * @param emailId
	 */
	public void sendMailByDM(JsonObject resultObj, String to,
			Map<String, Object> fieldValue, int emailId) {
		boolean success = false;
		try {
			String campaignID = ConfigOnZk.getInstance().getValue(ZK_CONFIG,
					"dmdelivery.common.campaignID", "1");
			String groupID = ConfigOnZk.getInstance().getValue(ZK_CONFIG,
					"dmdelivery.common.groupIDs", "82");
			success = DmdeliverUtil.sendMail(to, fieldValue, Integer.valueOf(campaignID),
					emailId, new int[] { Integer.valueOf(groupID) });
		} catch (BaseException e) {
			logger.error(e, "send mail failue, fieldValue(" + fieldValue
					+ "),emailId(" + emailId + ")", null);
		}

		addToResult(success, resultObj);
	}
	
	/**
	 * 
	 * @param resultObj
	 * @param to
	 * @param fieldValue
	 * @param emailId
	 * @param actId 活动id
	 * @param groupIDs
	 */
	public void sendMailByDMWithGroupAndActId(JsonObject resultObj, String to,
			Map<String, Object> fieldValue, int emailId, int actId, int[] groupIDs) {
		boolean success = false;
			success = DmdeliverUtil.sendMail(to, fieldValue, actId,
					emailId, groupIDs);

		addToResult(success, resultObj);
	}
	


    /**
     * 重置登录密码的邮件发送
     *
     * @param resultObj
     * @param mailEntity
     */
    @Deprecated
    public void sendMailResetPwd(JsonObject resultObj,Map<String,Object> dataMap,MailEntity mailEntity)  {
        mailEntity.setUrlDataSources(createImageDataSources());
        addToResult(sendMailUseFtl("resetLoginPassword.ftl", dataMap, mailEntity, null, null, null), resultObj);
    }

    /**
     * 找回登录密码的邮件发送
     *
     * @param resultObj
     * @param mailEntity
     * @param cacheMap
     * @param cacheId
     * @param jedisKey
     */
    @Deprecated
    public void sendMailFindPwd(JsonObject resultObj,Map<String,Object> dataMap,MailEntity mailEntity,Map<String,Object> cacheMap,String cacheId,JedisKey jedisKey)  {
        mailEntity.setUrlDataSources(createImageDataSources());
        addToResult(sendMailUseFtl("retrieveLoginPassword.ftl",dataMap,mailEntity,cacheMap,cacheId,jedisKey), resultObj);
    }

    /**
     * 验证身份的邮件发送
     * @param resultObj
     * @param dataMap
     * @param mailEntity
     * @param cacheMap
     * @param cacheId
     * @param jedisKey
     */
    @Deprecated
    public void sendMailVI(JsonObject resultObj,Map<String,Object> dataMap,MailEntity mailEntity,Map<String,Object> cacheMap,String cacheId,JedisKey jedisKey)  {
        mailEntity.setUrlDataSources(createImageDataSources());
        addToResult(sendMailUseFtl("authentication.ftl",dataMap,mailEntity,cacheMap,cacheId,jedisKey), resultObj);
    }

    /**
     * 更改邮箱的邮件发送
     * @param resultObj
     * @param dataMap
     * @param mailEntity
     * @param cacheMap
     * @param cacheId
     * @param jedisKey
     */
    @Deprecated
    public void sendMailChange(JsonObject resultObj,Map<String,Object> dataMap,MailEntity mailEntity,Map<String,Object> cacheMap,String cacheId,JedisKey jedisKey)  {
        mailEntity.setUrlDataSources(createImageDataSources());
        addToResult(sendMailUseFtl("changeEmail.ftl",dataMap,mailEntity,cacheMap,cacheId,jedisKey), resultObj);
    }

    /**
     * 设置支付密码的邮件发送
     * @param resultObj
     * @param dataMap
     * @param mailEntity
     * @param cacheMap
     * @param cacheId
     * @param jedisKey
     */
    @Deprecated
    public void sendMailPayPwd(JsonObject resultObj,Map<String,Object> dataMap,MailEntity mailEntity,Map<String,Object> cacheMap,String cacheId,JedisKey jedisKey)  {
        mailEntity.setUrlDataSources(createImageDataSources());
        addToResult(sendMailUseFtl("setPaymentPassword.ftl",dataMap,mailEntity,cacheMap,cacheId,jedisKey), resultObj);
    }



    private boolean sendMailUseFtl(String ftlName,Map<String,Object> dataMap,MailEntity mailEntity,Map<String,Object> cacheMap,String cacheId,JedisKey jedisKey){
        String cont = getTemlateString(ftlName,dataMap);
        mailEntity.setContent(cont);
        return sendMailBase(mailEntity,cacheMap,cacheId,jedisKey);
    }

    private boolean sendMailBase(MailEntity mailEntity,Map<String,Object> cacheMap,String cacheId,JedisKey jedisKey){
        if(cacheMap!=null && jedisKey!=null && StringUtils.isNotBlank(cacheId)){
            try {
                ShardJedisTool.getInstance().hmset(jedisKey,cacheId,cacheMap);
            } catch (JedisClientException e) {
                Map<String,Object> para = new HashMap<String, Object>();
                para.put("cacheMap",cacheMap);
                para.put("cacheId",cacheId);
                para.put("jedisKey",jedisKey.name());
                throw  new ServiceException("base-"+this.getClass().getSimpleName()+".sendMail-ShardJedisTool.hmset:{}",new Gson().toJson(para),e);
            }
        }

        try {
            return MailUtils.sendMail(mailEntity);
        } catch (Exception e) {
            throw  new ServiceException("base-"+this.getClass().getSimpleName()+".sendMail:{}",new Gson().toJson(mailEntity),e);
        }
    }


    private String getTemlateString(String ftlName,Map<String,Object> model){
        StringWriter stringWriter = null;
        try {
            Template t = configuration.getTemplate(ftlName);
            stringWriter = new StringWriter();
            t.process(model, stringWriter);
            return stringWriter==null? null : stringWriter.toString();
        } catch (IOException e) {
            throw new ServiceException("service-" + this.getClass().getSimpleName() + ".getTemlateString: {}", ftlName, e);
        } catch (TemplateException e) {
            throw new ServiceException("service-" + this.getClass().getSimpleName() + ".getTemlateString: {}", ftlName, e);
        }finally {
            if(stringWriter!=null){
                stringWriter.flush();
                try {
                    stringWriter.close();
                } catch (IOException e) {
                    logger.error(e,ftlName,null);
                }
            }
        }

    }


    private List<Map<String,String>> createImageDataSources(){
        List<Map<String,String>> images = new ArrayList<Map<String, String>>();
        Map<String,String> imageMap = new HashMap<String, String>();
        imageMap.put("cid",CID_LOGO);
        imageMap.put("url","http://www.meiliwan.com/images/logo.jpg?d="+System.currentTimeMillis());
        images.add(imageMap);
        imageMap = new HashMap<String, String>();
        imageMap.put("cid",CID_PROMISE);
        String url = "http://www.meiliwan.com/images/img_promise.png?d="+System.currentTimeMillis();
        imageMap.put("url",url);
        logger.debug("邮件发送图片URL",url,"");
        images.add(imageMap);
        return images;
    }



}

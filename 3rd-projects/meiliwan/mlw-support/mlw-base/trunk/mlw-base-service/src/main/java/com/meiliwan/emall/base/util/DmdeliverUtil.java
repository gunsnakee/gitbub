package com.meiliwan.emall.base.util;

import com.meiliwan.emall.base.util.dmdelivery.*;
import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.ConfigOnZk;

import java.util.Map;
import java.util.Set;

/**
 * Dmdeliver邮件发送工具类
 *
 *
 * User: jiawuwu
 * Date: 13-11-27
 * Time: 下午5:53
 * To change this template use File | Settings | File Templates.
 */
public class DmdeliverUtil {

    private static final MLWLogger LOGGER = MLWLoggerFactory.getLogger(DmdeliverUtil.class);
    
    private static final String ZK_CONFIG = "commons/mail.properties";
    private static String authUserName = "admin";
    private static String authPasswd = "Rbgrsuejf0(";

    private DmdeliverUtil(){

    }
    
    static {
    	try {
    		authUserName = ConfigOnZk.getInstance().getValue(ZK_CONFIG, "dmdelivery.auth.username");
    		authPasswd = ConfigOnZk.getInstance().getValue(ZK_CONFIG, "dmdelivery.auth.password");
		} catch (BaseException e) {
			LOGGER.error(e, "get dmdelivery config info failure", null);
		}
    	
    }


    private static boolean sendMail(String to,Map<String,Object> fieldValue,int campaignID,int emailId,int[] groupIDInts,NewRecipientType newrecip,boolean addDuplisToGroups,boolean overwrite){

        boolean rs = false;

        DMdeliverySoapAPI api = new DMdeliverySoapAPI();
        DMdeliverySoapAPIPort service  =  api.getDMdeliverySoapAPIPort();
        
        DMdeliveryLoginType login = new DMdeliveryLoginType();
        login.setUsername(authUserName);
        login.setPassword(authPasswd);

        ArrayOfIntType groupIDs = new ArrayOfIntType();
        for(int groupIDInt : groupIDInts){
            groupIDs.getInt().add(groupIDInt);
        }

        if(newrecip==null){
            newrecip = new NewRecipientType();
        }

        //field1 email
        RecipientNameValuePairType datavalue = new RecipientNameValuePairType();
        datavalue.setName("email");                         //email field
        datavalue.setValue(to);
        newrecip.getFields().add(datavalue);

        if(fieldValue!=null){
            Set<Map.Entry<String,Object>> entries = fieldValue.entrySet();
            for(Map.Entry<String,Object> entry:entries){
                datavalue = new RecipientNameValuePairType();
                datavalue.setName(entry.getKey());
                datavalue.setValue(entry.getValue()+"");
                newrecip.getFields().add(datavalue);
            }
        }


        RecordResultType result = service.addRecipient(
                login,      //username and password
                campaignID,     //campaign
                groupIDs,       //group
                newrecip,       //data for upload
                addDuplisToGroups,
                overwrite //overwrite
        );


        int recipientID=0;

        if( "OK".equals(result.getStatus()) || "DUPLICATE".equals(result.getStatus()) ){

            recipientID = result.getId();

            rs = service.sendSingleMailing(
                    login,
                    campaignID,
                    emailId,  //email id
                    recipientID
            );

        }else{
            LOGGER.warn("邮件发送失败",to+": "+result.getStatus()+" "+result.getStatusMsg(),null);
        }

        if(rs){
            LOGGER.info("mail to: "+to,to+" {campaignID:"+campaignID+",emailId:"+emailId+",recipientID:"+recipientID+"}",null);
        }else{
            LOGGER.warn("mail to: "+to,to+" {resultState:"+result.getStatus()+"resultMsg:"+result.getStatusMsg()+",campaignID:"+campaignID+",emailId:"+emailId+",recipientID:"+recipientID+"}",null);
        }

        return rs;

    }

    /**
     * 邮件发送
     *
     * @param to 收件人
     * @param fieldValue 邮件内容键值对
     * @param campaignID 活动ID
     * @param emailId 邮件ID
     * @param groupIDInts 组ID
     * @return
     */
    public static boolean sendMail(String to,Map<String,Object> fieldValue,int campaignID,int emailId,int[] groupIDInts){
        return sendMail(to,fieldValue,campaignID,emailId,groupIDInts,null,true,true);
    }


}

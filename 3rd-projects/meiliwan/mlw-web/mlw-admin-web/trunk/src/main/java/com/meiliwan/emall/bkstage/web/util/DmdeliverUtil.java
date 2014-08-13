package com.meiliwan.emall.bkstage.web.util;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dmdelivery.webservice.ArrayOfIntType;
import com.dmdelivery.webservice.CampaignArrayType;
import com.dmdelivery.webservice.CampaignType;
import com.dmdelivery.webservice.DMdeliveryLoginType;
import com.dmdelivery.webservice.DMdeliverySoapAPI;
import com.dmdelivery.webservice.DMdeliverySoapAPIPort;
import com.dmdelivery.webservice.NewRecipientType;
import com.dmdelivery.webservice.RecipientNameValuePairType;
import com.dmdelivery.webservice.RecordResultType;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;

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
	private static DMdeliveryLoginType login = new DMdeliveryLoginType();
	static{
		login.setUsername("service");
		login.setPassword("Loslvli2|");
	}
    private DmdeliverUtil(){

    }

    public static CampaignArrayType getCampaigns(){
    		DMdeliverySoapAPI api = new DMdeliverySoapAPI();
        DMdeliverySoapAPIPort service  =  api.getDMdeliverySoapAPIPort();
        
        CampaignArrayType type = service.getCampaigns(login);
        return type;
        
    }
    private static boolean sendMail(String to,Map<String,Object> fieldValue,int campaignID,int emailId,int[] groupIDInts,NewRecipientType newrecip,boolean addDuplisToGroups,boolean overwrite){

        boolean rs = false;

        DMdeliverySoapAPI api = new DMdeliverySoapAPI();
        DMdeliverySoapAPIPort service  =  api.getDMdeliverySoapAPIPort();
        

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
        try { 
			return sendMail(to,fieldValue,campaignID,emailId,groupIDInts,null,true,true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error(e, to+fieldValue+campaignID+emailId+groupIDInts, null);
			return false;
		}
    }


    public static void main(String[] args) {
//	    	Map<String,Object> fieldValue = new HashMap<String,Object>();
//	    	fieldValue.put("content", "asdfasdfafwe");
//	    	int[] group = new int[]{81};
//	    	
//	    	boolean result = sendMail("yinggao.zhuo@opi-corp.com",fieldValue,1,11,group);
//	    	System.out.println(result);
	    	 	CampaignArrayType type = getCampaigns();
	    	 	List<CampaignType> list = type.getCampaign();
	    	 	for (CampaignType campaignType : list) {
	    	 		System.out.println(campaignType.getId());
	    	 		System.out.println(campaignType.getName());
			}
	   
	}
    
}

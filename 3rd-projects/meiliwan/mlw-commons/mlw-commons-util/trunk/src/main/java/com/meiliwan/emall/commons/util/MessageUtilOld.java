package com.meiliwan.emall.commons.util;


import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 手机短信处理公共类,确定常量配置之后，常量再抽出来 User: guangde.tang Date: 13-5-20 Time: 下午1:46
 */
public class MessageUtilOld {

	private static final MLWLogger LOGGER = MLWLoggerFactory.getLogger(MessageUtilOld.class);

	private static final String PROPERTIES_ZK = "commons/message.properties";

    private MessageUtilOld(){

    }
	
	
	public static int sendMessage(String phoneNum, String messageContent,String extraMsg)
			throws IOException, BaseException {
		// 参数检验
		if (StringUtils.isEmpty(phoneNum) || !StringUtil.checkPhone(phoneNum)){
            // send person is null
            return 603;
        }


		if (StringUtils.isEmpty(messageContent)|| messageContent.length() > 300){
            // message null or messages to long
            return 601;
        }

        messageContent = messageContent.trim() + (extraMsg==null?"":extraMsg);

		// post请求
		String url = ConfigOnZk.getInstance().getValue(PROPERTIES_ZK, "message.url");

		List<NameValuePair> postData = new ArrayList<NameValuePair>(4);
		
		String paramNameUser = ConfigOnZk.getInstance().getValue(PROPERTIES_ZK, "message.url.param.user");
		String paramValueUser = ConfigOnZk.getInstance().getValue(PROPERTIES_ZK, "message.user.name");
		String paramNamePw = ConfigOnZk.getInstance().getValue(PROPERTIES_ZK, "message.url.param.password");
		String paramValuePw = ConfigOnZk.getInstance().getValue(PROPERTIES_ZK, "message.user.password");

		postData.add( new BasicNameValuePair(paramNameUser, paramValueUser));
		postData.add( new BasicNameValuePair(paramNamePw, paramValuePw));
		postData.add( new BasicNameValuePair("mb", phoneNum));
		postData.add( new BasicNameValuePair("ms", messageContent));
		
		UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(postData, "gbk");
		BufferedReader br = new BufferedReader(new InputStreamReader(uefEntity.getContent()));
		String parameters = br.readLine();
		
		int status = 604;// send fail
		String statusStr;
		// 返回结果int
		try {
			
			statusStr = HttpClientUtil.get().doGet(url + "?" + parameters);
			System.out.println("\n\n\n" + statusStr + "\n\n\n");
			status = parseReturnStatus(statusStr);
			LOGGER.info("send message : " + phoneNum,"send message : " + phoneNum,null);
			return status;
		} catch (IOException e) {
			LOGGER.error(e, url + "?" + parameters, null);
			return 604;
		} 
	}

	private static int parseReturnStatus(String paramStatus) {
		int status = -99;
		if (paramStatus.contains(",")) {
			status = Integer.parseInt(paramStatus.split(",")[0]);
		}
		if (0 == status)
            // send success
			return 605;
		if (-4 == status || -3 == status)
            // message to long
			return 601;
		if (-1 == status)
            //userName or pwd is null
			return 602;
        // send fail
		return 604;
	}
}

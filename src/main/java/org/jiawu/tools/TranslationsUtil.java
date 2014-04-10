package org.jiawu.tools;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * language tranlate util 
 * @author fuyangfan
 * @date 20130527
 */
public class TranslationsUtil {

	private static final Log LOG = LogFactory.getLog(TranslationsUtil.class);
	
	private static final String TYPE_TRANS_GOOGLE_HTTP = "GOOGLE";
	private static final String TYPE_TRANS_BING_HTTP = "BING";
	
	// bing api translate parameters
	private static final String tokenUrl = "https://datamarket.accesscontrol.windows.net/v2/OAuth2-13/";
	private static final String scopeUrl = "http://api.microsofttranslator.com";
	private static final String clientId = "ec89c602-d3cc-4b47-8530-b4a1f3df3777";
	private static final String clientSecret = "WUBSVZ/7eWjT1Ny0kkGsHvcwzV95m/uAtP1Rd91XYwE";
	private static final String grantType = "client_credentials";
	private static final String BING_TRANSLATE_API_URL = "http://api.microsofttranslator.com/V2/ajax.svc/Translate?";
	
	// http request url
	private static final String GOOGLE_TRANSLATE_HTTP_URL = "http://translate.google.com/translate_a/t";
	private static final String BING_TRANSLATE_HTTP_URL = "http://api.microsofttranslator.com/v2/ajax.svc/Translate";
	private static final String BING_APP_ID_HTTP_URL = "http://www.bing.com/translator/";
	
	/**
	 * translate function
	 * @param srcText
	 * @param targetLanguage
	 * @return
	 */
	public static String translate(String srcText, String targetLanguage) throws IOException {
		String result = "";
		
//		try {
//			// source encode by utf-8
//			srcText = URLEncoder.encode(srcText, "utf-8");
//		} catch (UnsupportedEncodingException e) {
//			LOG.error("string encode utf-8 failure : " + e.getMessage());
//		}
		
		if (StringUtils.isEmpty(srcText)){
			return result;
		}
		
		// target language default en
		if (StringUtils.isEmpty(targetLanguage)){
			targetLanguage = "en";
		}
		
		// retry flag
		boolean isRetry = true;
		// retry count
		int retryCount = 0;
		
		while(isRetry){
			// request times increament 1
			retryCount++;
			isRetry = false;
			// request not beyond 3 times
			if (retryCount > 3){
				LOG.info("RETRY CONNECTION BEYOND " +retryCount+ "TIMES");
				return result; 
			}


            result = translateGOOGLE(srcText, targetLanguage);
			
			// when result is null, need retry
			if (StringUtils.isEmpty(result)){
				isRetry = true;
			}
			else{
				// end while
				break;
			}
			
			// before retry,thread pause 500ms
			if (isRetry){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					LOG.error("Thread sleep interrupted exception：" + e.getMessage(),e);
				}
			}
		}

		if(!"zh".equals(targetLanguage)){
			result =BCConvert.qj2bj(result);
		}
		return result;
	}
	
	/**
	 * google translate function
	 * @param srcText
	 * @param targetLanguage
	 * @return
	 */
	private static String translateGOOGLE(String srcText, String targetLanguage) throws IOException {

        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("client", "t"));
        formparams.add(new BasicNameValuePair("sl", "auto"));
        formparams.add(new BasicNameValuePair("ie", "UTF-8"));
        formparams.add(new BasicNameValuePair("oe", "UTF-8"));
        formparams.add(new BasicNameValuePair("multires", "1"));
        formparams.add(new BasicNameValuePair("tl", targetLanguage));
        formparams.add(new BasicNameValuePair("q", srcText));

		return executeHttpPostMethod(GOOGLE_TRANSLATE_HTTP_URL, formparams);
		
		
	}
	

	

	private static String executeHttpPostMethod(String translateUrl, List<NameValuePair> formparams) throws IOException {
		    String result =null;

            CloseableHttpClient client = HttpClients.createDefault();
			// http post method
            HttpPost httpPost = new HttpPost(translateUrl);

           UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
            httpPost.setEntity(entity);

			// execute
            CloseableHttpResponse response = client.execute(httpPost);


            try {
                HttpEntity entity1 = response.getEntity();
                result=EntityUtils.toString(entity1);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                response.close();
            }


        if(StringUtils.isNotBlank(result)){
            JsonParser p = new JsonParser();
            JsonElement element = p.parse(result);
            JsonArray array = 	element.getAsJsonArray();
            System.out.println(array.get(0).getAsJsonArray().get(0).getAsJsonArray().get(0).getAsString());
            result =  array.get(0).getAsJsonArray().get(0).getAsJsonArray().get(0).getAsJsonPrimitive().getAsString();
        }


		
		return result;
	}
	

	
	public static void main(String[] args) throws IOException {
		String tr = "泰国新年抽奖活动页面测试已经完成，包括功能测试，页面兼容测试以及抽奖概率统计测试";
		//System.out.println("1."+tr);
		String trResult =TranslationsUtil.translate(tr, "en");
		System.out.println(trResult);

		tr = " sdasd速度苏打，速度苏打，，，，，，！！！！！！！！！！！！！！！！！！！！！！!!!!!!!!!!!!!!!!!!! ";
		//System.out.println("3."+BCConvert.qj2bj(tr));
		
		//System.out.println("4."+BCConvert.bj2qj(tr));
	}
	
}

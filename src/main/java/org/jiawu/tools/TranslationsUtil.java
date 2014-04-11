package org.jiawu.tools;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.*;

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
	private static final String BING_TRANSLATE_HTTP_URL = "http://api.microsofttranslator.com/V2/Http.svc/Translate";
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


    private static class TranslationsTask implements Callable<String[]>{
        private String srcText;
        private String targetLanguage;
        public TranslationsTask(String srcText,String targetLanguage){
            this.srcText = srcText;
            this.targetLanguage =  targetLanguage;
        }

        @Override
        public String[] call() throws Exception {
            String[] trans = new String[3];
            trans[0] = targetLanguage;
            trans[1] = srcText;
            trans[2] = translate(srcText,targetLanguage);
            return trans;
        }
    }


    public static Map<String,String> translate(Set<String> srcTexts,String targetLanguage,int poolSize){
        poolSize = poolSize<1 ? 1: poolSize;
        Map<String,String> trans = new HashMap<String, String>();
        ExecutorService pool = Executors.newFixedThreadPool(poolSize);
        int size = srcTexts.size();
        List<Future<String[]>> futures = new ArrayList<Future<String[]>>(size);
        for(String srcText:srcTexts){
            futures.add(pool.submit(new TranslationsTask(srcText,targetLanguage)));
        }
        for(Future<String[]> future:futures){
            String[] transArr;
            try {
                transArr = future.get();
                trans.put(transArr[1],transArr[2]);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        pool.shutdown();
        return trans;
    }

    public static Map<String,String> translate(Set<String> srcTexts,String targetLanguage){
        return  translate(srcTexts,targetLanguage,10);
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

		String result =  executeHttpPostMethod(GOOGLE_TRANSLATE_HTTP_URL, formparams);

        if(StringUtils.isNotBlank(result)){
            JsonParser p = new JsonParser();
            JsonElement element = p.parse(result);
            JsonArray array = 	element.getAsJsonArray();
            System.out.println(array.get(0).getAsJsonArray().get(0).getAsJsonArray().get(0).getAsString());
            result =  array.get(0).getAsJsonArray().get(0).getAsJsonArray().get(0).getAsJsonPrimitive().getAsString();
        }

        return result;

    }


    /**
     * bing translate function
     * @param srcText
     * @param targetLanguage
     * @return
     */
    public static String translateBING(String srcText, String targetLanguage) throws IOException {
        // language code convert
        if (targetLanguage.startsWith("zh-CN")){
            targetLanguage = "zh-CHS";
        }
        else if (targetLanguage.startsWith("zh-TW")){
            targetLanguage = "zh-CHT";
        } else if (targetLanguage.startsWith("zh")){
            targetLanguage = "zh-CHS";
        }

        String appId = null;


        try{
            CloseableHttpClient client = HttpClients.createDefault();
            // http post method
            HttpPost httpPost = new HttpPost(BING_APP_ID_HTTP_URL);
            CloseableHttpResponse response = client.execute(httpPost);


          BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String str = "";
            while((str = reader.readLine())!=null){
                // parser app id
                if (str.startsWith("Default.Constants.AjaxApiAppId")){
                    appId = str.substring(str.indexOf("'") + 1, str.length() - 2);
                }
            }
            reader.close();
        }catch (Exception e){
            LOG.error("fecth translate app id error : " + e.getMessage(),e);
        }

        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("appId",appId));
        formparams.add(new BasicNameValuePair("to",targetLanguage));
        formparams.add(new BasicNameValuePair("text",srcText));
        formparams.add(new BasicNameValuePair("contentType","text/html"));
        formparams.add(new BasicNameValuePair("maxTranslations","1"));

        String s = executeHttpPostMethod(BING_TRANSLATE_HTTP_URL, formparams);
        System.out.println(s);

        return null;

    }


    private static String translateByBingAPI(String srcText, String targetLanguage) throws IOException {

        String result = null;



        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("grant_type",grantType));
        nameValuePairs.add(new BasicNameValuePair("scope",scopeUrl));
        nameValuePairs.add(new BasicNameValuePair("client_id",clientId));
        nameValuePairs.add(new BasicNameValuePair("client_secret",clientSecret));

        String tokenResponse = executeHttpPostMethod(tokenUrl, nameValuePairs);
        Map<String, String> map = new Gson().fromJson(tokenResponse, new TypeToken<Map<String, String>>(){}.getType());
        String tokenAccess = map.get("access_token");



        // language code convert
        if (targetLanguage.startsWith("zh-CN")){
            targetLanguage = "zh-CHS";
        }
        else if (targetLanguage.startsWith("zh-TW")){
            targetLanguage = "zh-CHT";
        }

        // translate url
        StringBuffer translationUrlBuff = new StringBuffer(BING_TRANSLATE_API_URL);
        try {
            // set parameters
            String params = "appId=&contentType=text/plain&to="+ targetLanguage + "&text=" + URLEncoder.encode(srcText, "utf-8");
            translationUrlBuff.append(params);
            HttpPost method = new HttpPost(translationUrlBuff.toString());
            method.setHeader("Authorization", "Bearer "+tokenAccess);
            CloseableHttpClient client = HttpClients.createDefault();
            // execute
            CloseableHttpResponse response = client.execute(method);

            result = new String(response.getEntity().getContent().toString()).replaceAll("\"", "");
        } catch (IOException e) {
            LOG.error("TRANSLATE HTTP IOEXCEPTION ERROR:" + e.getMessage(),e);
        }
        return result;
    }


	

	

	private static String executeHttpPostMethod(String translateUrl, List<NameValuePair> formparams) throws IOException {
		    String result =null;

            CloseableHttpClient client = HttpClients.createDefault();
			// http post method
            HttpPost httpPost = new HttpPost(translateUrl);

           if(formparams!=null && formparams.size()>0){
               UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
               httpPost.setEntity(entity);
           }



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

        System.out.println(result);



		
		return result;
	}

    private static String executeHttpGetMethod(String translateUrl,List<NameValuePair> formparams) throws IOException {
        String result= null;
        CloseableHttpClient client = HttpClients.createDefault();
        String lasturl = translateUrl;

        int size = 0;
        int idx = 0;
        if(formparams!=null && (size=formparams.size())>0){
            String[] nameValues = new String[size];
            for(NameValuePair nameValuePair:formparams){
                nameValues[idx++]=nameValuePair.getName()+"="+nameValuePair.getValue();
            }
            lasturl=lasturl+"?"+StringUtils.join(nameValues,'&');
        }

        HttpGet httpGet = new HttpGet(lasturl);
        // execute
        CloseableHttpResponse response = client.execute(httpGet);


        try {
            HttpEntity entity1 = response.getEntity();
            result=EntityUtils.toString(entity1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            response.close();
        }

        System.out.println(result);


        return result;
    }
	

	
	public static void main(String[] args) throws IOException {
		String tr = "PE管";
		//System.out.println("1."+tr);
		String trResult =TranslationsUtil.translateByBingAPI(tr, "en");
		System.out.println(trResult);
	}
	
}

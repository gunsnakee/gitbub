package com.meiliwan.emall.imeiliwan.util;

import com.google.gson.Gson;
import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.ConfigOnZk;
import com.meiliwan.emall.commons.util.HttpClientUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: yyluo
 * Date: 14-5-9
 * Time: 上午11:52
 * 微信安全工具类
 */
public class WXSafeTool {
    private static final MLWLogger LOG = MLWLoggerFactory.getLogger(WXSafeTool.class);
    public final static String AUTHORIZE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize"; //授权url
    public final static String GET_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token"; //获取access_token url
    public final static String GET_USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo" ; //拉取用户信息的url
    public final static String BANDING_URL = "http://m.meiliwan.com/html5/html/binding" ; //微信绑定url
    public final static String TO_ORDLIST_RESP_URL = "http://m.meiliwan.com/html5/html/toOrdList";// 从微信对话框连接进入订单列表URL
    public final static String MLW_M_URL = "http://m.meiliwan.com";// 美丽湾h5主站
    public final static String GRANT_TYPE = "authorization_code" ; //授权类型

    /**
     * 验证微信请求消息真实性
     * @return
     */
    public static boolean isWxRequest(HttpServletRequest request,String token){
        //获取微信服务器发送给我们的四个参数
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String openId = request.getParameter("openid");
        boolean isOk = false;
        try{
            //验证URL有效性
            if(StringUtils.isNotBlank(signature) && StringUtils.isNotBlank(timestamp)
                    && StringUtils.isNotBlank(nonce) && StringUtils.isNotBlank(token)){
                isOk = VerifyUtils.checkSignature(token, signature, timestamp, nonce);
            }
        }catch(Exception e){
            Map errorMap = new HashMap();
            errorMap.put("errorMsg","微信：验证消息真实性异常！");
            errorMap.put("signature",signature);
            errorMap.put("timestamp",timestamp);
            errorMap.put("nonce",nonce);
            LOG.error(e,errorMap, WebUtils.getIpAddr(request));

        }
        return isOk;
    }

    /**
     * 获取安全绑定url
     * @param openId
     * @return
     */
    public static String getSafeBandingUrl(String openId){
        Long timestamp = new Date().getTime(); //时间戳
        String nonce = getRandomString(8);   //随机数
        String signature = setSignature(openId,timestamp+"",nonce) ;
        String safeBandingUrl = BANDING_URL +"?signature="+signature;
        System.out.println("safeBandingUrl="+safeBandingUrl);
        return  safeBandingUrl;
    }

    /**
     * 判断是否是安全绑定请求
     * @param openId
     * @param signature  请求带过来的signature
     * @return
     */
    public static boolean isSafeBanding(String openId,String signature){
        boolean isSafe = false;
        if(StringUtils.isNotBlank(openId) && StringUtils.isNotBlank(signature)){
            String cacheSignature = getSignature(openId);
            if(signature.equals(cacheSignature)){
                isSafe = true;
            }
        }
        return isSafe;
    }
    /**
     * 生成绑定请求签名
     * 往缓存中设置签名，防止绑定连接暴露后，直接通过连接来做绑定
     * @param openId    微信openid
     * @param timestamp  时间戳
     * @param nonce   八位随机数
     * @return
     */
    public static String setSignature (String openId, String timestamp, String nonce){
        //str用于存储加密后的字符串
        String signature = null;

        if(StringUtils.isNotBlank(openId) && StringUtils.isNotBlank(timestamp) && StringUtils.isNotBlank(nonce)){
            //将token timestamp nonce 按照字典顺序排序
            String[] params = new String[]{openId, timestamp, nonce};
            Arrays.sort(params);

            //将上面三个参数排序之后拼接成字符串
            StringBuffer buffer = new StringBuffer();
            for(int i = 0; i < params.length; i ++){
                buffer.append(params[i]);
            }
            try {
                //获取sha1加密对象
                MessageDigest digest = MessageDigest.getInstance("SHA-1");
                //将组合后的字符串使用sha1加密, 加密后获得一个byte数组
                byte[] byteDigest = digest.digest(buffer.toString().getBytes());
                //获取加密后的字符串, 将byte数组转化为字符串
                signature = VerifyUtils.byte2str(byteDigest);

                //往缓存中加绑定安全签名有效期 10分钟。
                try {
                    ShardJedisTool jedisTool = ShardJedisTool.getInstance();
                    jedisTool.set(JedisKey.wx$bindingSignature,openId,signature);
                    System.out.println("signature="+signature);
                } catch (JedisClientException e) {
                    Map map = new HashMap();
                    map.put("openId",openId);
                    map.put("signature",signature);
                    LOG.error(e, map, null);
                }
            } catch (Exception e) {
                Map map = new HashMap();
                map.put("openId",openId);
                map.put("timestamp",timestamp);
                map.put("nonce",nonce);
                LOG.error(e, map, null);
            }
        }
        return signature;
    }

    /**
     * 根据openid获取缓存中的signature
     * @param openId
     * @return
     */
    public static String getSignature (String openId){
        String signature = null;
        try{
            ShardJedisTool jedisTool = ShardJedisTool.getInstance();
            signature = jedisTool.get(JedisKey.wx$bindingSignature,openId);
        }catch (Exception e){
            LOG.error(e,openId,null);
        }
        return signature;
    }

    /**
     * 获取随机字符
     */
    // 获取随机字符串
    public static String getRandomString(int length) { // length 字符串长度
        StringBuffer buffer = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        StringBuffer sb = new StringBuffer();
        Random r = new Random();
        int range = buffer.length();
        for (int i = 0; i < length; i ++) {
            sb.append(buffer.charAt(r.nextInt(range)));
        }
        return sb.toString();
    }


    /**
     * 从zk获取微信相关参数
     * @return
     */
    public static String getWxParam(String key){
        String param = null;
        if(StringUtils.isNotBlank(key)){
            try {
                param = ConfigOnZk.getInstance().getValue("weixin-web/system.properties",key);
            } catch (BaseException e) {
                LOG.error(e,"从zk获取微信"+key+"异常！",null);
            }
        }
        return  param;
    }

    /**
     * 获取accessToken
     */
    public static String getAccessToken() {
        String appId = WXSafeTool.getWxParam("weixin.appId");
        String appSecret = WXSafeTool.getWxParam("weixin.appSecret");

        String accessToken = "";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("appid", appId);
        params.put("secret", appSecret);
        params.put("grant_type","client_credential");
        try{
            String result = HttpClientUtil.getPostBody("https://api.weixin.qq.com/cgi-bin/token", params) ;
            if(StringUtils.isNotBlank(result)){
                Gson gson = new Gson();
                Map resultMap = gson.fromJson(result,HashMap.class);
                if(resultMap.get("access_token")!=null ){
                    return resultMap.get("access_token").toString();
                }
            }
        }catch(Exception e){
            LOG.error(e,"java 获取access_token异常",null);
        }
        return null;
    }


}


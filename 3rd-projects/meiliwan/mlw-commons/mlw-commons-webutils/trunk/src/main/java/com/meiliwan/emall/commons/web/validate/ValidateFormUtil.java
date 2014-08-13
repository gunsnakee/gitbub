package com.meiliwan.emall.commons.web.validate;

/**
 * Created by yuxiong on 13-6-20.
 */
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meiliwan.emall.commons.bean.ValidateItem;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.ConfigOnZk;
import com.meiliwan.emall.commons.util.EncryptTools;
import com.meiliwan.emall.commons.web.CookieSessionUtil;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ValidateFormUtil
{
    private static final MLWLogger LOG = MLWLoggerFactory.getLogger(ValidateFormUtil.class);

    private static final String REDIS_RANDCODE = "redisRandCode";

    /**
     * 校验重复提交
     * @param request
     * @return
     */
    public static boolean validateRepeatSubmit(HttpServletRequest request, HttpServletResponse response) throws JedisClientException {
        //该次提交中是否有formId
        String currentFormId = request.getParameter(FormConstans.REQUEST_CURR_FORMID);
        String fmToken = request.getParameter(FormConstans.REQUEST_FORM_TOKEN);

        if (!StringUtils.isBlank(fmToken)) {
            String tokenKey = CookieSessionUtil.getSession(request, response).getAttribute(CookieSessionUtil.sessionField);
            //这里判断一下是否提交了currentFormId
            if (!StringUtils.isBlank(currentFormId)) {
                //将sessionId+表单的formName拼凑作为redis的key
                tokenKey = tokenKey + currentFormId;
            }

            ShardJedisTool jedis = ShardJedisTool.getInstance();
            //redis里的防止重复提交KEY使用getset操作，获取旧值写入默认1作为新值
            Object uuidObj = jedis.getSet(JedisKey.submit$norepeat, tokenKey, "1");
            LOG.debug("============================tokenKey: "+tokenKey+", currentFormId: "+currentFormId+", uuidObj: "+uuidObj+", fmToken: "+fmToken);
            if (null == uuidObj || !uuidObj.toString().equals(fmToken)) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 校验安全的提交
     * @param request
     * @param enSessFields
     * @param validateItem
     * @return
     */
    public static boolean validateSecuritySubmit(HttpServletRequest request, HttpServletResponse response, String[] enSessFields, ValidateItem validateItem) throws JedisClientException {
        //从request的请求里拿到加密的code
        String reqEnCode = null;


            Object randCodeObj = null;
            StringBuffer sbCode = new StringBuffer();
            if(enSessFields!=null){
                for (String field : enSessFields){

                    //如果session字段用了REDIS_RANDCODE，那么就到redis里去拿randcode
                    if (field.equals(REDIS_RANDCODE)) {
                        reqEnCode = request.getParameter(FormConstans.REQUEST_SECURE_SIGN);
                        String sessionId = CookieSessionUtil.getSession(request, response).getAttribute(CookieSessionUtil.sessionField);
                        //int uid = Integer.parseInt(userId.toString());
                        LOG.debug("===========================sessionId="+sessionId);
                        ShardJedisTool jedis = ShardJedisTool.getInstance();
                        randCodeObj = jedis.get(JedisKey.secure$rand, sessionId);
                //        jedis.del(JedisKey.secure$rand, sessionId);先不要直接删除redis里的randCode， 使用redis自身的缓存有效期删除 modify by yuxiong 2013.9.12                x
                    } else{
                        reqEnCode = request.getParameter(FormConstans.REQUEST_ENCRYPT_CODE);
                        Object obj = CookieSessionUtil.getSession(request, response).getAttribute(field);
                        if (obj == null){
                            return false;
                        }
                        sbCode.append(obj);
                        randCodeObj = CookieSessionUtil.getSession(request, response).getAttribute(FormConstans.SESSION_RAND_CODE);
              //          CookieSessionUtil.getSession(request, response).removeAttribute(FormConstans.SESSION_RAND_CODE);
                    }

                }
            }

        if (!StringUtils.isBlank(reqEnCode)) {
            LOG.debug("===========================randCodeObj="+randCodeObj);

            if (randCodeObj == null){
                return false;
            }

            if(validateItem!=null){
                sbCode.append(validateItem.toString());
            }
            LOG.debug("===========================validateItem="+validateItem.toString());
            String enCode = ValidateFormUtil.getEncryptCode(sbCode.toString(), randCodeObj.toString());
            LOG.debug("===========================reqEnCode="+reqEnCode);
            LOG.debug("===========================enCode="+enCode);

            if (!enCode.equals(reqEnCode)) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 用MD5构造加密code
     * @param codeStr
     * @param randCode
     * @return
     */
    public static String getEncryptCode(String codeStr, String randCode){

        String enCode = EncryptTools.EncryptByMD5(codeStr + randCode);

        try {
            String ssCode = ConfigOnZk.getInstance().getValue("web/system.properties", "SECURITY_SUBMIT_ENCCODE");
            LOG.debug("===========getEncryptCode.codeStr="+codeStr+", randCode="+randCode+", ssCode="+ssCode);
            return EncryptTools.EncryptByMD5(enCode + ssCode);
        } catch (Exception e) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("enCode", enCode);
            LOG.error(e, map, "");
        }
        return enCode;
    }

    public static String getEncryptCode(ValidateItem validateItem, String randCode){
        return getEncryptCode(validateItem.toString(), randCode);
    }

    public static String getNoRepeatSubmitCode(HttpServletRequest request, HttpServletResponse response, String formName) throws JedisClientException{
        UUID uuid = UUID.randomUUID();
        String uuidStr =  uuid.toString();
        //将sessionId+表单的formName拼凑作为redis的key
        String tokenKey = CookieSessionUtil.getSession(request, response).getAttribute(CookieSessionUtil.sessionField);
        //这里判断一下是否设置了formName，如果有formName则将formName也加入到input及redis的换成key中
        if(!StringUtils.isBlank(formName)){
            tokenKey = tokenKey + formName;
        }
        ShardJedisTool.getInstance().set(JedisKey.submit$norepeat, tokenKey, uuidStr);
        return uuidStr;
    }
}

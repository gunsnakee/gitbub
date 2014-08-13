package com.meiliwan.emall.commons.web;

import com.meiliwan.emall.commons.bean.ErrorPageCode;
import com.meiliwan.emall.commons.bean.WebConstant;
import com.meiliwan.emall.commons.exception.WebRuntimeException;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.exception.UserPassportException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.MlwEncryptTools;
import com.meiliwan.emall.commons.util.RandomUtil;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  获取当前登陆用户信息
 * jiawu.wu
 */
public class UserLoginUtil{

    private static final MLWLogger LOGGER = MLWLoggerFactory.getLogger(UserLoginUtil.class);

    private static final  String STATE="state";


    private UserLoginUtil(){

    }

    public static String encrypt(String oriStr) {
       return MlwEncryptTools.encryptLoginPwd(oriStr);
    }

    /**
     * 获取当前登陆用户uid，如果未登陆，返回null
     * @param request
     * @param response
     * @return
     */
    public static Integer getLoginUid(HttpServletRequest request,HttpServletResponse response) {
        Integer uidInteger = null;
        int  uid ;
        try {
            uid = CookieSessionUtil.getSession(request,response).getUid();
            if(uid != WebConstant.UID_NULL){
                String state = getUserState(uid);
                if(StringUtils.isBlank(state)){
                    uidInteger = uid;
                }else{
                    Short s = Short.parseShort(state);
                    if( s>-1){
                        uidInteger = uid;
                    }
                }

            }
        } catch (JedisClientException e) {
            LOGGER.error(e,e.getMessage(),WebUtils.getIpAddr(request));
            throw  new UserPassportException("获取当前登录用户ID时产生异常.",e);
        }
        return  uidInteger;
    }

    public static String getNickName(HttpServletRequest request,HttpServletResponse response) {
        try {
            return  CookieSessionUtil.getSession(request,response).getNickName();
        } catch (JedisClientException e) {
            LOGGER.error(e,e.getMessage(),WebUtils.getIpAddr(request));
            throw  new WebRuntimeException(ErrorPageCode.EXCEPTION_SYSTEM_DEFAULT);
        }
    }

    public static boolean updateUserState(Integer uid,String state){
        if(uid==null){
            return false;
        }
        try {
            return ShardJedisTool.getInstance().hset(JedisKey.mms$uInfo,uid,STATE,state);
        } catch (JedisClientException e) {
            LOGGER.error(e,"ShardJedisTool.getInstance().hset: {"+JedisKey.mms$uInfo.name()+","+uid+","+STATE+","+state+"}",null);
            throw new WebRuntimeException(ErrorPageCode.EXCEPTION_SYSTEM_DEFAULT);
        }
    }

    public static String getUserState(Integer uid){
        if(uid==null){
            return null;
        }
        try {
            return  ShardJedisTool.getInstance().hget(JedisKey.mms$uInfo,uid,STATE);
        } catch (JedisClientException e) {
            LOGGER.error(e,"ShardJedisTool.getInstance().hget: {"+JedisKey.mms$uInfo.name()+","+uid+","+STATE+"}",null);
            throw new WebRuntimeException(ErrorPageCode.EXCEPTION_SYSTEM_DEFAULT);
        }
    }

    /**
     * 
     * @param uid
     * @param isNewUser 当前用户是否为新注册用户
     * @return
     */
    public static String uidToMl(Integer uid, boolean isNewUser){
        try{
            return (isNewUser ? "2" : "1") + RandomUtil.randomNumCode(6)+StringUtils.leftPad(uid.toString(),11,'0')+RandomUtil.randomNumCode(3);
        }catch (Exception e){
            LOGGER.error(e,"uidToMl({uid:"+uid+"})","");
            throw new  WebRuntimeException(ErrorPageCode.EXCEPTION_SYSTEM_DEFAULT);
        }


    }
    public static Integer mlToUid(String ml){
        if(StringUtils.isBlank(ml) || ml.length()!=21){
            LOGGER.error(null,"mlToUid({ml:"+ml+"}) ml为空或者长度不是21位","");
            throw new  WebRuntimeException(ErrorPageCode.EXCEPTION_SYSTEM_DEFAULT);
        }
        try {
            return Integer.parseInt(ml.substring(7,18));
        }catch (Exception e){
            LOGGER.error(e,"mlToUid({ml:"+ml+"})","");
            throw new  WebRuntimeException(ErrorPageCode.EXCEPTION_SYSTEM_DEFAULT);
        }

    }


}

package com.meiliwan.emall.union.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.http.protocol.HTTP;
import org.springframework.ui.Model;

import com.meiliwan.emall.base.client.BaseMailAndMobileClient;
import com.meiliwan.emall.base.client.ValidateCodeSendClient;
import com.meiliwan.emall.base.dto.MailEntity;
import com.meiliwan.emall.base.dto.MsgTemplete;
import com.meiliwan.emall.commons.bean.ErrorPageCode;
import com.meiliwan.emall.commons.bean.WebConstant;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.exception.WebRuntimeException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.RandomUtil;
import com.meiliwan.emall.commons.web.CookieSessionUtil;
import com.meiliwan.emall.commons.web.UserLoginUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.mms.bean.UserPassport;
import com.meiliwan.emall.mms.client.UserPassportClient;

/**
 * 用户公共帮助类
 * Created by jiawu.wu on 13-6-29.
 */
public class UserCommonHelper {

    private static final  MLWLogger LOGGER = MLWLoggerFactory.getLogger(UserCommonHelper.class);

    private UserCommonHelper(){

    }

    /**‰
     * 更新登录密码
     *
     * @return
     */
    public static  boolean updateLoginPasswd(Model model,Integer uid, String password, String passwordRepeat) {

        if ( uid==null ) {
            model.addAttribute(WebConstant.COMMON_MSG,"用户ID不能为空!");
            return false;
        }

        if ( password==null || passwordRepeat==null || password.length()==0 || passwordRepeat.length()==0 ){
            model.addAttribute(WebConstant.COMMON_MSG,"密码不能为空!");
            return false;
        }

        if ( !password.equals(passwordRepeat) ) {
            model.addAttribute(WebConstant.COMMON_MSG,"两次输入的密码不一致!");
            return false;
        }

        try {
            password = URLDecoder.decode(password, HTTP.UTF_8);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e, "URLDecoder.decode(password, HTTP.UTF_8)", null);
            throw  new WebRuntimeException(ErrorPageCode.EXCEPTION_SYSTEM_DEFAULT);
        }

        UserPassport user = new UserPassport();
        user.setUid(uid);
        user.setPassword(UserLoginUtil.encrypt(password));

        boolean updateFlag =  UserPassportClient.update(user);
        if(!updateFlag){
            model.addAttribute(WebConstant.COMMON_MSG,"密码更新失败，可能操作已经超时!");
        }
        return updateFlag;
    }

    /**
     * 创建用户ID
     * @return
     */
    public static synchronized String grantUserId(){
      return "mlw_"+ StringUtils.leftPad(UserPassportClient.getSeqId(),10,'0');
    }

    /**
     * 生成不重复昵称 
     * @param initNickName
     * @return
     */
    public static synchronized String grantNickName(String initNickName) {
        if (StringUtils.isBlank(initNickName)) {
            return initNickName;
        }
        UserPassport user = UserPassportClient.getPassportByNickName(initNickName);
        if (user != null) {
            String key = "_mlw";
            Pattern pat = Pattern.compile("^*" + key + "[0-9]*$");
            Matcher mat = pat.matcher(initNickName);
            if (mat.find()) {
                int index = initNickName.lastIndexOf(key);
                String f = initNickName.substring(index + key.length(), initNickName.length());
                if (StringUtils.isBlank(f)) {
                    f = "1";
                } else {
                    int s = Integer.parseInt(f);
                    f = (++s) + "";
                }
                initNickName = initNickName.substring(0, index + key.length()) + f;
                return grantNickName(initNickName);
            } else {
                return grantNickName(initNickName + key);
            }
        } else {
            return initNickName;
        }
    }

}

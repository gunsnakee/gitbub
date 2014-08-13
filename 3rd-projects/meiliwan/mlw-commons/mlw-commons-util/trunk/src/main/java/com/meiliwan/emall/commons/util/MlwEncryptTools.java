package com.meiliwan.emall.commons.util;

import org.apache.commons.lang.StringUtils;

import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.exception.BaseRuntimeException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.web.exception.ErrorPageCode;

/**
 * Created by wenlepeng on 13-10-21.
 */
public class MlwEncryptTools {

    private static final MLWLogger LOGGER = MLWLoggerFactory.getLogger(MlwEncryptTools.class);


    private final static String PROPERTIES_ZK = "commons/keys.properties";

    private MlwEncryptTools(){

    }


    public static  String encrypt(String oriStr,String keyPrefix){


        if(StringUtils.isBlank(oriStr)){
            return oriStr;
        }

        String lst = "";

        if( StringUtils.isBlank(keyPrefix)){
            LOGGER.error(null, "加密key不允许空", null);
            throw new BaseRuntimeException(ErrorPageCode.EXCEPTION_SYSTEM_DEFAULT);
        }

        String md5key = null;
        String sha1Key = null;

        try {
            md5key = ConfigOnZk.getInstance().getValue(PROPERTIES_ZK, keyPrefix+".MD5");
            sha1Key =  ConfigOnZk.getInstance().getValue(PROPERTIES_ZK, keyPrefix+".SHA1");
        } catch (BaseException e) {
            LOGGER.error(e, "尝试从Zookepper获取key值发生异常.", null);
        }

       return encrypt(oriStr,md5key,sha1Key);

    }


    public static String encrypt(String oriStr,String md5KeyStr,String shaKeyStr){
        String lst = "";

        if(StringUtils.isBlank(md5KeyStr) || StringUtils.isBlank(md5KeyStr)){
            LOGGER.error(null, "MD5和SHA1加密key不允许空", null);
            throw new BaseRuntimeException(ErrorPageCode.EXCEPTION_SYSTEM_DEFAULT);
        }

        lst = MD5Util.union(oriStr,md5KeyStr);

        lst = EncryptTools.EncryptByMD5(lst);

        lst = MD5Util.union(lst, org.apache.commons.lang.StringUtils.reverse(shaKeyStr));

        lst = EncryptTools.EncryptBySHA1(lst);

        return lst;
    }

    /**
     * 加密登录密码
     * @param oriStr
     * @return
     */
    public static String encryptLoginPwd(String oriStr){
        return  encrypt(oriStr,"passportKey");
    }

    /**
     * 加密钱包支付密码
     * @param oriStr
     * @return
     */
    public static String encryptAccountPwd(String oriStr){
        return  encrypt(oriStr,"accountKey");
    }






}

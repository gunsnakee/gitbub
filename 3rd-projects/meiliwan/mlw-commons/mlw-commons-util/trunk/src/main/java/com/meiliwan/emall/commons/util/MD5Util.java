package com.meiliwan.emall.commons.util;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import org.apache.commons.lang.StringUtils;

import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密公共类
 * @author jiawu.wu
 *
 */
public final class MD5Util {


    private static final MLWLogger LOGGER = MLWLoggerFactory.getLogger(MD5Util.class);

    private MD5Util(){

    }



    /**
     * 得到str的原始MD5字符串
     * @param str
     * @return
     */
    public static String toMD5(String str){

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[]byteDigest = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < byteDigest.length; offset++) {
                i = byteDigest[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            //32位加密
            return buf.toString();
            // 16位的加密
            //return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e, "[MD5 加密异常，加密字符串:]" + str, "");
            return null;
        }

    }

    public static String union(String str,String key) {
        int strLen = str.length();
        int keyLen = key.length();
        Character[] s = new Character[strLen+keyLen];

        boolean flag= true;
        int strIdx=0;
        int keyIdx=0;
        for(int i=0;i<s.length;i++){
            if(flag){
                if(strIdx<strLen){
                    s[i] = str.charAt(strIdx);
                    strIdx++;
                }
                if(keyIdx<keyLen){
                    flag=false;
                }

            }else{
                if(keyIdx<keyLen){
                    s[i]=key.charAt(keyIdx);
                    keyIdx++;
                }
                if(strIdx<strLen){
                    flag=true;
                }

            }
        }
        return StringUtils.join(s);
    }



    /**
     *  加密str
     *
     * @param str 要加密的字符串
     * @param key 加密的key
     * @return
     */
    public static String encrypt(String str,String key){

        if( str==null || str.length()==0 || StringUtils.isBlank(key)){
            return encrypt(str);
        }

        return encrypt(union(str, key));

    }




    /**
     * 先将str进行一次MD5加密，加密后再取加密后的字符串的第1、3、5个字符追加到加密串，再拿这个加密串进行加密
     * @param str
     * @return
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.security.DigestException
     */
    public static String encrypt(String str)   {
        String encryptStr = toMD5(str);
        if(encryptStr!=null ){
            encryptStr = encryptStr + encryptStr.charAt(0)+encryptStr.charAt(2)+encryptStr.charAt(4);
            encryptStr = toMD5(encryptStr);
        }
        return encryptStr;
    }

}

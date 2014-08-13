package com.meiliwan.emall.union.weixin;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: yiyou.luo
 * Date: 14-3-25
 * Time: 下午5:27
 *
 */
public class VerifyUtils {


    /**
     * 加密/校验流程如下：
     1. 将token、timestamp、nonce三个参数进行字典序排序
     2. 将三个参数字符串拼接成一个字符串进行sha1加密
     3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信

     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */
    public static boolean checkSignature(String token, String signature, String timestamp, String nonce) {

        //将token timestamp nonce 按照字典顺序排序
        String[] params = new String[]{token, timestamp, nonce};
        Arrays.sort(params);

        //将上面三个参数排序之后拼接成字符串
        StringBuffer buffer = new StringBuffer();
        for(int i = 0; i < params.length; i ++){
            buffer.append(params[i]);
        }

        //str用于存储加密后的字符串
        String str = null;

        try {
            //获取sha1加密对象
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            //将组合后的字符串使用sha1加密, 加密后获得一个byte数组
            byte[] byteDigest = digest.digest(buffer.toString().getBytes());
            //获取加密后的字符串, 将byte数组转化为字符串
            str = byte2str(byteDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        /*
         * 将加密后的字符串 与 signature 参数进行比较,
         * 如果加密后的字符串为null直接返回false
        * 如果加密后的字符串不为null, 直接返回
         */
       boolean isVerified = (str != null) ? str.equals(signature.toUpperCase()) : false;

        return isVerified;
      //  return true;
    }

    /*
     * 将byte数组转为字符串
     */
    public static String byte2str(byte[] byteArray) {
        String str = "";
        for(int i = 0; i < byteArray.length; i ++){
            str += byte2HexStr(byteArray[i]);
        }
        return str;
    }

    /*
     * 将byte转为字符串
     */
    public static String byte2HexStr(byte b) {
        char[] digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        char[] temp = new char[2];
        temp[0] = digit[(b >>> 4) & 0x0F];
        temp[1] = digit[b & 0x0F];

        String s = new String(temp);

        return s;
    }

}

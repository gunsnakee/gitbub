package com.meiliwan.emall.sp2.util;

import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.ConfigOnZk;
import com.meiliwan.emall.commons.util.DESedeCoder;
import org.apache.commons.codec.binary.Base64;

/**
 * 礼品卡密码加密工具类
 * User: wuzixin
 * Date: 13-12-26
 * Time: 上午11:57
 */
public class TicketEncryptUtil {
    private static final MLWLogger LOGGER = MLWLoggerFactory.getLogger(TicketEncryptUtil.class);

    private final static String PROPERTIES_ZK = "commons/keys.properties";

    private final static String TICKETPWDKEY = "ticketPwdKey";

    /**
     * 礼品卡密码加密
     *
     * @return
     */
    public static String encrypt(String pwdStr) throws Exception {
        //获取秘钥
        String KEY_STRING = getPweKeyByZk();
        byte[] key = KEY_STRING.getBytes();

        return Base64.encodeBase64String(DESedeCoder.encrypt(pwdStr.getBytes(), key));
    }

    /**
     * 礼品卡密码解密,解密后返回原始密码
     *
     * @return
     */
    public static String decrypt(String pwdStr) throws Exception {
        //获取秘钥
        String KEY_STRING = getPweKeyByZk();
        byte[] key = DESedeCoder.decrypt(Base64.decodeBase64(pwdStr), KEY_STRING.getBytes());
        return new String(key);
    }

    /**
     * 获取秘钥
     *
     * @return
     */
    private static String getPweKeyByZk() {
        try {
            String key = ConfigOnZk.getInstance().getValue(PROPERTIES_ZK, TICKETPWDKEY);
            return key;
        } catch (BaseException e) {
            LOGGER.error(e, "尝试从Zookepper获取key值发生异常.", null);
        }
        return null;
    }

}

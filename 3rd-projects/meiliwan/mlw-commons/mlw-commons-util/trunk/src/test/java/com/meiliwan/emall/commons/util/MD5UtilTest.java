package com.meiliwan.emall.commons.util;

import junit.framework.TestCase;

import org.testng.annotations.Test;

import com.meiliwan.emall.commons.rabbitmq.RabbitmqConstant;

/**
 * Created by jiawu.wu on 13-6-27.
 */
public class MD5UtilTest extends TestCase {
    @Test
    public void testToMD5() throws Exception {
                 System.out.println("MD5:"+MD5Util.toMD5("123456"));
    }

    @Test
    public void testEncrypt() throws Exception {
        System.out.println("Encrypt:"+MD5Util.encrypt("123456","efdsfdsfdsf"));
    }


    @Test
    public void testUnion() throws Exception {
        System.out.println(RabbitmqConstant.MQ_HOST);
    }
}

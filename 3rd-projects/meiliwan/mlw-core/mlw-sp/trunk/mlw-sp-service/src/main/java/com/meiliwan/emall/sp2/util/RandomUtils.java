package com.meiliwan.emall.sp2.util;

import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.util.RandomUtil;
import com.meiliwan.emall.pms.constant.Constant;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.Random;

/**
 * 相关随机字符串生成策略
 * User: wuzixin
 * Date: 13-12-25
 * Time: 下午6:31
 */
public class RandomUtils {

    private static final String[] NUM_ARR = {"1", "2", "3", "4", "5", "6",
            "7", "8", "9"};

    private static final String[] UPPER_ALPHA_ARR = {"A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T",
            "U", "W", "X", "Z"};

    private static final String[] ALPHA_NUM_ARR = {"1", "A", "B", "2", "C", "D", "3", "E", "F", "G",
            "7", "H", "I", "5", "J", "K", "L", "M", "4", "N", "P", "Q", "9", "R", "S", "T",
            "U", "8", "W", "X", "6", "Z"};

    /**
     * 生成指定长度由数字和大写字母生成的字符串
     *
     * @param count
     * @return
     */
    public static String getStrAndNum(int count) {
        return randomAlphaAndNumStr(count);

    }

    /**
     * 获取自定义时间格式串加上随机数字长度的字符串
     *
     * @param count
     * @return
     */
    public static String getDateStrAndNum(int count, String parse) {
        return DateUtil.formatDate(new Date(), parse) + RandomUtil.randomNumCode(count);
    }

    /**
     * 获取优惠券券号
     *
     * @return
     */
    public static String getTKNoStr(Date date) {
        return "Q" + randomAlphaAndNumStr(9).toUpperCase();
    }

    /**
     * 生成指定长度随机字符串（必须包含大小写字母和数字），保证不会出现纯数字和纯字母的情况
     *
     * @param length 随机数长度
     * @return 随机字符串
     */
    private static String randomAlphaAndNumStr(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= length; i++) {
            if (i == length) {
                if (StringUtils.isAlpha(sb.toString())) {
                    sb.append(getRandom("num"));
                } else if (StringUtils.isNumeric(sb.toString())) {
                    sb.append(getRandom("upper"));
                } else {
                    sb.append(getRandom(""));
                }
            } else {
                sb.append(getRandom(""));
            }
        }
        return sb.toString();
    }

    /**
     * 生成一个随机字符,排除V、Y、0、O
     *
     * @param charOrNum 随机生成类型：upper大写字母，lower小写字母，num数字，否则三种类型随机生成
     * @return 随机字符
     */
    private static String getRandom(String charOrNum) {
        Random random = new Random();
        // 输出字母还是数字
        if ("upper".equalsIgnoreCase(charOrNum)) {
            // 大写字母
            return UPPER_ALPHA_ARR[random.nextInt(UPPER_ALPHA_ARR.length)];
        } else if ("num".equalsIgnoreCase(charOrNum)) {
            // 数字
            return NUM_ARR[random.nextInt(NUM_ARR.length)];
        } else {
            return ALPHA_NUM_ARR[random.nextInt(ALPHA_NUM_ARR.length)];
        }
    }
}

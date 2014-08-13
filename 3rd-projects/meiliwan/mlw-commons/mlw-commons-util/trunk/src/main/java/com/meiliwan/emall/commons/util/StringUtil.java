package com.meiliwan.emall.commons.util;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


/**
 * OMS系统自用的String工具类 88209759
 */
public class StringUtil {
    private static String[] BATCHAPPEND = new String[]{"000000", "00000", "0000", "000", "00",
            "0", ""};

    /**
     * 功能描述：判断是否为空或null 输入参数：<按照参数定义顺序>
     * @param target String 返回值: 类型 <说明>
     * @return boolean
     * @throw 异常描述
     */
    public static boolean checkNull(String target) {
        if (target == null || "".equals(target.trim()) || "null".equals(target.trim())) {
            return true;
        }
        return false;
    }

    /**
     * 功能描述：判断是否为空或null 输入参数：<按照参数定义顺序>
     * @param target String 返回值: 类型 <说明>
     * @return boolean
     * @throw 异常描述
     */
    public static boolean checkNull(Object target) {
        if (target == null || "".equals(target.toString().trim())
                || "null".equals(target.toString().trim())) {
            return true;
        }
        return false;
    }

    /**
     * 功能描述: 转换时间到Timestamp对象
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     *        <p/>
     *        public static Timestamp convertTimestamp(String time) { Timestamp ts = new
     *        Timestamp(System.currentTimeMillis()); return Timestamp.valueOf(time); }
     */

    public static Timestamp convertTimestamp(Date date) {
        if (date == null)
            return null;
        return new Timestamp(date.getTime());

    }

    /**
     * 功能描述: 转换字符串时间到Timestamp对象
     * @param time
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     *        <p/>
     *        public static Timestamp convertTimestamp(String time) { Timestamp ts = new
     *        Timestamp(System.currentTimeMillis()); return Timestamp.valueOf(time); }
     */

    public static Timestamp convertTimestamp(Date date, String time) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = format.format(date);
        String timeStr = dateStr + " " + time;
        Timestamp ts = Timestamp.valueOf(timeStr);
        return ts;

    }

    public static String date2String(Date date, String patten) {
        DateFormat format = new SimpleDateFormat(patten);
        String dateStr = format.format(date);

        return dateStr;
    }


    /**
     * 从指定Map中取值(Double型数据库字段)
     * @param map 数据库查询结果Map
     * @param key map中的key
     * @return map中的value
     */
    public static Double getDouble(Map<String, Object> map, String key) {
        if (map != null && StringUtils.isNotEmpty(key)) {
            if (map.get(key) != null) {
                return ((Double) map.get(key));
            }
        }
        return null;
    }

    /**
     * 从指定Map中取值(decimal型数据库字段)
     * @param map 数据库查询结果Map
     * @param key map中的key
     * @return map中的value
     */
    public static BigDecimal getBigDecimal(Map<String, Object> map, String key) {
        if (map != null && StringUtils.isNotEmpty(key)) {
            if (map.get(key) != null) {
                return ((BigDecimal) map.get(key));
            }
        }
        return null;
    }

    /**
     * 从指定Map中取值(decimal型数据库字段)
     * @param map 数据库查询结果Map
     * @param key map中的key
     * @return map中的value
     */
    public static String getBigDecimalStr(Map<String, Object> map, String key) {
        String dateStr = "";
        if (map != null && StringUtils.isNotEmpty(key)) {
            if (map.get(key) != null) {
                dateStr = ((BigDecimal) map.get(key)).toString();
            }
        }
        return dateStr;
    }

    /**
     * 从指定Map中取值
     * @param map 数据库查询结果Map
     * @param key map中的key
     * @return map中的value
     */
    public static String getString(Map<String, Object> map, String key) {
        String retStr = "";
        if (map != null && StringUtils.isNotEmpty(key)) {
            if (map.get(key) != null && (StringUtils.isNotEmpty((String) map.get(key)))) {
                retStr = ((String) map.get(key)).trim();
            }
        }
        return retStr;
    }

    /**
     * 从指定Map中取值(date型数据库字段)
     * @param map 数据库查询结果Map
     * @param key map中的key
     * @return map中的value(String)
     */
    public static String getDateStr(Map<String, Object> map, String key) {
        String dateStr = "";
        if (map != null && StringUtils.isNotEmpty(key)) {
            if (map.get(key) != null) {
                dateStr = ((Date) map.get(key)).toString();
            }
        }
        return dateStr;
    }

    /**
     * 从指定Map中取值(date型数据库字段)
     * @param map 数据库查询结果Map
     * @param key map中的key
     * @return map中的value(String)
     */
    public static Date getDate(Map<String, Object> map, String key) {
        if (map != null && StringUtils.isNotEmpty(key)) {
            if (map.get(key) != null) {
                return ((Date) map.get(key));
            }
        }
        return null;
    }

    /**
     * 从指定Map中取值(int型数据库字段)
     * @param map 数据库查询结果Map
     * @param key map中的key
     * @return map中的value
     */
    public static String getIntegerStr(Map<String, Object> map, String key) {
        String dateStr = "";
        if (map != null && StringUtils.isNotEmpty(key)) {
            if (map.get(key) != null) {
                dateStr = ((Integer) map.get(key)).toString();
            }
        }
        return dateStr;
    }

    /**
     * 从指定Map中取值(int型数据库字段)
     * @param map 数据库查询结果Map
     * @param key map中的key
     * @return map中的value
     */
    public static Integer getInteger(Map<String, Object> map, String key) {
        if (map != null && StringUtils.isNotEmpty(key)) {
            if (map.get(key) != null) {
                return (Integer) map.get(key);
            }
        }
        return null;
    }

    /**
     * 判断字符是否为数字
     * @param str
     * @return
     */
    public static boolean isNumber(final String str) {
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }

    /**
     * 截取字符串中的数字
     * @param content
     * @return
     */
    public static String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    public static Double parseDoubleByString(String str) throws NumberFormatException {
        if (str != null && !"".equals(str.trim())) {
            return Double.valueOf(str);
        }
        return null;
    }

    public static Integer parseIntegerByString(String str) throws NumberFormatException {
        if (str != null && !"".equals(str.trim())) {
            return Integer.valueOf(str);
        }
        return null;
    }

    /**
     * 功能描述：截取字符串
     * @param str      源字符串
     * @param subBytes 截取长度
     * @return String
     */
    public static String subString(String str, int subBytes) {
        // 用来存储字符串的总字节数
        int bytes = 0;
        for (int i = 0; i < str.length(); i++) {
            if (bytes == subBytes) {
                return str.substring(0, i);
            }
            char c = str.charAt(i);
            if (c < 256) {
                // 英文字符的字节数看作1
                bytes += 1;
            } else {
                // 中文字符的字节数看作2
                bytes += 2;
                if (bytes - subBytes == 1) {
                    return str.substring(0, i);
                }
            }
        }
        return str;
    }

    /**
     * 功能描述: 批次号转换 000010--10 000020--20
     * @param batch 原批次号
     * @return OMS批次号
     */
    public static String subBatch(String batch) {
        if (checkNull(batch)) {
            return "";
        }
        int temp = Integer.parseInt(batch);
        return String.valueOf(temp);
    }

    public static void main(String[] args) {
        System.out.println(getCheckCode());
        System.out.println(StringUtil.checkEmail("me_di@sina.cn"));
        System.out.println(StringUtil.checkEmail("527576701@qq.com"));
        System.out.println(StringUtil.checkEmail("wenlepeng@qq.com"));
        System.out.println(StringUtil.checkEmail("lsflsf520@126.com"));
        System.out.println(StringUtil.checkEmail("wenle.peng@opi-corp.com"));
    }

    /**
     * 功能描述: 退货批次补足六位
     * @param omsBatch
     * @return
     */
    public static String appendBatch(String omsBatch) {
        if (omsBatch.length() > 6) {
            return omsBatch;
        }
        return BATCHAPPEND[omsBatch.length()] + omsBatch;

    }


    /**
     * 功能描述: 获取4位校验码
     * @return 校验码
     */
    public static String getCheckCode() {
        String nowTime = String.valueOf(System.currentTimeMillis());
        return nowTime.substring(nowTime.length() - 4);
    }

    /**
     * 验证邮箱格式
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
//            String check = "^([a-z0-9A-Z]+[-|.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?.)+[a-zA-Z]{2,}$";
            String check = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 验证手机号格式
     * @param phone
     * @return
     */
    public static boolean checkPhone(String phone) {
        boolean flag = false;
        try {
            Pattern p = Pattern.compile("^(13|14|15|18)[0-9]{9}$");
            Matcher m = p.matcher(phone);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * String的toString()方法，null对象将返回""
     * @param str
     * @return
     */
    public static String nullToString(String str) {
        return str == null ? "" : str;
    }

    private final static Whitelist whitelist = new Whitelist()
            .addTags("div", "span", "br", "p", "i", "b", "em",
                    "u", "strong", "img", "embed", "font",
                    "blockquote")
            .addAttributes("i", "class")
            .addAttributes("img", "src", "alt", "width")
                    //增加width白名单，用于前台图片大小压缩
            .addProtocols("img", "src", "http")
            .addAttributes("span", "class", "name", "style")
            .addAttributes("font", "color", "size", "face")
            .addAttributes("a", "href")
            .addAttributes("embed", "src", "height", "width",
                    "align", "type", "wmode")
            .addProtocols("a", "href", "http")
            .addProtocols("embed", "src", "http");

    /**
     * 过滤HTML代码
     * @param body
     * @return
     */
    public static String cleanHTML(String body) {
        return Jsoup.clean(body, whitelist);
    }

    /**
     * 过滤所有特殊字符
     * @param str
     * @return
     * @throws java.util.regex.PatternSyntaxException
     */
    public static String specialFilter(String str) throws PatternSyntaxException {
        String regEx = "[`~!@#$%^&*()+=|{}':;',//[//]<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 用来对手机或者邮箱号码进行隐藏
     * @param str
     * @return
     */
    public static String stringHide(String str) {
        if (checkEmail(str)) {
            int index = str.indexOf('@');
            String prex = str.substring(0, index);
            String sufx = str.substring(index);
            StringBuilder strBuilder = new StringBuilder();
            if (prex.length() < 3) {
                strBuilder.append("***").append(sufx);
            } else {
                strBuilder.append(prex.charAt(0));
                for (int i = 1; i < prex.length() - 1; i++) {
                    strBuilder.append("*");
                }
                strBuilder.append(prex.charAt(prex.length() - 1));
                strBuilder.append(sufx);
            }
            return strBuilder.toString();
        } else if (checkPhone(str)) {
            int begin = 3;
            int end = 3;
            StringBuilder strBuilder = new StringBuilder();
            if (str.length() > 5) {
                for (int i = 0; i < begin; i++) {
                    strBuilder.append(str.charAt(i));
                }
                for (int i = begin; i < str.length() - end; i++) {
                    strBuilder.append("*");
                }
                for (int i = str.length() - end; i < str.length(); i++) {
                    strBuilder.append(str.charAt(i));
                }
            } else {
                for (int i = 0; i < str.length(); i++) {
                    if (i == 0 || i == str.length() - 1) {
                        strBuilder.append(str.charAt(i));
                    } else {
                        strBuilder.append("*");
                    }
                }
            }
            return strBuilder.toString();
        } else {
            return str;
        }

    }

    /**
     * 如是为空返回空串，如果不为空则返回原值
     * @param str
     * @return
     */
    public static String unNull(String str){
    	if(StringUtils.isBlank(str) || StringUtils.isEmpty(str)){
    		str = "" ;
    	}
    	return str ;
    }
    
    /**
	 * 判断字符串中是否包含中文
	 * @param str
	 * @return flg
	 */
	public static boolean isContainsChinese(String str) { 
		String regEx = "[\u4e00-\u9fa5]"; 
		Pattern pat = Pattern.compile(regEx); 
	    Matcher matcher = pat.matcher(str);      
	    boolean flg = false;   
	    if (matcher.find()){     
	        flg = true;    
	    }      
	    return flg;      
	}
}

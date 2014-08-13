package com.meiliwan.emall.commons.web;



import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MultiDomUrl
        implements TemplateMethodModel
{
    String host = null;
    String imgHostTag = null;
    String imgHostNumber = null;
    int imgUrlStarNum = 0;
    private static final Pattern PATTERN_IMG = Pattern.compile("http://img\\d?\\.meiliwan\\.com(.*?\\.jpg)");

    /**
     * 将默认的img.meiliwan.com的图片URL，替换做图片多域名的形式 img[0-4]
     * 替换参数默认：
     * host http://img?.meiliwan.com 图片域名
     * imgHostTag 替换标签 ?
     * imgNumber 图片地址个数 5
     * imgUrlStarNum 图片地址起始数 0
     * @param url 图片URL
     * @return
     */
    public static String replace(String url){
        return replace(url, "http://img?.meiliwan.com", "?", 5, 0);
    }

    /**
     * 将默认的img.meiliwan.com的图片URL，替换做图片多域名的形式 img[0-4]
     * @param url 图片URL
     * @param host 图片域名 http://img?.meiliwan.com
     * @param imgHostTag 替换标签 ?
     * @param imgNumber 图片地址个数 5
     * @param imgUrlStarNum 图片地址起始数 0
     * @return
     */
    private static String replace(String url, String host, String imgHostTag, int imgNumber, int imgUrlStarNum){

        if (StringUtils.isBlank(url)) {
            return "";
        }
        if (imgNumber <= 0) {
            imgNumber = 5;
        }

        int suffix = Math.abs(url.hashCode() % imgNumber) + imgUrlStarNum;
        String hostName = getHostName(host, imgHostTag, suffix);

        if (url.startsWith("/") && url.endsWith(".jpg")) {
            url = url.substring(1);
            return hostName + url;
        }else{
            Matcher matcher = PATTERN_IMG.matcher(url);
            StringBuffer sb = new StringBuffer();
            boolean findExisted = false;
            while (matcher.find()) {
                findExisted = true;

                String imgSrc = matcher.group(1);
                suffix = Math.abs(imgSrc.hashCode() % imgNumber) + imgUrlStarNum;
                hostName = getHostName(host, imgHostTag, suffix);
                matcher.appendReplacement(sb, hostName + imgSrc);
            }
            matcher.appendTail(sb);
            //如果整个字符串里找得到 http://img\d?\.meiliwan\.com(.*?\.jpg)， 则返回正则替换后的结果
            if(findExisted){
                return sb.toString();
            }else{
                return url;
            }
        }
    }

    public Object exec(List arguments) throws TemplateModelException
    {
        if ((null == arguments) || (arguments.size() == 0)) {
            return "";
        }

        String url = (String)arguments.get(0);
        int imgNumber = 5;

        if (!StringUtils.isBlank(imgHostNumber)) {
            imgNumber = Integer.valueOf(imgHostNumber).intValue();
        }
        return replace(url, host, imgHostTag, imgNumber, imgUrlStarNum);
    }

    private static String getHostName(String host, String imgHostTag, int imgNumber){
        return host.replace(imgHostTag, String.valueOf(imgNumber));
    }


    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getImgHostTag() {
        return this.imgHostTag;
    }

    public void setImgHostTag(String imgHostTag) {
        this.imgHostTag = imgHostTag;
    }

    public String getImgHostNumber() {
        return this.imgHostNumber;
    }

    public void setImgHostNumber(String imgHostNumber) {
        this.imgHostNumber = imgHostNumber;
    }

    public int getImgUrlStarNum() {
        return this.imgUrlStarNum;
    }

    public void setImgUrlStarNum(int imgUrlStarNum) {
        this.imgUrlStarNum = imgUrlStarNum;
    }
}



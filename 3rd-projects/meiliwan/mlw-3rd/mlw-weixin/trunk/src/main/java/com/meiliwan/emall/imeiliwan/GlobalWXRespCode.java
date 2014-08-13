package com.meiliwan.emall.imeiliwan;

/**
 *全局返回码
 * 公众号每次调用接口时，可能获得正确或错误的返回码，开发者可以根据返回码信息调试接口，排查错误。
 * 参考url：http://mp.weixin.qq.com/wiki/index.php?title=%E5%85%A8%E5%B1%80%E8%BF%94%E5%9B%9E%E7%A0%81%E8%AF%B4%E6%98%8E
 * User: yyluo
 * Date: 14-5-5
 * Time: 下午6:02
 * To change this template use File | Settings | File Templates.
 */
public enum GlobalWXRespCode {
    SYTEM_IS_BUSY("-1","系统繁忙"),
    REQUEST_SUCCESS("0","请求成功"),
    ACCESS_TOKEN_TIME_OUT("42001","access_token超时");

    private GlobalWXRespCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private String code;
    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

package com.meiliwan.emall.commons.web.validate;

/**
 * Created by yuxiong on 13-6-22.
 */
public interface FormConstans {

    /**
     * 写入form表单的hidden字段，请求的validate，用来标明是防止重复提交的校验
     */
    public static final String REQUEST_VALI_NOPASS_URL = "NOPASS_URL";

    public static final String REQUEST_CURR_FORMID = "currFormId";

    public static final String REQUEST_FORM_TOKEN = "fmToken";

    public static final String REQUEST_ENCRYPT_CODE = "encryptCode";
    
    public static final String REQUEST_SECURE_SIGN = "sign";

    public static final String SESSION_RAND_CODE = "sessRandCode";

    public static final String REQUEST_AJAX = "ajax";

    public static final String AJAX_RETURN_TOKEN = "returnToken";     //ajax 提交需要新令牌的

    public static final String NEW_RETURN_TOKEN = "newReturnToken";   // 新令牌

    public static final String PAKIDS = "pakIds";
    public static final String ACTIDS = "actIds";

    public static final String  VALIDATE_ITEM = "validateItem";

    public static final String UID = "uid";

    public static final String  AREACODE = "areaCode";

}

package com.meiliwan.emall.oms.bean;

import java.util.Date;

import com.meiliwan.emall.core.bean.BaseEntity;

public class OmsBizLog extends BaseEntity{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -4876553520642771012L;

	private Integer id;

    private String model;

    private String subModel;

    private String logCode;

    private Byte flag;

    private Integer userId;

    private String userName;

    private String uc;

    private String obj;

    private String objId;

    private String op;

    private String host;

    private String url;

    private String userClientIp;

    private String svrIp;

    private Date time;

    private String extraData;

    private String descp;

    @SuppressWarnings("unchecked")
	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model == null ? null : model.trim();
    }

    public String getSubModel() {
        return subModel;
    }

    public void setSubModel(String subModel) {
        this.subModel = subModel == null ? null : subModel.trim();
    }

    public String getLogCode() {
        return logCode;
    }

    public void setLogCode(String logCode) {
        this.logCode = logCode == null ? null : logCode.trim();
    }

    public Byte getFlag() {
        return flag;
    }

    public void setFlag(Byte flag) {
        this.flag = flag;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getUc() {
        return uc;
    }

    public void setUc(String uc) {
        this.uc = uc == null ? null : uc.trim();
    }

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj == null ? null : obj.trim();
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId == null ? null : objId.trim();
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op == null ? null : op.trim();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host == null ? null : host.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getUserClientIp() {
        return userClientIp;
    }

    public void setUserClientIp(String userClientIp) {
        this.userClientIp = userClientIp == null ? null : userClientIp.trim();
    }

    public String getSvrIp() {
        return svrIp;
    }

    public void setSvrIp(String svrIp) {
        this.svrIp = svrIp == null ? null : svrIp.trim();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData == null ? null : extraData.trim();
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp == null ? null : descp.trim();
    }
}
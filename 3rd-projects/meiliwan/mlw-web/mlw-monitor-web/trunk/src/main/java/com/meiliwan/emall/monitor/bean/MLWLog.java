package com.meiliwan.emall.monitor.bean;

import java.util.Date;

import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.core.bean.BaseEntity;

public class MLWLog extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3650948506513715749L;

	private Integer id;

    private String level;
    
    private String title;

    private String remark;

    private String serverIp;

    private String clientIp;

    private String module;

    private String application;

    private Date createTime;

    private String info;

    private String errorUuid;
    
	public String getErrorUuid() {
		return errorUuid;
	}

	public void setErrorUuid(String errorUuid) {
		this.errorUuid = errorUuid;
	}

	public boolean isErrorLevel(){
    		if(getLevel().equals("ERROR")){
    			return true;
    		}
    		return false;
    }
    
    public boolean isWarnLevel(){
		if(getLevel().equals("WARN")){
			return true;
		}
		return false;
    }
    
    @SuppressWarnings("unchecked")
	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
    }
    
    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp == null ? null : serverIp.trim();
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp == null ? null : clientIp.trim();
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module == null ? null : module.trim();
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application == null ? null : application.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info == null ? null : info.trim();
    }

	public String toEmailString() {
		return "编号:" + id + ", 等级:" + level + ", 标题:" + title 	+  ", 模块:" + module 
				+ ", 应用:" + application+ ", 创建时间:" + DateUtil.getDateTimeStr(createTime);
	}
    
    
}
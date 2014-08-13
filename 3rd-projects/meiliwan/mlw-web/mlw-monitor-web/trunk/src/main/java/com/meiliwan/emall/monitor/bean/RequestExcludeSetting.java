package com.meiliwan.emall.monitor.bean;

import java.io.Serializable;

import com.meiliwan.emall.commons.util.StringUtil;
import com.meiliwan.emall.core.bean.BaseEntity;

public class RequestExcludeSetting extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6411934224308237312L;

	private Integer rid;

    private String type;
    
    private String name;

    private Integer resumeTime;

    public boolean valid(){
    		
    		if(StringUtil.checkNull(type)){
    			return false;
    		}
    		if(StringUtil.checkNull(name)){
    			return false;
    		}
    		if(resumeTime==null||resumeTime<0){
    			return false;
    		}
    		return true;
    }
    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }
    
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getResumeTime() {
        return resumeTime;
    }

    public void setResumeTime(Integer resumeTime) {
        this.resumeTime = resumeTime;
    }

	@SuppressWarnings("unchecked")
	@Override
	public Integer getId() {
		// TODO Auto-generated method stub
		return this.rid;
	}
	@Override
	public String toString() {
		return "RequestExcludeSetting [rid=" + rid + ", type=" + type
				+ ", name=" + name + ", resumeTime=" + resumeTime + "]";
	}
	
	
}
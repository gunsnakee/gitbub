package com.meiliwan.emall.monitor.bean;

import com.meiliwan.emall.commons.util.StringUtil;
import com.meiliwan.emall.core.bean.BaseEntity;

public class Player extends BaseEntity{
	
	public static final short STATE_VALID=1;
	public static final short STATE_UNVALID=0;
    /**
	 * 
	 */
	private static final long serialVersionUID = -7653439042493778747L;

	private Integer pid;

    private String name;

    private String mobile;

    private String email;

    private Short state;

    public void setStateValid(){
    		this.state=STATE_VALID;
    }
    public boolean pidIsNull(){
    		if(pid==null){
			return true;
		}
    		return false;
    }
    public boolean valid(){
    		
    		if(StringUtil.checkNull(name)){
    			return false;
    		}
		return true;
    }
    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

	@Override
	public Integer getId() {
		// TODO Auto-generated method stub
		return this.pid;
	}
}
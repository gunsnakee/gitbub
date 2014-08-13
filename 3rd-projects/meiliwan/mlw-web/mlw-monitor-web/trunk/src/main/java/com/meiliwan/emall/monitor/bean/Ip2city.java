package com.meiliwan.emall.monitor.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

public class Ip2city extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3885376011985204893L;

	private Integer id;

    private Long startIp;

    private Long endIp;

    private String province;

    private String city;

    @SuppressWarnings("unchecked")
	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getStartIp() {
        return startIp;
    }

    public void setStartIp(Long startIp) {
        this.startIp = startIp;
    }

    public Long getEndIp() {
        return endIp;
    }

    public void setEndIp(Long endIp) {
        this.endIp = endIp;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

	@Override
	public String toString() {
		return "Ip2city [id=" + id + ", startIp=" + startIp + ", endIp="
				+ endIp + ", province=" + province + ", city=" + city + "]";
	}
    
}
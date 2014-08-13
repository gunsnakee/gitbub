package com.meiliwan.emall.base.bean;

import java.util.List;
import java.util.Map;

import com.meiliwan.emall.base.dto.TransportAreaDTO;

public class AreaGrade {

	private List<TransportAreaDTO> province; 
	private Map<String,List<TransportAreaDTO>> city;
	private Map<String,List<TransportAreaDTO>> county;
	public List<TransportAreaDTO> getProvince() {
		return province;
	}
	public void setProvince(List<TransportAreaDTO> province) {
		this.province = province;
	}
	public Map<String, List<TransportAreaDTO>> getCity() {
		return city;
	}
	public void setCity(Map<String, List<TransportAreaDTO>> city) {
		this.city = city;
	}
	public Map<String, List<TransportAreaDTO>> getCounty() {
		return county;
	}
	public void setCounty(Map<String, List<TransportAreaDTO>> county) {
		this.county = county;
	}
	
}

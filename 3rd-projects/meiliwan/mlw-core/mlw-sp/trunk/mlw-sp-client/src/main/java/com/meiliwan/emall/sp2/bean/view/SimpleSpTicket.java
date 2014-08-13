package com.meiliwan.emall.sp2.bean.view;

import java.math.BigDecimal;
import java.util.Map;

import com.meiliwan.emall.sp2.constant.SpTicketType;

public class SimpleSpTicket {
	
	private String ticketId; 
	
	private String batchId;
	
	private SpTicketType stType;
	
	private Map<SimpleOrdi, BigDecimal> soiDiscountMap;
	
	public SpTicketType getStType() {
		return stType;
	}
	
	public void setStType(SpTicketType stType) {
		this.stType = stType;
	}
	
	public String getTicketId() {
		return ticketId;
	}
	
	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}
	
	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public Map<SimpleOrdi, BigDecimal> getSoiDiscountMap() {
		return soiDiscountMap;
	}

	public void setSoiDiscountMap(Map<SimpleOrdi, BigDecimal> soiDiscountMap) {
		this.soiDiscountMap = soiDiscountMap;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((batchId == null) ? 0 : batchId.hashCode());
		result = prime * result + ((stType == null) ? 0 : stType.hashCode());
		result = prime * result
				+ ((ticketId == null) ? 0 : ticketId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleSpTicket other = (SimpleSpTicket) obj;
		if (batchId == null) {
			if (other.batchId != null)
				return false;
		} else if (!batchId.equals(other.batchId))
			return false;
		if (stType != other.stType)
			return false;
		if (ticketId == null) {
			if (other.ticketId != null)
				return false;
		} else if (!ticketId.equals(other.ticketId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SimpleSpTicket [ticketId=" + ticketId + ", batchId=" + batchId
				+ ", stType=" + stType + ", soiDiscountMap=" + soiDiscountMap
				+ "]";
	}
	
	
}

package com.meiliwan.emall.sp2.bean;

import java.math.BigDecimal;
import java.util.Date;

import com.meiliwan.emall.core.bean.BaseEntity;

public class SpTicketBatchProdBean  extends BaseEntity {
		private static final long serialVersionUID = -5347749024065887724L;
	   
	    private Integer batchId;

	    private Integer proId;

	    private String ticketName;
	    
	    private BigDecimal ticketPrice;

	    private Short ticketType;
	    
	    private Date startTime;

	    private Date endTime;
	    
	    private String actUrl;

	    private String descp;


		public Integer getBatchId() {
			return batchId;
		}

		public void setBatchId(Integer batchId) {
			this.batchId = batchId;
		}

		public Integer getProId() {
			return proId;
		}

		public void setProId(Integer proId) {
			this.proId = proId;
		}

		public String getTicketName() {
			return ticketName;
		}

		public void setTicketName(String ticketName) {
			this.ticketName = ticketName;
		}

		public BigDecimal getTicketPrice() {
			return ticketPrice;
		}

		public void setTicketPrice(BigDecimal ticketPrice) {
			this.ticketPrice = ticketPrice;
		}

		public Short getTicketType() {
			return ticketType;
		}

		public void setTicketType(Short ticketType) {
			this.ticketType = ticketType;
		}

		public Date getStartTime() {
			return startTime;
		}

		public void setStartTime(Date startTime) {
			this.startTime = startTime;
		}

		public Date getEndTime() {
			return endTime;
		}

		public void setEndTime(Date endTime) {
			this.endTime = endTime;
		}

		public String getActUrl() {
			return actUrl;
		}

		public void setActUrl(String actUrl) {
			this.actUrl = actUrl;
		}

		public String getDescp() {
			return descp;
		}

		public void setDescp(String descp) {
			this.descp = descp;
		}
		
		@Override
		public Integer getId() {
			return batchId;
		}
		
}

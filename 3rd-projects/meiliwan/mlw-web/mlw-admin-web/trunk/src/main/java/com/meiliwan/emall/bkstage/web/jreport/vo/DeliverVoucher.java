package com.meiliwan.emall.bkstage.web.jreport.vo;


public class DeliverVoucher extends BaseReportVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5724293984753344732L;

	private int recvUid;
	
	private String recvNickName;
	
	private String recvArea;
	
	private String recvOrg;
	
	private String recvAddress;
	
	private String recvPhone;

	
	
	public DeliverVoucher(int recvUid, String recvNickName, String recvArea,
			String recvOrg, String recvAddress, String recvPhone) {
		super();
		this.recvUid = recvUid;
		this.recvNickName = recvNickName;
		this.recvArea = recvArea;
		this.recvOrg = recvOrg;
		this.recvAddress = recvAddress;
		this.recvPhone = recvPhone;
	}

	public int getRecvUid() {
		return recvUid;
	}

	public void setRecvUid(int recvUid) {
		this.recvUid = recvUid;
	}

	public String getRecvNickName() {
		return recvNickName;
	}

	public void setRecvNickName(String recvNickName) {
		this.recvNickName = recvNickName;
	}

	public String getRecvArea() {
		return recvArea;
	}

	public void setRecvArea(String recvArea) {
		this.recvArea = recvArea;
	}

	public String getRecvOrg() {
		return recvOrg;
	}

	public void setRecvOrg(String recvOrg) {
		this.recvOrg = recvOrg;
	}

	public String getRecvAddress() {
		return recvAddress;
	}

	public void setRecvAddress(String recvAddress) {
		this.recvAddress = recvAddress;
	}

	public String getRecvPhone() {
		return recvPhone;
	}

	public void setRecvPhone(String recvPhone) {
		this.recvPhone = recvPhone;
	}

	@Override
	public String toString() {
		return "DeliverVoucherDIO [recvUid=" + recvUid + ", recvNickName="
				+ recvNickName + ", recvArea=" + recvArea + ", recvOrg="
				+ recvOrg + ", recvAddress=" + recvAddress + ", recvPhone="
				+ recvPhone + "]";
	}

	@Override
	public Integer getId() {
		return this.getRecvUid();
	}
	
	
}

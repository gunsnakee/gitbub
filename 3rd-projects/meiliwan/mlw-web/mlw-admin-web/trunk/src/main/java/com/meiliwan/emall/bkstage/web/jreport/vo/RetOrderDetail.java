package com.meiliwan.emall.bkstage.web.jreport.vo;

import java.util.Date;
import java.util.List;

/**
 * 逆向-换货商品清单
 *
 */
public class RetOrderDetail extends BaseReportVO{
	private String orderId;
    private String oldOrderId;
	private String retType;
	private String retStatus;
	private Date applyTime;
	private String createType;
	private String payType;
	private short isInvoice;
	private String recvName;
    private String recvArea;
    private String recvAddress;
    private String recvPhone;
    private String recvMobile;
    private String userId;
    private String userNickName;
    private String userEmail;
    private boolean userEmailVerified;
    private String userMobile;
    private boolean userMobileVerified;
    private int userAfterSaleScore;
    private String checkResult;
    private String checkDetail;
    private String checkMan;
    private Date checkTime;

    private String stockCheckResult;
    private String stockCheckDetail;
    private String stockCheckMan;
    private Date stockCheckTime;

    private String serviceType;
    private String refundType;
    private double refundProAmount;
    private double refundTransFee;
    private double claimTransFee;
    private double refundTotalAmount;
    
    //增加退款渠道详情各字段 lzl 2014.07.04
    private double retCardAmount ;
    private double retMlwAmount ;
    private double retThirdPayAmount ;
    private String serviceRetAlipayName;
    private String serviceRetAlipay;
    private String serviceRetBank;
    private String serviceRetCardNum;
    private String serviceRetCardName;
    private String serviceRetOpenBank;

    private List<RetOrderLog> retOrderLogs;
    private List<RetOrderGoodsItem> retOrderGoodsItems;

    public String getCheckMan() {
        return checkMan;
    }

    public void setCheckMan(String checkMan) {
        this.checkMan = checkMan;
    }

    public String getStockCheckResult() {
        return stockCheckResult;
    }

    public void setStockCheckResult(String stockCheckResult) {
        this.stockCheckResult = stockCheckResult;
    }

    public String getStockCheckDetail() {
        return stockCheckDetail;
    }

    public void setStockCheckDetail(String stockCheckDetail) {
        this.stockCheckDetail = stockCheckDetail;
    }

    public String getStockCheckMan() {
        return stockCheckMan;
    }

    public void setStockCheckMan(String stockCheckMan) {
        this.stockCheckMan = stockCheckMan;
    }

    public Date getStockCheckTime() {
        return stockCheckTime;
    }

    public void setStockCheckTime(Date stockCheckTime) {
        this.stockCheckTime = stockCheckTime;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getRefundType() {
        return refundType;
    }

    public void setRefundType(String refundType) {
        this.refundType = refundType;
    }

    public double getRefundProAmount() {
        return refundProAmount;
    }

    public void setRefundProAmount(double refundProAmount) {
        this.refundProAmount = refundProAmount;
    }

    public double getRefundTransFee() {
        return refundTransFee;
    }

    public void setRefundTransFee(double refundTransFee) {
        this.refundTransFee = refundTransFee;
    }

    public double getClaimTransFee() {
        return claimTransFee;
    }

    public void setClaimTransFee(double claimTransFee) {
        this.claimTransFee = claimTransFee;
    }

    public double getRefundTotalAmount() {
        return refundTotalAmount;
    }

    public void setRefundTotalAmount(double refundTotalAmount) {
        this.refundTotalAmount = refundTotalAmount;
    }

    public String getOrderId() {
		return orderId;
	}


	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	public String getOldOrderId() {
		return oldOrderId;
	}


	public void setOldOrderId(String oldOrderId) {
		this.oldOrderId = oldOrderId;
	}


	public String getRetType() {
		return retType;
	}


	public void setRetType(String retType) {
		this.retType = retType;
	}


	public String getRetStatus() {
		return retStatus;
	}


	public void setRetStatus(String retStatus) {
		this.retStatus = retStatus;
	}


	public Date getApplyTime() {
		return applyTime;
	}


	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}


	public String getCreateType() {
		return createType;
	}


	public void setCreateType(String createType) {
		this.createType = createType;
	}


	public String getPayType() {
		return payType;
	}


	public void setPayType(String payType) {
		this.payType = payType;
	}


	public short getIsInvoice() {
		return isInvoice;
	}


	public void setIsInvoice(short isInvoice) {
		this.isInvoice = isInvoice;
	}


	public String getRecvName() {
		return recvName;
	}


	public void setRecvName(String recvName) {
		this.recvName = recvName;
	}


	public String getRecvArea() {
		return recvArea;
	}


	public void setRecvArea(String recvArea) {
		this.recvArea = recvArea;
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


	public String getRecvMobile() {
		return recvMobile;
	}


	public void setRecvMobile(String recvMobile) {
		this.recvMobile = recvMobile;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getUserNickName() {
		return userNickName;
	}


	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
	}


	public String getUserEmail() {
		return userEmail;
	}


	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}


	public boolean isUserEmailVerified() {
		return userEmailVerified;
	}


	public void setUserEmailVerified(boolean userEmailVerified) {
		this.userEmailVerified = userEmailVerified;
	}


	public String getUserMobile() {
		return userMobile;
	}


	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}


	public boolean isUserMobileVerified() {
		return userMobileVerified;
	}


	public void setUserMobileVerified(boolean userMobileVerified) {
		this.userMobileVerified = userMobileVerified;
	}


	public int getUserAfterSaleScore() {
		return userAfterSaleScore;
	}


	public void setUserAfterSaleScore(int userAfterSaleScore) {
		this.userAfterSaleScore = userAfterSaleScore;
	}


	public String getCheckResult() {
		return checkResult;
	}


	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}


	public String getCheckDetail() {
		return checkDetail;
	}


	public void setCheckDetail(String checkDetail) {
		this.checkDetail = checkDetail;
	}


	public Date getCheckTime() {
		return checkTime;
	}


	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}


	public List<RetOrderLog> getRetOrderLogs() {
		return retOrderLogs;
	}


	public void setRetOrderLogs(List<RetOrderLog> retOrderLogs) {
		this.retOrderLogs = retOrderLogs;
	}


	public List<RetOrderGoodsItem> getRetOrderGoodsItems() {
		return retOrderGoodsItems;
	}


	public void setRetOrderGoodsItems(List<RetOrderGoodsItem> retOrderGoodsItems) {
		this.retOrderGoodsItems = retOrderGoodsItems;
	}


	@Override
    public String getId() {
        return orderId;  //To change body of implemented methods use File | Settings | File Templates.
    }

	public double getRetCardAmount() {
		return retCardAmount;
	}

	public void setRetCardAmount(double retCardAmount) {
		this.retCardAmount = retCardAmount;
	}

	public double getRetMlwAmount() {
		return retMlwAmount;
	}

	public void setRetMlwAmount(double retMlwAmount) {
		this.retMlwAmount = retMlwAmount;
	}

	public double getRetThirdPayAmount() {
		return retThirdPayAmount;
	}

	public void setRetThirdPayAmount(double retThirdPayAmount) {
		this.retThirdPayAmount = retThirdPayAmount;
	}

	public String getServiceRetAlipay() {
		return serviceRetAlipay;
	}

	public void setServiceRetAlipay(String serviceRetAlipay) {
		this.serviceRetAlipay = serviceRetAlipay;
	}

	public String getServiceRetBank() {
		return serviceRetBank;
	}

	public void setServiceRetBank(String serviceRetBank) {
		this.serviceRetBank = serviceRetBank;
	}

	public String getServiceRetCardNum() {
		return serviceRetCardNum;
	}

	public void setServiceRetCardNum(String serviceRetCardNum) {
		this.serviceRetCardNum = serviceRetCardNum;
	}

	public String getServiceRetCardName() {
		return serviceRetCardName;
	}

	public void setServiceRetCardName(String serviceRetCardName) {
		this.serviceRetCardName = serviceRetCardName;
	}

	public String getServiceRetOpenBank() {
		return serviceRetOpenBank;
	}

	public void setServiceRetOpenBank(String serviceRetOpenBank) {
		this.serviceRetOpenBank = serviceRetOpenBank;
	}

	public String getServiceRetAlipayName() {
		return serviceRetAlipayName;
	}

	public void setServiceRetAlipayName(String serviceRetAlipayName) {
		this.serviceRetAlipayName = serviceRetAlipayName;
	}
}

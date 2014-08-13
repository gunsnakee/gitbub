package com.meiliwan.emall.oms.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.meiliwan.emall.oms.bean.*;

public class OrdDetailDTO implements Serializable{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 3895801635215695307L;
	private Ord ord;
    private List<Ordi> ordiList=new ArrayList<Ordi>();
    private OrdInvoice invoice;
    private OrdAddr ordAddr;
    private List<OrdPay> ordPayList=new ArrayList<OrdPay>();
    private List<OmsBizLog> logList=new ArrayList<OmsBizLog>();
    private List<OrdLog> ordlogList=new ArrayList<OrdLog>();
    private OrdDesc ordDesc;
	public List<OrdLog> getOrdlogList() {
		return ordlogList;
	}
	public void setOrdlogList(List<OrdLog> ordlogList) {
		this.ordlogList = ordlogList;
	}
	public List<OrdPay> getOrdPayList() {
		return ordPayList;
	}
	public void setOrdPayList(List<OrdPay> ordPayList) {
		this.ordPayList = ordPayList;
	}
	public OrdInvoice getInvoice() {
		if(invoice==null){
			return new OrdInvoice();
		}
		return invoice;
	}
	public void setInvoice(OrdInvoice invoice) {
		this.invoice = invoice;
	}

    public OrdDesc getOrdDesc() {
        return ordDesc;
    }

    public void setOrdDesc(OrdDesc ordDesc) {
        this.ordDesc = ordDesc;
    }

    public OrdAddr getOrdAddr() {
        return ordAddr;
    }

    public void setOrdAddr(OrdAddr ordAddr) {
        this.ordAddr = ordAddr;
    }

    public Ord getOrd() {
		return ord;
	}
	public void setOrd(Ord ord) {
		this.ord = ord;
	}
	public List<Ordi> getOrdiList() {
		return ordiList;
	}
	public void setOrdiList(List<Ordi> ordiList) {
		this.ordiList = ordiList;
	}
	
	public List<OmsBizLog> getLogList() {
		return logList;
	}
	public void setLogList(List<OmsBizLog> logList) {
		this.logList = logList;
	}

    @Override
    public String toString() {
        return "OrdDetailDTO{" +
                "ord=" + ord +
                ", ordiList=" + ordiList +
                ", invoice=" + invoice +
                ", ordAddr=" + ordAddr +
                ", ordPayList=" + ordPayList +
                ", logList=" + logList +
                ", ordlogList=" + ordlogList +
                ", ordDesc=" + ordDesc +
                '}';
    }
}
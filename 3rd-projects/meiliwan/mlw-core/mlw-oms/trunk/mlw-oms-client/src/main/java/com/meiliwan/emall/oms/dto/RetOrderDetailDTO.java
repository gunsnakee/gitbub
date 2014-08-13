package com.meiliwan.emall.oms.dto;

import java.io.Serializable;
import java.util.List;

import com.meiliwan.emall.oms.bean.Ordi;
import com.meiliwan.emall.oms.bean.OrdLog;
import com.meiliwan.emall.oms.bean.OrdiStatus;
import com.meiliwan.emall.oms.bean.RetApply;

/**
 * 退换货详细列表
 * @author yinggao.zhuo
 * @date 2013-6-18
 */
public class RetOrderDetailDTO implements Serializable{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 4840164619838118827L;
	private RetApply RetApply;
    private Ordi oldOrdi;
    private Ordi ordi;
    private OrdiStatus ordiStatus;
    private List<OrdLog> logList;


    public Ordi getOldOrdi() {
        return oldOrdi;
    }

    public void setOldOrdi(Ordi oldOrdi) {
        this.oldOrdi = oldOrdi;
    }

    public Ordi getOrdi() {
        return ordi;
    }

    public void setOrdi(Ordi ordi) {
        this.ordi = ordi;
    }

    public RetApply getRetApply() {
        return RetApply;
    }

    public List<OrdLog> getLogList() {
        return logList;
    }

    public void setLogList(List<OrdLog> logList) {
        this.logList = logList;
    }

    public void setRetApply(RetApply RetApply) {
		this.RetApply = RetApply;
	}

    public OrdiStatus getOrdiStatus() {
		return ordiStatus;
	}

	public void setOrdiStatus(OrdiStatus ordiStatus) {
		this.ordiStatus = ordiStatus;
	}

	@Override
	public String toString() {
		return "RetOrderDetailDTO [RetApply=" + RetApply + ", oldOrdi=" + oldOrdi + ", ordi="
				+ ordi + ", ordiStatus=" + ordiStatus + ", logList=" + logList
				+ "]";
	}


}
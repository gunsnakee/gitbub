package com.meiliwan.emall.union.bean;

import java.util.ArrayList;
import java.util.List;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.EncryptTools;

/**
 * 
 * @author rubi
 *
 */
public class TransRequest {

	private final static MLWLogger logger = MLWLoggerFactory.getLogger(TransRequest.class);
	
	private String version;
	private Long requestTime;
	private String custCode;
	private List<TransTrace> traces = new ArrayList<TransTrace>();
	private String security;
	
	public String getSecurity() {
		return security;
	}
	public void setSecurity(String security) {
		this.security = security;
	}
	public void addTrace(TransTrace trace){
		traces.add(trace);
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Long getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(Long requestTime) {
		this.requestTime = requestTime;
	}
	public String getCustCode() {
		return custCode;
	}
	public void setCustCode(String custCode) {
		this.custCode = custCode;
	}
	public List<TransTrace> getTraces() {
		return traces;
	}
	public void setTraces(List<TransTrace> traces) {
		this.traces = traces;
	}
	public String toMD5(String preKey){
		StringBuilder sb = new StringBuilder();
		sb.append(preKey);
		sb.append(version).append(requestTime).append(custCode);
		for (TransTrace trace : traces) {
			sb.append(trace.getCustDataId());
			sb.append(trace.getOrderId()).append(trace.getOrderStatus());
			sb.append(trace.getOrderStatusInfo()).append(trace.getCurrentCityName());
			sb.append(trace.getOrderStatusTime()).append(trace.getSignMan());
		}
		logger.debug(sb.toString());
		logger.debug(EncryptTools.EncryptByMD5(sb.toString()));
		return EncryptTools.EncryptByMD5(sb.toString());
	}
	@Override
	public String toString() {
		return "EmsRequest [version=" + version + ", requestTime="
				+ requestTime + ", custCode=" + custCode + ", traces=" + traces
				+ "]";
	}
	
	public static void main(String[] args) {
		String str = EncryptTools.EncryptByMD5("1231425341.01393557130281EMSnull140223744369805投递并签收平果县速递物流揽投部1393496700000本人收签收");
		System.out.println(str);
		
	}
}

package com.meiliwan.emall.service.union;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;

import com.meiliwan.emall.commons.util.SpringContextUtil;
import com.google.gson.Gson;
import  com.meiliwan.emall.dao.union.UnionOrderDao;
import com.meiliwan.emall.bean.union.UnionOrder;
import com.meiliwan.emall.commons.bean.CpsOrderVO;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.rabbitmq.rcvmsgcenter.MsgTaskWorker;
import com.meiliwan.emall.commons.util.HttpClientUtil;
import com.meiliwan.emall.service.union.handler.CPSPushHandler;
import com.meiliwan.emall.service.union.handler.impl.CaiBeiHandler;
import com.meiliwan.emall.service.union.handler.impl.ChannetHandler;
import com.meiliwan.emall.service.union.handler.impl.LinkTechHandler;
import com.meiliwan.emall.service.union.handler.impl.TwoDimCodeHandler;

public class UnionMsgTaskWorker implements MsgTaskWorker {
	
	private final static MLWLogger LOG = MLWLoggerFactory.getLogger(UnionMsgTaskWorker.class);
	private static Map<String, CPSPushHandler> cpsPushHandlers = new HashMap<String, CPSPushHandler>();
	
	@Autowired
	private static UnionOrderDao unionOrderDao;
	
	static {
		unionOrderDao = (UnionOrderDao) SpringContextUtil.getBean("unionOrderDao");
		cpsPushHandlers.put("linkTech", new LinkTechHandler());
		cpsPushHandlers.put("channet", new ChannetHandler());
		cpsPushHandlers.put("CaiBei", new CaiBeiHandler());
		cpsPushHandlers.put("2wCode", new TwoDimCodeHandler());
	}

	public static UnionOrderDao getUnionOrderDao() {
		return unionOrderDao;
	}

	public static void setUnionOrderDao(UnionOrderDao unionOrderDao) {
		UnionMsgTaskWorker.unionOrderDao = unionOrderDao;
	}

	@Override
	public void handleMsg(String msg) {
		LOG.debug("recvd union msg(" + msg + ")");
		
		CpsOrderVO cpsOrder = new Gson().fromJson(msg, CpsOrderVO.class);
		
		String type =cpsOrder.getSourceType();
		CPSPushHandler cpsPushHandler = cpsPushHandlers.get(type);
		if (cpsPushHandler != null && !cpsPushHandler.isStoped()) {
			String pushParam = cpsPushHandler.buildPushParam(cpsOrder);
			String queryInfo = cpsPushHandler.buildQueryInfo(cpsOrder);
			String mlwQueryInfo = cpsPushHandler.buildMlwQueryInfo(cpsOrder);
			String pushUrl = cpsPushHandler.getPushUrl();
			
			saveCpsOrder(cpsOrder, queryInfo, mlwQueryInfo);
			
			if(StringUtils.isBlank(pushUrl)){
				return;
			}
			
			if(cpsPushHandler instanceof CaiBeiHandler){
				pushCpsOrderForCaiBei(pushUrl, pushParam);
			}else{
				pushCpsOrder(pushUrl, pushParam);
			}
		}else{
			LOG.warn("no such handler or has stoped ", "type:" + type, null);
		}
		
	}
	
	private void saveCpsOrder(CpsOrderVO cpsOrder, String queryInfo, String mlwQueryInfo){
		UnionOrder unionOrder = new UnionOrder();
		
		unionOrder.setSourceidId(cpsOrder.getSourceId());
		unionOrder.setSourceType(cpsOrder.getSourceType());
		unionOrder.setOrderId(cpsOrder.getOrderId());
		unionOrder.setUId(cpsOrder.getUid());
		unionOrder.setQueryInfo(queryInfo);
		unionOrder.setMlwQueryInfo(mlwQueryInfo);
		unionOrder.setCreateTime(new Date());
		unionOrder.setTotalPrice(cpsOrder.getTotalAmount());
		unionOrder.setPayMethod("other");
		unionOrder.setPayStatus(10);
		unionOrder.setOrderStatus(10);
		
		unionOrderDao.insert(unionOrder);
	}
	
	private void pushCpsOrderForCaiBei(String pushUrl, String pushParam){
		try {
			pushUrl += "?" + pushParam;
			
			LOG.info("request url",  pushUrl , null);
			HttpClientUtil.getInstance().doGet(pushUrl);
		} catch (ClientProtocolException e) {
			LOG.error(e, "request url(" + pushUrl
					+ ")  to linkTech occur ClientProtocolException", null);
		} catch (IOException e) {
			LOG.error(e, "request url(" + pushUrl
					+ ")  to linkTech occur IOException", null);
		} 
	}
	
	private void pushCpsOrder(String pushUrl, String pushParam){
		try {
			pushUrl += "?" + pushParam;
			
			URL stdUrl = new URL(pushUrl);
			URI uri = new URI(stdUrl.getProtocol(), stdUrl.getHost(), stdUrl.getPath(), stdUrl.getQuery(), null);
			LOG.info("request url",  uri , null);
			HttpClientUtil.getInstance().doGet(uri);
		} catch (ClientProtocolException e) {
			LOG.error(e, "request url(" + pushUrl
					+ ")  to linkTech occur ClientProtocolException", null);
		} catch (IOException e) {
			LOG.error(e, "request url(" + pushUrl
					+ ")  to linkTech occur IOException", null);
		} catch (URISyntaxException e) {
			LOG.error(e, "request url(" + pushUrl
					+ ")  to linkTech occur URISyntaxException", null);
		}
	}

}

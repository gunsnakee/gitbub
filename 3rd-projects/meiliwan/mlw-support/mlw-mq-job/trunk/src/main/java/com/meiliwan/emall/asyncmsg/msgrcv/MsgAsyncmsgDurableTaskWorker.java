package com.meiliwan.emall.asyncmsg.msgrcv;


import java.util.Date;
import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.gson.Gson;
import com.meiliwan.emall.asyncmsg.dao.AsyncMsgDao;
import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.rabbitmq.MqModel;
import com.meiliwan.emall.commons.rabbitmq.MsgSender;
import com.meiliwan.emall.commons.rabbitmq.asyncmsg.AsyncMsg;
import com.meiliwan.emall.commons.rabbitmq.rcvmsgcenter.MsgTaskWorker;
import com.meiliwan.emall.commons.util.SpringContextUtil;

public class MsgAsyncmsgDurableTaskWorker implements MsgTaskWorker {
	
	private static final MLWLogger LOG = MLWLoggerFactory.getLogger(MsgAsyncmsgDurableTaskWorker.class);
	
	private AsyncMsgDao asyncMsgDao = null;
	
	public MsgAsyncmsgDurableTaskWorker() {
		asyncMsgDao = (AsyncMsgDao)SpringContextUtil.getBean("asyncMsgDao");
		new Thread(new AsyncmsgWatchWorker()).start();
	}
	
	/**
	 * 提取消息，生成AsyncMsg对象，
	 */
	@Override
	public void handleMsg(String msg) {
		AsyncMsg asyncMsg = null;
		try {
			asyncMsg = new Gson().fromJson(msg, AsyncMsg.class);
			asyncMsgDao.insertSelective(asyncMsg);
		} catch (Exception e) {
			LOG.error(new BaseException("insert aysn-msg err", e), "msg : " + msg, "rabbitmqsvr.meiliwan.com");
		}
	}
	
	private class AsyncmsgWatchWorker implements Runnable {

		@Override
		public void run() {
			while (true) {
				// select now to deal msg
				try {
					List<AsyncMsg> asyncMsgList = asyncMsgDao.selectNeed2SendAsyncmsgBefore(new Date());
					for (AsyncMsg asyncMsg : asyncMsgList) {
						try {
							MqModel fromMqModel = MqModel.fromMqname(asyncMsg.getFromModel());
							MqModel toMqModel = MqModel.fromMqname(asyncMsg.getToModel());
							String msgGroupName = asyncMsg.getMsgGroupName();
							if (fromMqModel != null && toMqModel != null ) {
								MsgSender.getInstance(fromMqModel).send(toMqModel, msgGroupName, asyncMsg.getMsg());
								// send todeal msg!
								LOG.info("ASYNCMSG-MsgAsyncmsgDurableTaskWorker.AsyncmsgWatchWorker.loopRun" , asyncMsg, "rabbitmqsvr.meiliwan.com");
								// update to hasSended! 
								asyncMsg.setIsSend((short) 1);
								asyncMsgDao.updateByPrimaryKeySelective(asyncMsg);
							}
						} catch (Exception e) {
							LOG.error(new BaseException("MsgAsyncmsgDurableTaskWorker.AsyncmsgWatchWorker.loopRun send & updatestate err ", e),  asyncMsg , "rabbitmqsvr.meiliwan.com");
						}
					}
					Thread.sleep(5000);
				} catch (Exception e) {
					LOG.error(new BaseException("MsgAsyncmsgDurableTaskWorker.AsyncmsgWatchWorker err.", e), "AsyncmsgWatchWorker.run , please check !" , "rabbitmqsvr.meiliwan.com");
				}
			}
		}
		
	}
	
	public static  void main(String[] args) {
		new ClassPathXmlApplicationContext("classpath:conf/spring/bizlog-dal.xml");
	}
	 
}

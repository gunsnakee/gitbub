package com.meiliwan.emall.monitor.thread;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.StringUtil;
import com.meiliwan.emall.monitor.allocation.PlayerAppDTO;
import com.meiliwan.emall.monitor.allocation.PlayerModuleDTO;
import com.meiliwan.emall.monitor.bean.MLWLog;
import com.meiliwan.emall.monitor.service.MonitorService;
import com.meiliwan.emall.monitor.service.PlayerAppService;
import com.meiliwan.emall.monitor.service.PlayerModuleService;

@Service
public class MLWLogTimerTask extends TimerTask{

	private static final MLWLogger logger = MLWLoggerFactory.getLogger(MLWLogTimerTask.class);
	private Timer timer = new Timer();
	private static int TIMER_TIME=10*60*1000;//心跳时间十分钟
	@Autowired
	private MonitorService monitorService;
	@Autowired
	private PlayerAppService playerAppService;
	@Autowired
	private PlayerModuleService playerModuleService;
	
	
	private MLWLogTimerTask(){
		init();
	}
	private void init(){
		logger.info("MLWLogTimerTask init", null, null);
		try {
			//延时1000毫秒后重复的执行task，周期是TIMER_TIME毫秒
			timer.schedule(this, 10000, TIMER_TIME);
		} catch (Exception e) {
			logger.error(e, timer, null);
		}
	}
	@Override
	public void run() {
		//查询,判断，发送
		try {
			List<MLWLog> list = queryMLWLog();
			if(list.isEmpty()){
				return ;
			}
			filter(list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e, null, null);
		}
	}
	
	private List<MLWLog> queryMLWLog(){
		List<MLWLog> list = monitorService.getMLWLogListQueryBy10MinitueAgo();
		return list;
	}
	
	private List<PlayerAppDTO> queryPlayerApp(){
		List<PlayerAppDTO> list = playerAppService.getAllPlayerApp();
		return list;
	}
	
	private List<PlayerModuleDTO> queryPlayerModule(){
		List<PlayerModuleDTO> list = playerModuleService.getAllPlayerModule();
		return list;
	}
	
	
	private void filter(List<MLWLog> list){
			
		List<PlayerAppDTO> playerAppList = queryPlayerApp();
		List<PlayerModuleDTO> playerModuleList = queryPlayerModule();
		for (MLWLog mlwLog : list) {
			if(mlwLog.isErrorLevel()){
				sendErrorOrWarnByPlayerApp(mlwLog,playerAppList);
				sendErrorOrWarnByPlayerModule(mlwLog,playerModuleList);
			}
		}
	}
	
	private void sendErrorOrWarnByPlayerModule(MLWLog mlwLog,List<PlayerModuleDTO> playerModuleList){
		
		for (PlayerModuleDTO playerModuleDTO : playerModuleList) {
			if(playerModuleDTO.getModuleName().equals(mlwLog.getModule())){
				sendMail(mlwLog,playerModuleDTO.getEmail());
			}
		}
	}
	
	private void sendErrorOrWarnByPlayerApp(MLWLog mlwLog,List<PlayerAppDTO> playerAppList){
		StringBuilder sb = new StringBuilder();
		for (PlayerAppDTO playerAppDTO : playerAppList) {
			if(playerAppDTO.getAppName().equals(mlwLog.getApplication())){
				sb.append(playerAppDTO.getEmail()).append(",");
			}
		}
		if(sb.length()==0){
			return ;
		}
		String email = sb.substring(0, sb.length()-1);
		sendMail(mlwLog,email);
	}
	
	private void sendMail(MLWLog mlwLog,String email){
		if(StringUtil.checkNull(email)){
			return ;
		}
//		MailEntity mailEntity = new MailEntity();
//        mailEntity.setTitle("监控日志(错误/警告)").setReceivers(email).setContent(mlwLog.toEmailString());
//        BaseMailAndMobileClient.sendMail(mailEntity);
	}
}

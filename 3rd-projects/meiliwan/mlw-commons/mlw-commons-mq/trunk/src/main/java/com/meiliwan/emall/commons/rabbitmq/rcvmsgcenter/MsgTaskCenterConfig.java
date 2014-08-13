package com.meiliwan.emall.commons.rabbitmq.rcvmsgcenter;

import java.util.ArrayList;
import java.util.List;

public class MsgTaskCenterConfig {
	
	private String belongedModel = "";
	private List<MsgTaskFlowshopConfig> msgTaskFlowshopConfigList = new ArrayList<MsgTaskFlowshopConfig>();
	
	public String getBelongedModel() {
		return belongedModel;
	}

    public void setBelongedModel(String belongedModel) {
		this.belongedModel = belongedModel;
	}

	public void addTaskFlowshopConfig(MsgTaskFlowshopConfig flowshopConfig) {
		msgTaskFlowshopConfigList.add(flowshopConfig);
	}

    public List<MsgTaskFlowshopConfig> getMsgTaskFlowshopConfigList() {
		return msgTaskFlowshopConfigList;
	}
    
    
	public static class MsgTaskFlowshopConfig {
		private String rabbitMsgType = "" ;
		private String flowshopName = "" ;
		
		private List<MsgTaskWorkerConfig> msgTaskWorkerConfigList = new ArrayList<MsgTaskWorkerConfig>();
		private List<MsgPatternConfig> msgPatternConfigList = new ArrayList<MsgPatternConfig>();
		
		public void addTaskWorkerConfig(MsgTaskWorkerConfig workerTaskConfig) {
			msgTaskWorkerConfigList.add(workerTaskConfig);
		}
		
		public void addMsgPatternConfig(MsgPatternConfig msgPatternConfig) {
			msgPatternConfigList.add(msgPatternConfig);
		}
		
		public String getRabbitMsgType() {
			return rabbitMsgType;
		}
		
		public String getFlowshopName() {
			return flowshopName;
		}

		public void setFlowshopName(String flowshopName) {
			this.flowshopName = flowshopName;
		}

		public void setRabbitMsgType(String rabbitMsgType) {
			this.rabbitMsgType = rabbitMsgType;
		}

		public List<MsgTaskWorkerConfig> getMsgTaskWorkerConfigList() {
			return msgTaskWorkerConfigList;
		}
		
		public List<String> getMsgPatternList(){
			List<String> msgPatternList = new ArrayList<String>();
			for (MsgPatternConfig msgPatternConfig : msgPatternConfigList) {
				msgPatternList.add(msgPatternConfig.msgPattern);
			}
			return msgPatternList;
		}
	} 
	
	public static class MsgPatternConfig {
		private String msgPattern;

		public void setMsgPattern(String msgPattern) {
			this.msgPattern = msgPattern;
		}
	}
	
	
	public static class MsgTaskWorkerConfig {
		private String classname;
		private int ord;
		
		public String getClassname() {
			return classname;
		}
		
		public void setClassname(String classname) {
			this.classname = classname;
		}
		
		public int getOrd() {
			return ord;
		}
		
		public void setOrd(int ord) {
			this.ord = ord;
		}
	}
}

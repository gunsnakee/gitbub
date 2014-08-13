package com.meiliwan.emall.commons.rabbitmq.asyncmsg;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.meiliwan.emall.commons.rabbitmq.MqModel;
import com.meiliwan.emall.commons.rabbitmq.RabbitmqConstant;

/**
 * @author leo
 *
 * 
 * eg:
 * MsgSender.delaySend(new AsyncMsg.AsyncMsgBuilder(MqModel.xx, MqModel.tt, "msgff")
 *		 .setMsg("msg", "msgdescp")
 *		 .setTime(tomorrowTime)
 *		 .build());
 */
public class AsyncMsg {
	
	private Integer id;

    private String fromModel;

    private String toModel;
    
    private String msgGroupName;

    private String exchangeType;

    private String routingKey;

    private Date comeTime;

    private Date sendTime;

    private Integer delayTime;

    private Short isSend;

	private String msg;

    private String msgDescp;
    
    private AsyncMsg(AsyncMsgBuilder b) {
    	
    	this.fromModel = b.fromModel;
    	this.toModel = b.toModel;
    	this.msgGroupName = b.msgGroupName;
    	this.routingKey = b.routingKey;
    	this.isSend = b.isSend;
    	this.exchangeType = b.exchangeType;
    	
    	this.msg = b.msg;
    	this.msgDescp = b.msgDescp;
    	
    	this.sendTime = b.sendTime;
    	this.comeTime = b.comeTime;
    	this.delayTime = b.delayTime;
    }
    
    public AsyncMsg(){}
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFromModel() {
        return fromModel;
    }

    public void setFromModel(String fromModel) {
        this.fromModel = fromModel == null ? null : fromModel.trim();
    }

    public String getToModel() {
        return toModel;
    }

    public void setToModel(String toModel) {
        this.toModel = toModel == null ? null : toModel.trim();
    }
    
    public String getMsgGroupName() {
		return msgGroupName;
	}

	public void setMsgGroupName(String msgGroupName) {
		this.msgGroupName = msgGroupName;
	}

	public String getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(String exchangeType) {
        this.exchangeType = exchangeType == null ? null : exchangeType.trim();
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey == null ? null : routingKey.trim();
    }

    public Date getComeTime() {
        return comeTime;
    }

    public void setComeTime(Date comeTime) {
        this.comeTime = comeTime;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Integer delayTime) {
        this.delayTime = delayTime;
    }

    public Short getIsSend() {
        return isSend;
    }

    public void setIsSend(Short isSend) {
        this.isSend = isSend;
    }
    
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg == null ? null : msg.trim();
    }

    public String getMsgDescp() {
        return msgDescp;
    }

    public void setMsgDescp(String msgDescp) {
        this.msgDescp = msgDescp == null ? null : msgDescp.trim();
    }
    
    
    public static class AsyncMsgBuilder {
    	
    	private String fromModel;

        private String toModel;
        
        private String msgGroupName;

        private String exchangeType;

        private String routingKey;

        private Date comeTime;

        private Date sendTime;

        private Integer delayTime;

        private Short isSend;

    	private String msg;

        private String msgDescp;
        
        
        public AsyncMsgBuilder(MqModel fromModel, MqModel toModel, String msgGroupName) {
        	this.fromModel = fromModel.mqname();
        	this.toModel = toModel.mqname();
        	this.msgGroupName = msgGroupName;
        	
        	this.exchangeType = "topic";
        	this.routingKey = this.fromModel + RabbitmqConstant.ROUTING_KEY_TOKEN
        					+ this.toModel + RabbitmqConstant.ROUTING_KEY_TOKEN 
        					+ this.msgGroupName;
        	
        	this.isSend = -1;
        }
        
        public AsyncMsgBuilder setMsg(String msg, String msgDescp) {
			this.msg = msg;
			this.msgDescp = msgDescp;
			return this;
		}
        
        public AsyncMsgBuilder setTime(Date sendTime) {
        	this.sendTime = sendTime;
        	this.comeTime = new Date();
        	this.delayTime = (int)(sendTime.getTime()/1000 - comeTime.getTime()/1000);
        	return this;
        }

		public Date getComeTime() {
			return comeTime;
		}

		public Date getSendTime() {
			return sendTime;
		}

		public Integer getDelayTime() {
			return delayTime;
		}
		
		public AsyncMsg build() {
			return new AsyncMsg(this);
		}

    }

	@Override
	public String toString() {
		return "AsyncMsg [id=" + id + ", fromModel=" + fromModel + ", toModel="
				+ toModel + ", msgGroupName=" + msgGroupName
				+ ", exchangeType=" + exchangeType + ", routingKey="
				+ routingKey + ", comeTime=" + comeTime + ", sendTime="
				+ sendTime + ", delayTime=" + delayTime + ", isSend=" + isSend
				+ ", msg=" + msg + ", msgDescp=" + msgDescp + "]";
	}
	
	public static void main(String[] args) {
		Date date = new Date();
		Date d2 = new Date(date.getTime() + 1000L * 60);
		
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d2));
		
	}
    
}

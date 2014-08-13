package com.meiliwan.emall.base.capability;

public class Print {
	//时间消耗
	private long timeConsume;
	//请求次数
	private int ask;
	
	public long getTimeConsume() {
		return timeConsume;
	}
	public void setTimeConsume(long timeConsume) {
		this.timeConsume = timeConsume;
	}
	
	public int getAsk() {
		return ask;
	}
	public void setAsk(int ask) {
		this.ask = ask;
	}
	@Override
	public String toString() {
		return "Print [线程消耗时间:" + timeConsume +"ms,请求次数:" + ask + "]";
	}
	
}

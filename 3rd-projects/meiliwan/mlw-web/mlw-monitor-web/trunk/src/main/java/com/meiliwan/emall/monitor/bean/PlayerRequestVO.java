package com.meiliwan.emall.monitor.bean;

public class PlayerRequestVO extends Player {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6487054085990100219L;
	private Integer playerRequestId;
	public Integer getPlayerRequestId() {
		return playerRequestId;
	}
	public void setPlayerRequestId(Integer playerRequestId) {
		this.playerRequestId = playerRequestId;
	}
	@Override
	public String toString() {
		return super.toString()+"PlayerRequestVO [playerRequestId=" + playerRequestId + "]";
	}

	
	
}
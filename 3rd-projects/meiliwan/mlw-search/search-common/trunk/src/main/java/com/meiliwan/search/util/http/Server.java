package com.meiliwan.search.util.http;


public interface Server {
	public void init();

	public void start();

	public void stop();

	public String serverName();
}

package com.meiliwan.emall.commons.util;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

public class TestHttp {

	/**
	 * @param args
	 * @throws java.io.IOException
	 * @throws ClientProtocolException 
	 */
	public static void main(String[] args) throws ClientProtocolException, IOException {
		// TODO Auto-generated method stub
		HttpClientUtil.get().doGet("");
	}

}

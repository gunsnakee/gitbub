/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.meiliwan.search.util.http;

import org.jboss.netty.handler.codec.http.DefaultHttpResponse;

/**

 */
public interface Handler {

	 String getPath();
	 
	 void handle(NettyHttpRequest req,DefaultHttpResponse resp);
	 
}

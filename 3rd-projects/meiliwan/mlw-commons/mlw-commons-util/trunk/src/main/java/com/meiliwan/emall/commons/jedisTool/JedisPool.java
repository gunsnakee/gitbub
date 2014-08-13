package com.meiliwan.emall.commons.jedisTool;


import org.apache.commons.pool.impl.GenericObjectPool;

/**
 *
 *
 * User: jiawuwu
 * Date: 13-10-9
 * Time: 上午11:27
 * To change this template use File | Settings | File Templates.
 */
public class JedisPool extends redis.clients.jedis.JedisPool{

    private String host;
    private int port;

    public JedisPool(GenericObjectPool.Config poolConfig, String host, int port) {
        super(poolConfig, host, port);
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

	@Override
	public String toString() {
		return "JedisPool [host=" + host + ", port=" + port + "]";
	}
    
}

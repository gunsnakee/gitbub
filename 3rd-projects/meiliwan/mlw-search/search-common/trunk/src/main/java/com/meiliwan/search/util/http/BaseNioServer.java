package com.meiliwan.search.util.http;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelUpstreamHandler;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.BaseConfig;
import com.meiliwan.emall.commons.util.IPUtil;




public abstract class BaseNioServer implements Server {
	final ChannelGroup allChannels = new DefaultChannelGroup(
			"nio-server");
//	protected ESLogger log;
	protected MLWLogger log;
	
	protected ServerBootstrap bootstrap;
	protected ChannelFactory channelFactory = null;

	public BaseNioServer() {

	}

	/**
	 * init
	 * 
	 * Implement this {@link com.woyo.search.query.common.Server} method
	 */
	public void init() {
		log = MLWLoggerFactory.getLogger(this.serverName());
//		log = Loggers.getLogger(this.serverName());
	}

	abstract protected ChannelUpstreamHandler finalChannelUpstreamHandler();

	protected ChannelPipelineFactory getChannelPipelineFactory() {
		return new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new  ObjectEncoder(),
						new ObjectDecoder(), finalChannelUpstreamHandler());
			}
		};
	}

	protected final SocketAddress getSocketAddress() {
		String addressString = this.getServerAddress();
		return new InetSocketAddress(addressString.split(":")[0],
				Integer.parseInt(addressString.split(":")[1]));
	}

	public String getServerAddress() {

		String myip = "0.0.0.0";
		
			myip = IPUtil.getLocalIp();
		
		
		if (BaseConfig.getValue(this.serverName() + ".bind-address") == null || BaseConfig.getValue(this.serverName() + ".bind-port") == null){
			System.out.println("YOURSERVER.bind-address or port not set!!!! Using default value(s)");
		}
		String ip = BaseConfig.getValue(this.serverName() + ".bind-address", myip);
		int port = Integer.parseInt(BaseConfig.getValue(this.serverName() + ".bind-port", defaultPort()+ ""));
		return ip + ":" + port;
	}
	protected int defaultPort()
	{
		return 9889;
	}

	protected ChannelFactory createChannelFactory() {
//		ExecutorService es = Executors.newCachedThreadPool();

		return new NioServerSocketChannelFactory(//es, es
		 Executors.newCachedThreadPool(),
		 Executors.newCachedThreadPool()
		// new MemoryAwareThreadPoolExecutor(4, 0, 100000000)

		);

	}

	/**
	 * start
	 * 
	 * Implement this {@link com.woyo.search.query.common.Server} method
	 */
	public void start() {
		this.channelFactory = this.createChannelFactory();

		bootstrap = new ServerBootstrap(channelFactory);
		bootstrap.setPipelineFactory(getChannelPipelineFactory());

		Channel serverChannel = bootstrap.bind(this.getSocketAddress());
		allChannels.add(serverChannel);
//		log.info("[{}] started at : {} ", this.serverName() , this.getSocketAddress());
		log.info(String.format("[%s] started at : %s", this.serverName() , this.getSocketAddress()), "start", "");
	}

	/**
	 * stop
	 * 
	 * Implement this {@link com.woyo.search.query.common.Server} method
	 */
	public void stop() {
		ChannelGroupFuture closeFuture = allChannels.close();
		closeFuture.awaitUninterruptibly();
		
		if (channelFactory != null)
			channelFactory.releaseExternalResources();
	}

}

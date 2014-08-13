package com.meiliwan.recommend.server;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelUpstreamHandler;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;

import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.recommend.core.RecommendModule;
import com.meiliwan.recommend.core.RecommendProcessor;
import com.meiliwan.recommend.handler.AssemblerRefreshHandler;
import com.meiliwan.recommend.handler.ProductCacheHandler;
import com.meiliwan.recommend.handler.RecommendHandler;
import com.meiliwan.search.common.StatusHandler;
import com.meiliwan.search.util.http.BaseChannelHandler;
import com.meiliwan.search.util.http.BaseNioServer;
import com.meiliwan.search.util.http.Handler;
import com.meiliwan.search.util.http.Handlers;

/**
 * @author lgn-mop
 *
 */
public class RecommendServer extends BaseNioServer{
	String channelPath = RecommendModule.getZkServicePath().split("/")[RecommendModule.getZkServicePath().split("/").length -1]; //recommend
	
	Handlers  handlers = new Handlers();
	final BaseChannelHandler channel = new BaseChannelHandler( channelPath, handlers);
	public RecommendServer(){
		super();
		
//		log = Loggers.getLogger(RecommendServer.class);
		log = MLWLoggerFactory.getLogger(RecommendServer.class);
	}


	public String serverName() {
		return "RecommendHttpServer";
	}

	public void start(){
		super.start();
//		log.info("Register channel to netty : {}", channel.toString());
		log.info("Register channel to netty ", "", "");
	}
	
	
	@Override
	protected ChannelUpstreamHandler finalChannelUpstreamHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	protected ChannelPipelineFactory getChannelPipelineFactory(){
		return new ChannelPipelineFactory(){
			
			public ChannelPipeline getPipeline()
				throws Exception{
				
				// Create a default pipeline implementation.
				ChannelPipeline pipeline = Channels.pipeline();


				pipeline.addLast("decoder", new HttpRequestDecoder());

				pipeline.addLast("encoder", new HttpResponseEncoder());

				pipeline.addLast("channel", channel);

				return pipeline;
			}
		};
	}
	
	public void addHandler(Handler ... h){
		this.handlers.addHandler(h);
    }
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		RecommendServer server = new RecommendServer();
		RecommendProcessor core = new   RecommendProcessor(server.getServerAddress());
		server.addHandler(new RecommendHandler(core),
//						  new ConfigHandler(core),
						new ProductCacheHandler(),
						new AssemblerRefreshHandler(core),
						  new StatusHandler(core));
		server.start();
	}
	
}

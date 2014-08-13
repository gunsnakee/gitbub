package com.meiliwan.search.suggest.server;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelUpstreamHandler;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;

import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.search.common.Constants;
import com.meiliwan.search.common.StatusHandler;
import com.meiliwan.search.suggest.core.SuggestModule;
import com.meiliwan.search.suggest.core.SuggestProcessor;
import com.meiliwan.search.suggest.handler.SuggestHandler;
import com.meiliwan.search.util.http.BaseChannelHandler;
import com.meiliwan.search.util.http.BaseNioServer;
import com.meiliwan.search.util.http.Handler;
import com.meiliwan.search.util.http.Handlers;
//import com.meiliwan.search.util.logging.Loggers;

public class SuggestServer extends BaseNioServer{
	
	String channelPath = SuggestModule.getZkServicePath().replace(Constants.ZK_SEARCH_ROOT + "/", ""); //suggest
	
	Handlers  handlers = new Handlers();
	final BaseChannelHandler channel = new BaseChannelHandler( channelPath, handlers);
	
	
	public SuggestServer(){
		super();
//		log = Loggers.getLogger(SuggestServer.class);
		log = MLWLoggerFactory.getLogger(SuggestServer.class);
	}
	
		

	/**
	 * your should specify "serverName".bind-address in your .properties 
	 */
	public String serverName() {
		return "SuggestHttpServer";
	}

	public void addHandler(Handler ... h){
		this.handlers.addHandler(h);
    }
	
	public int defaultPort(){
		return 10087;
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
	
	@Override
	protected ChannelUpstreamHandler finalChannelUpstreamHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	public void start(){
		super.start();
//		log.info("Register channel to netty : {}", channel.toString());
		log.info("Register channel to netty", "", "");
	}
	

	public static void main(String[] args) throws Exception {
//		System.out.println(Config.get().get("zk.quorum"));
		SuggestServer s = new SuggestServer();
		
		SuggestProcessor core = new SuggestProcessor(s.getServerAddress());
		//这里有注册了两个处理器，即两个URL
		//SuggestHandler可以通过 http://10.249.9.243:10087/suggest/product/select?q=guo&size=20 的格式进行访问
		//StatusHandler可以通过  http://10.249.9.243:10087/suggest/product/status 进行访问
		s.addHandler(new SuggestHandler(core));
//		s.addHandler(new UpdateHandler(core));
		s.addHandler(new StatusHandler(core));
		s.start();
	}

}

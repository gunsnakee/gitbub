package com.meiliwan.search.spell.server;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelUpstreamHandler;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;

import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.search.common.Constants;
import com.meiliwan.search.common.StatusHandler;
import com.meiliwan.search.spell.core.SpellModule;
import com.meiliwan.search.spell.core.SpellProcessor;
import com.meiliwan.search.spell.handler.SpellHandler;
import com.meiliwan.search.util.http.BaseChannelHandler;
import com.meiliwan.search.util.http.BaseNioServer;
import com.meiliwan.search.util.http.Handler;
import com.meiliwan.search.util.http.Handlers;


public class SpellServer extends BaseNioServer{
	String channelPath = SpellModule.getZkServicePath().replace(Constants.ZK_SEARCH_ROOT + "/", "");
	
	Handlers  handlers = new Handlers();
	final BaseChannelHandler channel = new BaseChannelHandler( channelPath, handlers);
	
	
	public SpellServer(){
		super();
//		log = Loggers.getLogger(SpellServer.class);
		log = MLWLoggerFactory.getLogger(SpellServer.class);
	}
	
	public int defaultPort(){
		return 11087;
	}

	/**
	 * your should specify "serverName".bind-address in your .properties 
	 */
	public String serverName() {
		
		return "SpellHttpServer";
	}

	public void addHandler(Handler ... h){
		this.handlers.addHandler(h);
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
		log.info("Register channel to netty ", "", "");
	}
	
	public static void main(String[] args) throws Exception {
		
		SpellServer server = new SpellServer();
		
		SpellProcessor spell = new SpellProcessor(server.getServerAddress());
		
		server.addHandler(new SpellHandler(spell),
						new StatusHandler(spell));
		
		server.start();
	}
	
}

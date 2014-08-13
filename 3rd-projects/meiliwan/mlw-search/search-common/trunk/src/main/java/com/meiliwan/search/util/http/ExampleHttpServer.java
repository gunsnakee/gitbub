package com.meiliwan.search.util.http;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelUpstreamHandler;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;



public class ExampleHttpServer extends BaseNioServer{

	Handlers  handlers = new Handlers();
	final BaseChannelHandler channel = new BaseChannelHandler("example", handlers);
		

	/**
	 * your can specify "serverName".bind-address in your .properties, 
	 * or otherwise, the server use your IP as the network address
	 */
	public String serverName() {
		
		return "ExampleHttpServer";
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

}

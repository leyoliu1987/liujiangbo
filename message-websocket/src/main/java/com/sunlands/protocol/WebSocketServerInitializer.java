package com.sunlands.protocol;

import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sunlands.handler.HttpRequestForWsHandler;
import com.sunlands.handler.WebsocketServiceHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
/**
 * 
 * Created on 2017年11月15日 下午7:32:20

 * Description: [websocket启动初始化配置]

 * Company:     [尚德机构]

 * @author      [liujiangbo]
 *
 */
@Component("webSocketServerInitializer")
public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {

	@Resource(name="websocketServiceHandler")
	private WebsocketServiceHandler websocketServiceHandler;

	@Value("${request.uri}")
	private String requestUri;
	@Value("${readerIdleTimeSeconds}")
	private int readerIdleTimeSeconds;
	@Value("${writerIdleTimeSeconds}")
	private int writerIdleTimeSeconds;
	@Value("${allIdleTimeSeconds}")
	private int allIdleTimeSeconds;

	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		ChannelPipeline channelPipeline = channel.pipeline();
		// 将字节解码为httpRequest 将httpRequest编码为字节
		channelPipeline.addLast("http-codec", new HttpServerCodec());
		// 安装了这个之后下一个handler将收到完成的http 请求,聚合解码HttpRequest/HttpContent/LastHttpContent到FullHttpRequest
		channelPipeline.addLast("aggregator", new HttpObjectAggregator(65536));
		// 向客户端发送HTML5文件
		channelPipeline.addLast("http-chunked", new ChunkedWriteHandler());
		//处理FullHttpRequest
		channelPipeline.addLast(new HttpRequestForWsHandler(Optional.ofNullable(requestUri).orElse("/websocket")));
		// 处理升级握手事件 所有以／websocket请求的http协议都升级为websocket协议
		channelPipeline
				.addLast(new WebSocketServerProtocolHandler(Optional.ofNullable(requestUri).orElse("/websocket")));
		// 超时处理类
//		channelPipeline.addLast(new IdleStateHandler(Optional.ofNullable(readerIdleTimeSeconds).orElse(5),
//				Optional.ofNullable(writerIdleTimeSeconds).orElse(5),
//				Optional.ofNullable(allIdleTimeSeconds).orElse(10)));
		// 自定义处理类
		channelPipeline.addLast(websocketServiceHandler);
	}

}

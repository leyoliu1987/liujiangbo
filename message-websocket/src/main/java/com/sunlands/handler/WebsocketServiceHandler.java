package com.sunlands.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sunlands.common.dto.MessageSendDTO;
import com.sunlands.common.utils.JsonUtils;
import com.sunlands.service.WebsocketService;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

@Service("websocketServiceHandler")
@Sharable
public class WebsocketServiceHandler extends SimpleChannelInboundHandler<TextWebSocketFrame>
		implements WebsocketService {

	private static final Logger logger = LoggerFactory.getLogger(WebsocketServiceHandler.class);

	private final Map<String, Channel> consultantMap = new ConcurrentHashMap<>();
	// private final Map<String, String> randomMap = new ConcurrentHashMap<>();

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		Channel channel = ctx.channel();
		if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {// 如果websocket握手完成

			ctx.pipeline().remove(HttpRequestForWsHandler.class);// 将此pineline中的HttpRequestHandler删除

			Attribute<String> userNameAttr = channel.attr(AttributeKey.valueOf("userName"));
			Attribute<String> numberAttr = channel.attr(AttributeKey.valueOf("randomNumber"));
			Attribute<String> serviceNameAttr = channel.attr(AttributeKey.valueOf("serviceName"));

			if (userNameAttr == null || numberAttr == null || StringUtils.isBlank(userNameAttr.get())
					|| StringUtils.isBlank(numberAttr.get())) {
				channel.writeAndFlush(new CloseWebSocketFrame(1000, "LOGOUT")).addListener(ChannelFutureListener.CLOSE);
			}

			StringBuilder keyBuilder = new StringBuilder();
			keyBuilder.append(userNameAttr.get()).append("_").append(serviceNameAttr);
			Channel previousChannel = consultantMap.get(keyBuilder.toString());
			if (previousChannel != null) {
				previousChannel.writeAndFlush(new CloseWebSocketFrame(1000, "STOP"))
						.addListener(ChannelFutureListener.CLOSE);
			}
			consultantMap.put(keyBuilder.toString(), channel);

		} else if (evt instanceof IdleStateEvent) {// 闲置的连接就断掉
			IdleStateEvent event = (IdleStateEvent) evt;
			// 判断是否是channel 读 空闲
			if (event.state().equals(IdleState.READER_IDLE)) {
				if (logger.isInfoEnabled()) {
					logger.info("channel ID:{} 沒有读取到任何信息", channel.id().asShortText());
				}
				channel.writeAndFlush(new CloseWebSocketFrame()).addListener(ChannelFutureListener.CLOSE);
			}
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		this.handleWebSocketFrame(ctx, msg);
	}

	private void handleWebSocketFrame(ChannelHandlerContext ctx, TextWebSocketFrame frame) {
		// 返回应答消息
		String message = ((TextWebSocketFrame) frame).text();
		logger.info("》》》》》》handleWebSocketFrame message = {}", message);
		MessageSendDTO messageDTO = JsonUtils.toBean(message, MessageSendDTO.class);
		if (messageDTO != null) {
			String userName = messageDTO.getUserName();
			String serviceName = messageDTO.getServiceName();
			StringBuilder builder = new StringBuilder();
			builder.append(userName).append("_").append(serviceName);
			Channel userChannel = consultantMap.get(builder.toString());
			if (userChannel != null && userChannel.isActive()) {
				userChannel.writeAndFlush(new TextWebSocketFrame(message));
			} else {
				logger.error("没有发现存活的此连接  userName={}", userName);
			}
		} else {
			logger.error(">>>>>>>>>>>>>>>消息的格式不正确");
		}

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("websocket throw error: {}", cause);
		super.exceptionCaught(ctx, cause);
		this.checkChannelExistAndRemove(ctx);
	}

	private void checkChannelExistAndRemove(ChannelHandlerContext ctx) {
		try {
			Channel exceptionChannel = ctx.channel();
			Attribute<String> userName = exceptionChannel.attr(AttributeKey.valueOf("userName"));
			if (exceptionChannel.id().equals(exceptionChannel.id())) {
				consultantMap.remove(userName);
			}
		} catch (Exception e) {
			logger.error("关闭异常的websocket失败: {}", e);
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Attribute<Object> name = ctx.channel().attr(AttributeKey.valueOf("userName"));
		System.out.println("method channelInactive name={" + name.get() + "}");
		super.channelInactive(ctx);
		this.checkChannelExistAndRemove(ctx);
	}

	/**
	 * Created on 2017年11月15日 Discription:[发送消息给指定前端连接人]
	 * 
	 * @author:[liujiangbo]
	 */
	@Override
	public Boolean sendMessageToUser(String userName, Object message) {
		if (logger.isDebugEnabled() && message != null) {
			logger.debug("发送消息给指定前端连接人，内容为：{}", userName, message);
		}

		Channel activeChannel = consultantMap.get(userName);
		if (activeChannel != null && activeChannel.isActive()) {
			try {
				activeChannel.writeAndFlush(new TextWebSocketFrame(message + ""));
				return true;
			} catch (Exception e) {
				logger.error("发送消息给指定前端连接人失败,原因: {}", userName, e);
				return false;
			}
		} else {
			return false;
		}

	}

}

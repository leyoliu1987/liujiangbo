package com.sunlands.service;

import io.netty.channel.ChannelInboundHandler;

public interface WebsocketService extends ChannelInboundHandler {

	/**
	 * 发送消息给指定的连接者
	 * @param consultantAccount
	 * @param message
	 */
	public Boolean sendMessageToUser(String consultantAccount,Object obj);
}

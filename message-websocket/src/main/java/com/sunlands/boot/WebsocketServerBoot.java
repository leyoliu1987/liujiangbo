/**
 * Created on 2017年11月27日 下午3:33:03
 *
 * Description: [描述该类概要功能介绍]
 *
 * Company:     [尚德机构]
 *
 * @author      [liujiangbo]
 *
*/
package com.sunlands.boot;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sunlands.protocol.WebSocketServerInitializer;

import io.netty.bootstrap.ServerBootstrap;

/**
 * Created on 2017年11月27日 下午3:33:03
 * 
 * Description: [websocket服务启动类]
 * 
 * Company: [尚德机构]
 * 
 * @author [liujiangbo]
 *
 */
@Component("websocketServerBoot")
public class WebsocketServerBoot extends BaseNettyBoot {
	Logger logger = LoggerFactory.getLogger(WebsocketServerBoot.class);
	@Value("${websocket_port}")
	private int port;

	@Resource(name = "webSocketServerInitializer")
	private WebSocketServerInitializer webSocketServerInitializer;

	@PostConstruct
	@Override
	protected void start() {
		ServerBootstrap b = init();
		// 绑定端口
		b.bind(port).syncUninterruptibly();

		logger.info(">>>>>>>>>>>websocketServer start!!!  端口为：" + port);

		startNext();
	}

	@Override
	protected ServerBootstrap init() {
		return buildBootstrap().childHandler(webSocketServerInitializer);
	}

	



}

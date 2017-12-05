package com.sunlands.boot;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;

import com.google.common.collect.Lists;
import com.sunlands.chainserver.BootJob;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created on 2017年11月28日 下午7:39:28

 * Description: [boot共用抽象类]

 * Company:     [尚德机构]

 * @author      [liujiangbo]
 *
*/
public abstract class BaseNettyBoot extends BootJob{
	@Value("${SO_BACKLOG}")
	protected int so_backlog;
	
	
	protected Optional<Channel> channelOptional = Optional.empty();
	// 创建boss和worker
	protected EventLoopGroup bossGroup = new NioEventLoopGroup();
	protected EventLoopGroup workerGroup = new NioEventLoopGroup();
	
	protected abstract ServerBootstrap init();
	
	@Override
	protected void stop() {
		channelOptional.ifPresent(channel -> {
			channel.close();
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		});

	}
	
	protected ServerBootstrap buildBootstrap() {
		// 服务类
		ServerBootstrap b = new ServerBootstrap();

		// 设置循环线程组事例
		b.group(bossGroup, workerGroup);

		// 设置channel工厂
		b.channel(NioServerSocketChannel.class);

		b.option(ChannelOption.SO_BACKLOG, so_backlog).option(ChannelOption.SO_KEEPALIVE, true);// 链接缓冲池队列大小
		return b;
	}
}

package com.sunlands.chainserver;

import java.util.Optional;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created on 2017年11月27日 下午4:24:01
 * 
 * Description: [服务链初始化与启动类]
 * 
 * Company: [尚德机构]
 * 
 * @author [liujiangbo]
 *
 */
@Component
public class ServerLauncher {
	Logger logger = LoggerFactory.getLogger(ServerLauncher.class);

	private Optional<BootChain> chainOptional = Optional.empty();
	@Resource(name = "websocketServerBoot")
	private BootJob websocketServerBoot;

	public void init() {
		//1.获取责任链
		 chainOptional = BootChain.chain();

		// 2.链不为空时执行，采用责任链模式启动，如果有多个服务可以将多个服务的boot添加到链上启动
		chainOptional.ifPresent(chain -> chain
											.boot()
										//	.setNext(springWebBoot)
        								//	.setNext(websocketServerBoot)
											.end());
		logger.info("----------completed init--------------------------");

	}

	public void start() {
		chainOptional.ifPresent(x -> x.start());
	}

	public void stop() {
		chainOptional.ifPresent(x -> x.stop());
	}

}

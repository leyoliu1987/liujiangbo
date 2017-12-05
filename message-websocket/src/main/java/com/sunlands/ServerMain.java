package com.sunlands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.sunlands.chainserver.ServerLauncher;
import com.sunlands.config.SpringConfig;

/**
 * 
 * Created on 2017年11月15日 上午9:48:10
 * 
 * Description: [server启动]
 * 
 * Company: [尚德机构]
 * 
 * @author [liujiangbo]
 *
 */

public class ServerMain {

	private static Logger logger = LoggerFactory.getLogger(ServerMain.class);

	public static void main(String[] args) {
		 SpringApplication springApplication = new SpringApplication(SpringConfig.class);
		 springApplication.setBannerMode(Mode.OFF);
		 ConfigurableApplicationContext context = springApplication.run(args);
		 ServerLauncher launcher = context.getBean(ServerLauncher.class);
		 launcher.init();
		 launcher.start();
		 addHook(launcher);
	}
		

	/**
	 * Created on 2017年11月27日
	 *
	 * Discription:[添加一个守护线程]
	 *
	 * @author:[liujiangbo]
	 *
	 * @param launcher
	 */
	private static void addHook(ServerLauncher launcher) {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {

			try {
				launcher.stop();
			} catch (Exception e) {
				logger.error("ServerMain stop exception ", e);
			}

			logger.info("jvm exit, all service stopped.");

		}, "StartMain-shutdown-hook"));

	}

}

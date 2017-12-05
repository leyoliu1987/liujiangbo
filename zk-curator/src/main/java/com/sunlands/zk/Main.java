package com.sunlands.zk;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created on 2017年12月1日 下午2:59:01

 * Description: [描述该类概要功能介绍]

 * Company:     [尚德机构]

 * @author      [liujiangbo]
 *
*/
public class Main {

	
	public static void main(String[] args) {
		 SpringApplication springApplication = new SpringApplication(SpringConfig.class);
		// springApplication.setBannerMode(Mode.OFF);
		 ConfigurableApplicationContext context = springApplication.run(args);
	}
}

package com.sunlands.zk;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created on 2017��12��1�� ����2:59:01

 * Description: [���������Ҫ���ܽ���]

 * Company:     [�е»���]

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

package com.sunlands.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created on 2017年11月29日 下午2:00:16
 * 
 * Description: [spring配置类]
 * 
 * Company: [尚德机构]
 * 
 * @author [liujiangbo]
 *
 */
@SpringBootApplication
@Configuration
@ComponentScan(basePackages = "com.sunlands")
@PropertySource(value = { "classpath:config.properties" }, ignoreResourceNotFound = true)
public class SpringConfig {

	@Bean(name = "restTemplate")
	public RestTemplate restTemplate() {
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setConnectTimeout(3000);
		factory.setReadTimeout(3000);
		return new RestTemplate(factory);
	}

}

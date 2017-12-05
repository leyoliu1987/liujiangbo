package com.sunlands.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ErrorHandler;

/**
 * 
 * Created on 2017年11月15日 上午11:49:04
 * Description: [mq错误处理类]
 * Company:     [尚德机构]
 * @author         [liujiangbo]
 *
 */
public class MessageErrorHandler implements ErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(MessageErrorHandler.class);

    @Override
    public void handleError(Throwable t) {
        if (log.isErrorEnabled()) {
            log.error("使用activemq发送消息异常,当前异常原因为: {}",t);
        }
    }
}

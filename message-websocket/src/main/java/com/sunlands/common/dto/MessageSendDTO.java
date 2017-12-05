/**
 * Created on 2017年11月15日 上午11:35:48
 * Description: [描述该类概要功能介绍]
 * Company:     [尚德机构]
 * @author         [liujiangbo]
 *
*/
package com.sunlands.common.dto;

import java.io.Serializable;

/**
 * Created on 2017年11月15日 上午11:35:48 Description: [发送消息对象] Company: [尚德机构]
 * 
 * @author [liujiangbo]
 *
 */
public class MessageSendDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userName;

	private String message;
	
	private String serviceName;
	
	

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "MessageSendDTO [userName=" + userName + ", message=" + message + "]";
	}
	
	

}

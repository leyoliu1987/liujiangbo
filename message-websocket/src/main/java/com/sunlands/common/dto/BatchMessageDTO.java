package com.sunlands.common.dto;

import java.io.Serializable;
import java.util.List;


/**
 * Created on 2017年11月29日 下午3:52:46

 * Description: [描述该类概要功能介绍]

 * Company:     [尚德机构]

 * @author      [liujiangbo]
 *
*/
public class BatchMessageDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<String> targets;
	
	private String content;

	public List<String> getTargets() {
		return targets;
	}

	public void setTargets(List<String> targets) {
		this.targets = targets;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "BatchMessageDTO [targets=" + targets + ", content=" + content + "]";
	}
	
	

}

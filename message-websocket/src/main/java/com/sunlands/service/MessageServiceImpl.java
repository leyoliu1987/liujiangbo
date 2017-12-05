package com.sunlands.service;


import java.util.concurrent.CompletableFuture;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sunlands.common.dto.BatchMessageDTO;
import com.sunlands.common.utils.JsonUtils;
import com.sunlands.common.vo.ResultVO;

/**
 * Created on 2017年11月29日 下午5:14:43
 * 
 * Description: [发送消息处理类]
 * 
 * Company: [尚德机构]
 * 
 * @author [liujiangbo]
 *
 */
@Service
public class MessageServiceImpl implements MessageService {
	@Value("${target_netty_location}")
	private String targetNettyLocation;

	@Autowired
	private RestTemplate restTemplate;
	@Resource(name = "websocketServiceHandler")
	private WebsocketService websocketService;

	@Override
	public Boolean sendBatchMessage(BatchMessageDTO batchMessage) {
		try {
			CompletableFuture.runAsync(() -> copyToTarget(batchMessage));
			String content = batchMessage.getContent();
			batchMessage.getTargets().parallelStream()
					.forEach(target -> websocketService.sendMessageToUser(target, content));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Created on 2017年11月29日
	 *
	 * Discription:[将消息发送到另一个节点]
	 *
	 * @author:[liujiangbo]
	 *
	 * @param batchMessage
	 */
	private void copyToTarget(BatchMessageDTO batchMessage) {
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(type);
		String json = JsonUtils.toJson(batchMessage);
		HttpEntity<String> formEntity = new HttpEntity<String>(json, headers);

		restTemplate.postForObject(targetNettyLocation + "sendMessage/copyData", formEntity, ResultVO.class);

	}

	@Override
	public void sendBatchMessageWithNoCopy(BatchMessageDTO batchMessage) {
		if (batchMessage != null && CollectionUtils.isNotEmpty(batchMessage.getTargets())
				&& StringUtils.isNotBlank(batchMessage.getContent())) {
			String content = batchMessage.getContent();
			batchMessage.getTargets().parallelStream()
					.forEach(target -> websocketService.sendMessageToUser(target, content));
		}

	}

}

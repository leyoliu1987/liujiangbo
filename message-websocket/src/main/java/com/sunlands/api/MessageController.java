package com.sunlands.api;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sunlands.common.dto.BatchMessageDTO;
import com.sunlands.common.vo.ResultVO;
import com.sunlands.service.MessageService;

/**
 * Created on 2017年11月29日 上午11:44:05
 * 
 * Description: [提供给外界调用的api]
 * 
 * Company: [尚德机构]
 * 
 * @author [liujiangbo]
 *
 */
@RestController
@RequestMapping("/sendMessage")
public class MessageController {
	@Autowired
	private MessageService messageService;

	@GetMapping("/test")
	public String hello() {
		return "Hello world!";
	}

	@PostMapping("/sendBatchMessage")
	public ResultVO sendBatchMessage(BatchMessageDTO batchMessage) {
		if (batchMessage != null) {
			List<String> targets = batchMessage.getTargets();
			String content = batchMessage.getContent();
			if (CollectionUtils.isEmpty(targets))
				return ResultVO.setFailMessage("消息人列表不能为空");
			if (StringUtils.isBlank(content))
				return ResultVO.setFailMessage("消息内容不能为空");
			Boolean result = messageService.sendBatchMessage(batchMessage);
			if (result)
				return ResultVO.setSuccessData(null);
			else
				return ResultVO.setFailMessage("发送失败");

		} else {
			return ResultVO.setFailMessage("参数不能为空");
		}

	}
	
	@PostMapping("/copyData")
	public ResultVO copyData(@RequestBody BatchMessageDTO batchMessage) {
		messageService.sendBatchMessageWithNoCopy(batchMessage);
		return ResultVO.setSuccessData(null); 
	}
}

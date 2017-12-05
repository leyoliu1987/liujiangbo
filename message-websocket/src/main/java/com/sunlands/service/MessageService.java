package com.sunlands.service;

import java.util.List;

import com.sunlands.common.dto.BatchMessageDTO;

/**
 * Created on 2017年11月29日 下午4:33:01

 * Description: [发送消息处理类]

 * Company:     [尚德机构]

 * @author      [liujiangbo]
 *
*/
public interface MessageService {

	/**
	 * Created on 2017年11月29日 
	 *
	 * Discription:[批量发送消息]
	 *
	 * @author:[liujiangbo]
	 *
	 * @param targets
	 * @param content
	 * @return
	 */
	Boolean sendBatchMessage(BatchMessageDTO batchMessage);

	/**
	 * Created on 2017年11月29日 
	 *
	 * Discription:[发送同步过来的数据]
	 *
	 * @author:[liujiangbo]
	 *
	 * @param batchMessage
	 */
	void sendBatchMessageWithNoCopy(BatchMessageDTO batchMessage);
   
}

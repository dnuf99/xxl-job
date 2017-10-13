/**
 * 
 */
package com.xxl.job.admin.core.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.htffund.message.app.service.MessageInterface;

/**
 * @author Administrator
 * 
 */
public class MessageSenderManagerUtils {

	private static final Log LOG = LogFactory.getLog(MessageSenderManagerUtils.class);

	private static ExecutorService executorService = Executors
			.newCachedThreadPool();
	
	
	private static MessageInterface messageService;



	/**
	 * 发送预警短信给响应人员
	 * 
	 * @param bussCode
	 * @param contentObject
	 * @return
	 */
	public static void sendMessage(final String mobileno, final String bussCode, final String paramJsonString, final String channel) {

		executorService.execute(new Runnable() {
			@Override
			public void run() {
				messageService = (MessageInterface)SpringContextUtil.getBean("messageService");				
				
				try {
					if(messageService == null) {
						throw new IllegalStateException("can't find dubbo service");
					}
					
					if(StringUtils.isEmpty(mobileno)){
						throw new IllegalStateException("mobileno is null");
					}
					
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("businesscode", bussCode);
					map.put("targettype", "0");
					map.put("mobileno", mobileno);
					map.put("priority", 99);
					map.put("contenttype", "0");
					map.put("content", paramJsonString);
					if(StringUtils.isEmpty(channel))
						map.put("sendchannel", "");						
					else
						map.put("sendchannel", channel);
					
					String sid = messageService.sendMessage(map);

					LOG.info("message :" + paramJsonString
							+ ",消息发送成功！消息ID = " + sid + paramJsonString);

				} catch (Exception e) {
					LOG.error("消息发送异常！消息:" + paramJsonString, e);
				}

			}
		});

	}
	
	
}

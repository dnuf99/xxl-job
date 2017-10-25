/**
 * 
 */
package com.xxl.job.admin.core.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.htffund.message.app.service.MessageInterface;
import com.htffund.message.app.service.vo.Message;
import com.htffund.message.app.service.vo.Result;

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
	public static void sendMessage(final String mobileno, final String content) {

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
					Message message = new Message();
			        message.setSendChannel(Message.SendChannel.SMS);
			        message.setTemplateId("");
			        message.setUserId("");
			        message.setMobileNo(mobileno);
			        message.setContent(content);
			        Result result = messageService.sendMessage(message);
			        LOG.info("[sendMobileMsg,mobileNo=," + mobileno + "errorCode=" + result.getErrorCode() + ",errorMsg=" + result.getErrorMsg()
			                + " ,短信数据：" + content);

				} catch (Exception e) {
					LOG.error("消息发送异常！mobileno:" + mobileno + ",消息: " + content, e);
				}

			}
		});

	}
	
	
}

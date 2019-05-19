package com.zhibo.base;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.zhibo.base.comm.MatcherByManagerCode;
import com.zhibo.base.comm.MyServer;

import io.netty.buffer.Unpooled;

/**
 * 通信管理机配置报文接收
 * @author 44489
 * @version 2019年4月19日下午3:05:03
 */
@Component
public class RabbitManagerConfigReceiver {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	@RabbitListener(queues=RabbitConfig.MANAGER_CONFIG_QUEUE_NAME)
	public void receiveMsg(byte[] msg) {
		String str = Util.bytesToHexString(msg);
		logger.debug(str);
		//长度不可小于8个字节
		if(msg.length < 8) {
			return;
		}
		MatcherByManagerCode matcher = new MatcherByManagerCode(Arrays.copyOfRange(msg, 2, 6));
		MyServer.channelGroup.writeAndFlush(Unpooled.copiedBuffer(msg), matcher);
	}
}

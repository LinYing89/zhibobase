package com.zhibo.base.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.zhibo.base.data.NetMessageAnalysisResult;

@Service
public class TestService {

	private SimpMessageSendingOperations messaging;
	
	@Autowired
	public TestService(SimpMessageSendingOperations messaging) {
		this.messaging = messaging;
	}
	
	public void broadcastReceived(NetMessageAnalysisResult result) {
		result.setTime("[" + new SimpleDateFormat("HH-mm-ss").format(new Date()) + "]");
		String topic = String.format("/topic/netMessage");
		messaging.convertAndSend(topic, result);
	}
	
	public void broadcastSent(NetMessageAnalysisResult result) {
		result.setTime("[" + new SimpleDateFormat("HH-mm-ss").format(new Date()) + "]");
		String topic = String.format("/topic/sent");
		messaging.convertAndSend(topic, result);
	}
}
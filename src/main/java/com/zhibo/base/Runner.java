package com.zhibo.base;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {
	
//	@Autowired
//	private RabbitTemplate rabbitTemplate;
    @Override
    public void run(String... args) throws Exception {
//        System.out.println("Sending message...");
//    	byte[] req = new byte[]{0x00, (byte) 0xff, 0x00, (byte) 0xff};
//    	rabbitTemplate.convertAndSend(RabbitConfig.TOPIC_MANAGER_PUBLISH_EXCHAGE_NAME, "com.zhibo.manager0", req);
    }

}

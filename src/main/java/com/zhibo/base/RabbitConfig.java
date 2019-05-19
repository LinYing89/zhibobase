package com.zhibo.base;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

	/**
	 * 通信管理机配置报文接收队列
	 */
	static final String MANAGER_CONFIG_QUEUE_NAME = "manager-config";
	/**
	 * 通信管理机配置报文接收交换机
	 */
	static final String TOPIC_MANAGER_CONFIG_EXCHAGE_NAME = "manager-config-exchange";
	/**
	 * 通信管理机报文分发交换机
	 */
	public static final String TOPIC_MANAGER_PUBLISH_EXCHAGE_NAME = "manager-publish-exchange";
	
	//随机生成queue名称, 程序退出后自动删除
//	public String queueName;
	
	/**
	 * 通信管理机配置报文接收队列
	 * 
	 * @return
	 */
	@Bean
	Queue managerConfigQueue() {
		return new Queue(MANAGER_CONFIG_QUEUE_NAME);
	}

//	@Bean
//	Queue managerPublishQueue(ConnectionFactory rabbitConnectionFactory) {
//		Connection connection = rabbitConnectionFactory.createConnection();
//		Channel channel = connection.createChannel(false);  
//		try {
//			//随机生成queue名称, 程序退出后自动删除
//			queueName = channel.queueDeclare().getQueue();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println("queue name " + queueName);
//		return new Queue(queueName);
//	}

	/**
	 * 通信管理机配置报文接收交换机
	 * 
	 * @return
	 */
	@Bean
	TopicExchange managerConfigExchange() {
		return new TopicExchange(TOPIC_MANAGER_CONFIG_EXCHAGE_NAME);
	}

	/**
	 * 通信管理机报文分发交换机
	 * 
	 * @return
	 */
	@Bean
	TopicExchange managerPublishExchange() {
		return new TopicExchange(TOPIC_MANAGER_PUBLISH_EXCHAGE_NAME);
	}

	/**
	 * 绑定配置队列和配置交换机
	 * 
	 * @param managerConfigQueue
	 * @param managerConfigExchange
	 * @return
	 */
	@Bean
	Binding binding(Queue managerConfigQueue, TopicExchange managerConfigExchange) {
		return BindingBuilder.bind(managerConfigQueue).to(managerConfigExchange).with("com.zhibo.#");
	}

}

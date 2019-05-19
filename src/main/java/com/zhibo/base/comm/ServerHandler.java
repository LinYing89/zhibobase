package com.zhibo.base.comm;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.zhibo.base.RabbitConfig;
import com.zhibo.base.SpringUtil;
import com.zhibo.base.Util;
import com.zhibo.base.comm.MessageAnalysiser.OnAnalysiserResultListener;
import com.zhibo.base.data.NetMessageAnalysisResult;
import com.zhibo.base.data.OneMessageAnalysisResult;
import com.zhibo.base.service.TestService;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;

public class ServerHandler extends ChannelInboundHandlerAdapter {

	private MessageAnalysiser messageAnalysiser;
	private TestService testService = SpringUtil.getBean(TestService.class);
	private RabbitTemplate rabbitTemplate = SpringUtil.getBean(RabbitTemplate.class);

	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	public ServerHandler() {
		messageAnalysiser = new MessageAnalysiser();
		messageAnalysiser.setAnalysiserResult(new OnAnalysiserResultListener() {
			@Override
			public void onAnalysisResult(NetMessageAnalysisResult result) {
				testService.broadcastReceived(result);
			}
		});
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		System.out.println("new channel " + ctx.channel().id());
		logger.info("new channel " + ctx.channel().id());
		//附加通信机编号属性到channel, 通信机编号为集合, 一个链接可以有多个通信机
		Attribute<Set<byte[]>> attr = ctx.channel().attr(MyServer.NETTY_CHANNEL_KEY);
		Set<byte[]> listManagerCode = attr.get();
		if(null == listManagerCode) {
			listManagerCode = new HashSet<byte[]>();
			attr.setIfAbsent(listManagerCode);
		}
		MyServer.channelGroup.add(ctx.channel());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf m = (ByteBuf) msg;
		try {
			byte[] req = new byte[m.readableBytes()];
			m.readBytes(req);
			String strMsg = Util.bytesToHexString(req);
			logger.info(strMsg);
//			ctx.writeAndFlush(Unpooled.copiedBuffer(req));
			publishReceivedMsg(req);
			
			//获取channel附加属性
			Attribute<Set<byte[]>> attr = ctx.channel().attr(MyServer.NETTY_CHANNEL_KEY);
			Set<byte[]> listManagerCode = attr.get();
			
			NetMessageAnalysisResult result = messageAnalysiser.analyisis(req);
			//将通信机编码插入属性中
			for(OneMessageAnalysisResult oneMsg : result.getListErrorResult()) {
				listManagerCode.add(oneMsg.getManagerCodeBytes());
			}
		} catch (Exception e) {
			e.printStackTrace();
			
			logger.error(e.getMessage());
		} finally {
			m.release();
			// ReferenceCountUtil.release(msg);
		}
	}
	
	/**
	 * 通过rabbitmq发布报文
	 * @param msg
	 */
	private void publishReceivedMsg(byte[] msg) {
		//发布接收到的字节数组
		String key = "";
		if(msg.length > 8) {
			switch (msg[2]) {
			case 0:
				//电力
				key = "com.zhibo.manager0";
				break;
			case 0x02:
				//信号灯
				key = "com.zhibo.manager2";
				break;
			case 0x40:
				//移动药箱
				key = "com.zhibo.manager4";
				break;
			default:
				break;
			}
		}
		rabbitTemplate.convertAndSend(RabbitConfig.TOPIC_MANAGER_PUBLISH_EXCHAGE_NAME, key, msg);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
		logger.info("channel " + ctx.channel().id().asShortText() + " is closed");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		ctx.close();
		logger.info("channel " + ctx.channel().id().asShortText() + " is closed");
	}

//	public static void main(String[] args) {
//		byte[] b = new byte[] { 0x00, 0, 1, 0 };
//		int managerNum = (b[0] << 24) | (b[1] << 16) | (b[2] << 8) | b[3];
//		System.out.println(managerNum + "?");
//	}

}

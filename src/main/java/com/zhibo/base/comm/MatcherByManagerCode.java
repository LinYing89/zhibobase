package com.zhibo.base.comm;

import java.util.Set;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelMatcher;
import io.netty.util.Attribute;

public class MatcherByManagerCode implements ChannelMatcher {

	private byte[] managerCodeBytes;
	
	public MatcherByManagerCode(byte[] managerCodeBytes) {
		this.managerCodeBytes = managerCodeBytes;
	}
	
	@Override
	public boolean matches(Channel channel) {
		if(null == managerCodeBytes || managerCodeBytes.length != 4) {
			return false;
		}
		Attribute<Set<byte[]>> attr = channel.attr(MyServer.NETTY_CHANNEL_KEY);
		Set<byte[]> listManagerCode = attr.get();
		if(null == listManagerCode) {
			return false;
		}
		for(byte[] by : listManagerCode) {
			if(by == null || by.length != 4) {
				continue;
			}
			if(managerCodeBytes[0] == by[0] && managerCodeBytes[1] == by[1] 
					&& managerCodeBytes[2] == by[2] && managerCodeBytes[3] == by[3]) {
				return true;
			}
		}
		return false;
	}

	public byte[] getManagerCodeBytes() {
		return managerCodeBytes;
	}

	public void setManagerCodeBytes(byte[] managerCodeBytes) {
		this.managerCodeBytes = managerCodeBytes;
	}

}

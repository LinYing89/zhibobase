package com.zhibo.base;

import java.math.BigDecimal;

public class Util {

	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
			stringBuilder.append(" ");
		}
		return stringBuilder.toString();
	}
	
	/**
	 * accurate to the second decimal place
	 * @param f
	 * @return
	 */
	public static float scale2(float f) {
		BigDecimal b = new BigDecimal(f);
		return b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
	}
	
	public static double scale(double value, int scale) {
		BigDecimal b = new BigDecimal(value);
		return b.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	public static int bytesToInt(byte[] by) {
		int value = 0;
		for (int i = 0; i < by.length; i++) {
			value = value << 8 | (by[i] & 0xff);
		}
		return value;
	}
	
	/**
	 * 补码表示的字节转为int
	 * @param by
	 * @return
	 */
	public static int complementalBytesToInt(byte[] by) {
		//符号位
		int symbol = (by[0] & 0xff) >> 7;
		//去掉符号位
//			int b0 = by[0] & 0xff;
//			byte bb0 = (byte)(b0 & 0x7f);
		by[0] = (byte)((by[0] & 0xff) & 0x7f);
		
		int value = 0;
		for (int i = 0; i < by.length; i++) {
			value = value << 8 | (by[i] & 0xff);
		}
		//补码取反+1获得原码, 取反后有符号位, 所有取绝对值
		value = Math.abs(~value  + 1);
		//添加符号位
		if(symbol == 1) {
			value = value * -1;
		}
		return value;
	}
	
	public static long bytesToLong(byte[] by) {
		long value = 0;
		for (int i = 0; i < by.length; i++) {
			value = value << 8 | (by[i] & 0xff);
		}
		return value;
	}
	
//	public static String encodePassword(String password) {
//		BCryptPasswordEncoder bcry = new BCryptPasswordEncoder();
//		String hashpw = bcry.encode(password);
//		return hashpw;
//	}
}
package com.server.util;

import java.util.Date;
import java.text.SimpleDateFormat;

/*
 * 日期生成类，包含静态工厂方法
 */
public class GenerateDate {

	public static String getDate() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd E HH:mm:ss--->");
		
		String date = sdf.format(new Date());
		
		return date;

	}
}

package com.server.util;

import java.util.Date;
import java.text.SimpleDateFormat;

/*
 * ���������࣬������̬��������
 */
public class GenerateDate {

	public static String getDate() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd E HH:mm:ss--->");
		
		String date = sdf.format(new Date());
		
		return date;

	}
}

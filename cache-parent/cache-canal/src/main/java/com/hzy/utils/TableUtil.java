package com.hzy.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 
 * 表数据的相关转换
 * 
 * @author  liuchangsong
 * @version  [版本号, 2018年9月28日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class TableUtil {
	/**
	 * 根据字段名获取javabean的属性名
	 * @param Column        字段名
	 * @return
	 */
	public static String zhColumn(String Column) {
		StringBuffer s1 = new StringBuffer();
		
		String[] strs = Column.split("_");
		for (int i=1;i<strs.length;i++) {
			
			String str = strs[i];
			
			char c = str.charAt(0);
			if (c >= 'a' && c <= 'z') {
				c = (char) ((int) c - 32);
				s1.append(c);
			} else
				s1.append(c);

			for (int j = 1; j < str.length(); j++) {
				char c1 = str.charAt(j);
				s1.append(c1);
			}
		}
		return strs[0]+s1.toString();
	}
	/**
	 * 根据数据库表名转换成javabean名
	 * @param table         表名
	 * @return
	 */
	public static String zhTable(String table) {
		StringBuffer s1 = new StringBuffer();
		
		String[] strs = table.split("_");
		for (int i=0;i<strs.length;i++) {
			
			String str = strs[i];
			
			char c = str.charAt(0);
			if (c >= 'a' && c <= 'z') {
				c = (char) ((int) c - 32);
				s1.append(c);
			} else
				s1.append(c);

			for (int j = 1; j < str.length(); j++) {
				char c1 = str.charAt(j);
				s1.append(c1);
			}
		}
		return s1.toString();
	}
	
	/**
	 * 将字段类型转换成java类型
	 * @param type       数据库对应的类型
	 * @param value      字段值
	 * @return           转换后的数据
	 * @see [类、类#方法、类#成员]
	 */
	public static Object zhType(String type,String value) {
		if(type.startsWith("tinyint")||type.startsWith("smallint")||
				type.startsWith("mediumint")||type.startsWith("int")) {
			return Integer.parseInt(value);
			
		}else if(type.startsWith("float")){
			return Float.parseFloat(value);
			
		}else if(type.startsWith("double")) {
			return Double.parseDouble(value);
			
		}else if(type.startsWith("date")||type.startsWith("time")||type.startsWith("year")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				return sdf.parse(value);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}
		return value;
	}
}

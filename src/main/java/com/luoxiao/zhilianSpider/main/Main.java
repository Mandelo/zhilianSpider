package com.luoxiao.zhilianSpider.main;

import java.sql.SQLException;

import com.luoxiao.zhilianSpider.utils.ParseJobTools;

/** 
* @Description: 主方法
* @author luoxiao
* @date 2017年3月25日 
*/
public class Main {
	/**
	 * @param args 设置关键字 职业 和 地区
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		ParseJobTools tools = new ParseJobTools();
		//搜索关键字:武汉 java
		tools.praseContent("JAVA", "武汉");
	}

}
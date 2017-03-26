package com.luoxiao.zhilianSpider.main;

import java.io.IOException;
import java.sql.SQLException;

import com.luoxiao.zhilianSpider.utils.ExportTools;
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
	 * @throws IOException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		ParseJobTools parseTools = new ParseJobTools();
		ExportTools exportTools = new ExportTools();
		//搜索关键字:"java" "武汉"
		//parseTools.praseContent("JAVA", "武汉");
		//从数据库查询数据,并生成eacel文件
		exportTools.exportExcel();
	}

}
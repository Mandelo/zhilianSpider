package com.luoxiao.zhilianSpider.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.luoxiao.zhilianSpider.bean.JobInfoBean;

/**
 * @Description: 解析相关的方法
 * @author luoxiao
 * @date 2017年3月25日
 */
public class ParseJobTools {

    // 抓取的总数据量
    int dataCount;

    public void praseContent(String job, String position) throws ClassNotFoundException, SQLException {
        String url = ConnectionTools.ZHILIAN_URL;
        int timeOut = 60 * 3000;
        Document document = null;
        String pageNo = null;
        /* Elements nextPageNo = null; */
        try {
            // 封装请求参数
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("jl", position);
            paramMap.put("kw", job);
            paramMap.put("sm", "0");
            paramMap.put("p", "1");

            do {
                document = Jsoup.connect(url).data(paramMap).timeout(timeOut).get();
                pageNo = document.select("div.pagesDown>ul>li>a.current").text();
                System.out.println("-------------------------查询参数:--------------->>" + paramMap+ "<<------------------------------------------");
                System.out.println("------------------------------------------------------------------抓取开始-----------------------------------------");
                //开始解析并存入数据库
                parseAndSave(document);
                //修改请求页码,爬取下一页
                paramMap.put("p", String.valueOf(Integer.parseInt(pageNo) + 1));
            } while (Integer.parseInt(pageNo) + 1 <= ConnectionTools.pages);
            //最后一页
            if (Integer.parseInt(pageNo) + 1 > ConnectionTools.pages) {
                System.out.println("------------------------------------------------>>" + ("") + "抓取结束!共抓取【"
                        + ConnectionTools.pages + "】个页面" + "存入数据【" + dataCount
                        + "】条<<-----------------------------------------------");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param 解析document对象
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void parseAndSave(Document document) throws ClassNotFoundException, SQLException {
        Connection connection;
        Statement stmt;
        Elements jobElements = document.select("table.newlist:gt(0)>tbody>tr:eq(0)");
        for (Element jobElement : jobElements) {
            JobInfoBean bean = new JobInfoBean();
            //封装bean
            bean.setCompanyName(jobElement.select("td.gsmc>a").text());
            bean.setUrl(jobElement.select("td.gsmc a").get(0).attr("href"));
            bean.setWages(jobElement.select("td.zwyx").text());
            bean.setLocation(jobElement.select("td.gzdd").text());
            bean.setDate(jobElement.select("td.gxsj span").text());
            String sql = "INSERT INTO JOB_INFO(COMPANYNAME,URL,WAGES,LOCATION,DATE)VALUES ('" + bean.getCompanyName()
                    + "','" + bean.getUrl() + "','" + bean.getWages() + "','" + bean.getLocation() + "','"
                    + bean.getDate() + "')";
            connection = new ConnectionTools().getConnection();
            stmt = connection.createStatement();
            Set<String> blacklistSet1 = getSet(ConnectionTools.blackList);
            //过滤黑名单公司
            if (blacklistSet1.contains(bean.getCompanyName())) {
                System.out.println("已过滤 -----\\(▔▽▔)/ \\(▔▽▔)/ \\(▔▽▔)/=====>>  " + bean.getCompanyName());
            } else {
                stmt.execute(sql);
                dataCount++;
                System.out.println("存入到数据库成功！" + bean);
            }
            stmt.close();
            connection.close();
        }

    }
 
    /**
     * @Description: 黑名单列表转化为Set
     * @param:   黑名单列表
     * @return:  黑名单Set
     */
    public Set<String> getSet(String[] list) {
        Set<String> blacklistSet = new HashSet<String>();
        for (String s : list) {
            blacklistSet.add(s);
        }
        return blacklistSet;
    }
}

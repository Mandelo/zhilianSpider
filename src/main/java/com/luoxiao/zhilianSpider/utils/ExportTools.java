package com.luoxiao.zhilianSpider.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.common.usermodel.Hyperlink;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CreationHelper;

/**
 * @Description: 生成excel文件相关
 * @author luoxiao
 * @date 2017年3月27日
 */
public class ExportTools {

    Connection connection;
    Statement stmt;

    public void exportExcel() throws IOException {
        String sql = "SELECT * FROM job_info;";
        try {
            connection = new ConnectionTools().getConnection();
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            @SuppressWarnings("resource")
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("zhilian");
            CreationHelper creationHelper = workbook.getCreationHelper();
            // 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
            HSSFRow row = sheet.createRow((short) 0);
            HSSFCell cell;

            // url样式
            HSSFCellStyle urlStyle = workbook.createCellStyle();
            HSSFFont urlFont = workbook.createFont();
            // 设置下划线
            urlFont.setUnderline(HSSFFont.U_SINGLE);
            // 设置字体颜色
            urlFont.setColor(HSSFColor.BLUE.index);
            urlStyle.setFont(urlFont);

            cell = row.createCell((short) 0);
            cell.setCellValue("编号");
            cell = row.createCell((short) 1);
            cell.setCellValue("公司名称");
            cell = row.createCell((short) 2);
            cell.setCellValue("详情页");
            cell = row.createCell((short) 3);
            /* link.setAddress(rs.getString("url")); */
            cell.setCellValue("月薪:元/月");
            cell = row.createCell((short) 4);
            cell.setCellValue("地址");
            cell = row.createCell((short) 5);
            cell.setCellValue("发布日期");
            // 写入数据,i为行号
            int i = 1;
            System.out
                    .println("---------------------------------------开始写入数据----------------------------------------------");
            while (rs.next()) {
                row = sheet.createRow(i);
                cell = row.createCell(0);
                cell.setCellValue(rs.getString("id"));
                cell = row.createCell(1);
                cell.setCellValue(rs.getString("companyName"));
                // url
                cell = row.createCell(2);
                Hyperlink link = creationHelper.createHyperlink(Hyperlink.LINK_URL);
                cell.setCellValue(rs.getString("url"));
                link.setAddress(rs.getString("url"));
                cell.setHyperlink((org.apache.poi.ss.usermodel.Hyperlink) link);
                cell.setCellStyle(urlStyle);
                cell = row.createCell(3);
                cell.setCellValue(rs.getString("wages"));
                cell = row.createCell(4);
                cell.setCellValue(rs.getString("location"));
                cell = row.createCell(5);
                cell.setCellValue(rs.getString("date"));
                i++;
            }
            String date = formatDate(new Date());
            String outputFile = "C:\\智联招聘  " + date + ".xls";
            FileOutputStream FOut = new FileOutputStream(outputFile);
            workbook.write(FOut);
            FOut.flush();
            FOut.close();
            stmt.close();
            connection.close();
            System.out.println("----------------------------------------文件" + outputFile
                    + "创建完成----------------------------------");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ExportTools e = new ExportTools();
        e.exportExcel();
    }

    /**
     * @Description: 格式化当前日期
     * @param:
     * @return:
     * @throws
     */
    public String formatDate(Date date) {
        date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(date);
        return result;
    }

}

package com.xingli.utils;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.*;

/**
 * @ProjectName: workutils
 * @Package: com.xingli.utils
 * @Author: xingli12
 * @Description:
 * @Date: Created in 2018-05-31 17:55
 * @Modified By:
 * @UpdateDate:
 * @Version:
 */
public class ExcelUtil {
    public static void read(String path){

        File file = new File(path);
        InputStream inputStream = null;
        Workbook workbook = null;
        try {
            inputStream = new FileInputStream(file);
            workbook = WorkbookFactory.create(inputStream);
            inputStream.close();
            //工作表对象
            Sheet sheet = workbook.getSheetAt(1);
            //总行数
            int rowLength = sheet.getLastRowNum() + 1;
            //工作表的行
            Row row = sheet.getRow(0);
            //第一行的列数
            int colLength = row.getLastCellNum();

            //得到指定的单元格
            Cell cell = row.getCell(0);
            //得到单元格样式
            CellStyle cellStyle = cell.getCellStyle();
//            System.out.println("行数：" + rowLength + "列数：" + colLength);
            for (int i = 0; i < rowLength; i++) {
                //获取一行记录
                row = sheet.getRow(i);

                for (int j = 0; j < row.getLastCellNum(); j++) {
                    cell = row.getCell(j);
                    Cell firstCell = row.getCell(0);
                    if(cell.getStringCellValue() != null && cell.getStringCellValue() != "" && j !=0){

                      System.out.println(firstCell.getStringCellValue() + ","+ cell.getStringCellValue().trim() );
                    }
                }
//                System.out.println();
            }
//            //将修改好的数据保存
//            OutputStream out = new FileOutputStream(file);
//            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        String path = "D:\\工作\\症状\\常见病症状.xlsx";
        read(path);
    }
}

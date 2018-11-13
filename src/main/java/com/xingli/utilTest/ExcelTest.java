package com.xingli.utilTest;


import com.xingli.utils.ListSaveUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;


import java.io.File;
import java.io.FileInputStream;

import java.io.InputStream;
import java.util.ArrayList;


/**
 * projectName  workutils
 * package com.xingli.utilTest
 *
 * @author xingli12
 * description
 * @version v1
 * @date Created in 2018-10-24 16:45
 * modified By
 * updateDate
 */
public class ExcelTest {
    public static void main(String[] args){

        String path = "D:/工作/20181017疾病关键因素/社区诊断名辅诊节点汇总0907(危重转诊疾病确认).xlsx";
        File file = new File(path);
        InputStream inputStream = null;
        Workbook workBook = null;
        try {
            ArrayList<String> arrayList = new ArrayList<>();
            inputStream = new FileInputStream(file);
            workBook = WorkbookFactory.create(inputStream);
            int numberOfSheets = workBook.getNumberOfSheets();
            // sheet工作表
            for (int i = 0; i < numberOfSheets; i++) {
                Sheet sheet = workBook.getSheetAt(i);
                // 获取当前Sheet的总行数数
                System.out.printf("当前sheet为: %s\n",sheet.getSheetName());
                int rowsOfSheet = sheet.getPhysicalNumberOfRows();
//                System.out.println("当前表格的总行数:" + rowsOfSheet);
                for (int j = 0; j < rowsOfSheet; j++) {
                    Row row = sheet.getRow(j);
                    if (row == null) {
                        continue;
                    } else {
                        int rowNum = row.getRowNum();

                        //当前列
                        int numberOfCells = row.getPhysicalNumberOfCells();
//                        System.out.println("当前行:" + rowNum);
//                        System.out.println(numberOfCells);
                        for (int c = 0; c < 20; c++) {
                            Cell cell = row.getCell(c);
                            if (cell == null) {
                                continue;
                            } else {
                                if(StringUtils.isNotBlank(cell.getStringCellValue())){
                                    String word = cell.getStringCellValue().trim();
                                    arrayList.add(sheet.getSheetName()+"##"+word);
//                                    if(TestBadWords(word)){
//                                        arrayList.add(word);
//                                    }
                                }
                            }
                        }
                    }
                }
            }
            ListSaveUtils.writeFileContext(arrayList,"D:/工作/20181017疾病关键因素/list_all1.txt");
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                workBook.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                inputStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }


    protected static boolean TestBadWords(String word){
        String pattern = ".*(rrr|ss|tt).*";
        if(StringUtils.isNotBlank(word) && word.matches(pattern)){
            return true;
        }
        return false;
    }
}

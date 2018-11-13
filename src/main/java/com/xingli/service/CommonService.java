package com.xingli.service;

import com.xingli.utils.HTMLSpirit;
import com.xingli.utils.TableUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName: workutils
 * @Package: com.xingli.service
 * @Author: xingli12
 * @Description:
 * @Date: Created in 2018-06-21 15:52
 * @Modified By:
 * @UpdateDate:
 * @Version:
 */
public class CommonService {

    private String tempHtmlContent ="";
    private String tempContent ="";
    /**
     * 将html内容转换为txt内容
     *
     * @param file
     * @param htmlContent
     * @throws Exception
     */
    private void convertHtmlToTxt(File file, String htmlContent, String path) throws Exception {
        Map<String, Integer> map = new HashMap<>();
        String replaceContent = TableUtil.replaceTable(htmlContent,"弢","絾");
        ArrayList<String> stringArrayList = TableUtil.splitString(replaceContent);

        ArrayList<String> arrayList1 = new ArrayList<>();

        for (String partString: stringArrayList
                ) {
            if(!partString.startsWith("弢")){
                String[] htmltag = HTMLSpirit.delHTMLTag(partString);
                arrayList1.addAll(Arrays.asList(htmltag));
            }else {
                arrayList1.add(TableUtil.unrepalceTable(partString,"弢","絾").replace("\r\n",""));
            }
        }
        //删除html标签
        String[] htmlContents = new String[arrayList1.size()];
        htmlContents = arrayList1.toArray(htmlContents);
        htmlContent = HTMLSpirit.packageDiv(htmlContents, path);
        tempHtmlContent = htmlContent;
        HTMLSpirit.writeTxt(file, htmlContent);
        HTMLSpirit.packageStr(map, htmlContent);
        HTMLSpirit.sortMap(map);

    }



    public static void main(String[] args) throws Exception {
        File file = new File("D:\\disk2\\resource\\进行性肌营养不良20180517\\进行性肌营养不良(progressivemuscular dystrophy, PMD).html");
        String path = "D:\\disk2\\resource\\进行性肌营养不良20180517\\";
        String str = HTMLSpirit.readfile(file.getPath());
        CommonService commonService = new CommonService();
        commonService.convertHtmlToTxt(file,str,path);
        System.out.println(commonService.tempHtmlContent);
    }
}

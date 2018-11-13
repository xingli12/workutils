package com.xingli.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title： TableUtil
 * @Package： com.xingli.utils
 * @author： lx
 * @Date 2018-06-20 21:24
 * @Description:
 * @Modified By:
 * @UpdateDate:
 * @Version:
 */
public class TableUtil {

    public static void main(String args[]) throws Exception {
        StringBuffer buffer = new StringBuffer();
        BufferedReader bf= new BufferedReader(new FileReader("D:\\工作\\20180620标注系统处理图表\\table.txt"));
        String s = null;
        while((s = bf.readLine())!=null){//使用readLine方法，一次读一行
            buffer.append(s.trim());
        }
        String content = buffer.toString();
//
//        ArrayList<String> arrayList = findTable(xml);
//
//        for(String a : arrayList){
//            System.out.println(a);
//        }

//        String content = "<!DOCTYPE html><head><meta></head><table><p>继之肌肉组织由纤维组织或脂肪组织所替代</p><table><p>内嵌</p></table></table><p>药伙伴罕见病公益社区为罕见病患者提供疾病咨询，就医指南，康复方案以及用药指南</p><table><p>肌活检显示肌纤维坏死与大小不等</p></table></html>";
//        String str = content.replace("<table", "塍").replace("/table>", "絾");
        String str = replaceTable(content,"弢","絾");

        ArrayList<String> arrayList = findTable(str);
        for (String table: arrayList
             ) {
            System.out.println(table);
        }

        System.out.println("===========================");

        String[] listMap = getTable(str);
        for (String map: listMap
             ) {
            System.out.println(map);
        }
        System.out.println("===========================");

        ArrayList<String> splitList = splitString(str);
        for (String string: splitList
             ) {
            System.out.println(string);
        }

        System.out.println("=====================");
        System.out.println(content);
        System.out.println(replaceTable(content,"弢","絾"));

    }

    public static  String replaceTable(String content,String fir,String sec){
        String str = content.replace("<table", fir).replace("/table>", sec);
        return str;
    }

    public static String unrepalceTable(String content,String fir,String sec){
        String str = content.replace(fir,"<table").replace( sec,"/table>");
        return str;
    }

    public static ArrayList<String> findTable(String content){

        ArrayList<String>  word=new ArrayList<String>();
        int m=0,n=0;
        int count=0;
        for(int i=0;i<content.length();i++){
//            System.out.println(str.charAt(i));
            if(content.charAt(i)=='弢'){
                if(count==0){
                    m=i;
                }
                count++;
            }
            if(content.charAt(i)=='絾'){
                count--;
                if(count==0){
                    n=i;
                    word.add(content.substring(m,n+1));
                }
            }
        }

        return word;
    }

    //获取table子串
    public static String[] getTable(String content){

        ArrayList<String> list = findTable(content);
        String[] num = new String[list.size()];
        for (int i = 0; i < num.length; i++) {
            String s = list.get(i);
            int a = content.indexOf(s);
            int b = a + s.length()-1;
            String tables = a +":::"+ b;
            num[i]= tables;
        }
        return num;
    }

    //处理字符串切分
    public static ArrayList<String> splitString(String content) {

        ArrayList<String> splitList = new ArrayList<>();

        String[] listMap = getTable(content);
//        System.out.println(listMap.length);
        if (0 == listMap.length) {
            splitList.add(content);
        } else {
            for (int i = 0; i < listMap.length; i++) {
                int a = Integer.valueOf(listMap[i].split(":::")[0]);
                int b = Integer.valueOf(listMap[i].split(":::")[1]);
//            System.out.println(a + "->" + b);
                if (i == 0) {
                    String tmpSub = content.substring(0, a);
//                System.out.println(tmpSub);
                    splitList.add(tmpSub);

                } else {
                    String tmpSub = content.substring(Integer.valueOf(listMap[i - 1].split(":::")[1]) + 1, a);
//                System.out.println(tmpSub);
                    splitList.add(tmpSub);
                }
//            System.out.println(content.substring(a,b+1));
                splitList.add(content.substring(a, b + 1));
                if (i == listMap.length - 1) {
//                System.out.println(content.substring(b+1,content.length()));
                    splitList.add(content.substring(b + 1, content.length()));
                }
            }
        }
//        System.out.println("=======================");
            return splitList;
        }

}

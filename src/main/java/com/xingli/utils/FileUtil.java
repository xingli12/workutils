package com.xingli.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @ProjectName: workutils
 * @Package: com.xingli.utils
 * @Author: xingli12
 * @Description:
 * @Date: Created in 2018-05-30 09:14
 * @Modified By:
 * @UpdateDate:
 * @Version:
 */
public class FileUtil {

    /**
     * @param path
     * @Description: 非递归读取目录下所有文件名
     * @return: java.util.List<java.lang.String>
     * @Date: Created in 9:24 2018/5/30/0030
     * @Modified By: xingli12
     */
    public static List<String> scanFiles(String path) {
        //文件列表
        List<String> filePaths = new ArrayList<String>();
        //将目录加入链表
        LinkedList<File> list = new LinkedList<File>();
        File dir = new File(path);
        //返回该目录下的抽象路径名的文件和目录
        File[] file = dir.listFiles();

        for (int i = 0; i < file.length; i++) {
            if (file[i].isDirectory()) {
                // 把第一层的目录，全部放入链表
                list.add(file[i]);
            }
            //将第一层文件加入列表
            filePaths.add(file[i].getAbsolutePath());
        }
        // 循环遍历链表
        while (!list.isEmpty()) {
            // 把链表的第一个记录删除
            File tmp = list.removeFirst();
            // 如果删除的目录是一个路径的话
            if (tmp.isDirectory()) {
                // 列出这个目录下的文件到数组中
                file = tmp.listFiles();
                if (file == null) {// 空目录
                    continue;
                }
                // 遍历文件数组
                for (int i = 0; i < file.length; ++i) {
                    if (file[i].isDirectory()) {
                        // 如果遍历到的是目录，则将继续被加入链表
                        list.add(file[i]);
                    }
                    filePaths.add(file[i].getAbsolutePath());
                }
            }
        }
        return filePaths;
    }

    /**
     * 以行为单位读取文件，写到另一个文件中
     */
    public static void loadFile(String inFileName, String outFileName) {
        File inFile = new File(inFileName);
        File outFile = new File(outFileName);
        if (!outFile.exists()) {
            try {
                outFile.createNewFile(); // 创建文件
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedReader reader = null;
            BufferedWriter writer = null;
            try {
                //  System.out.println("以行为单位读取文件内容，一次读一整行：");
                InputStreamReader isr = new InputStreamReader(new FileInputStream(inFile), "GBK"); //ANSI编码
                OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8");
                reader = new BufferedReader(isr);
                writer = new BufferedWriter(osw);
                String tempString = null;
                int line = 1;
                // 一次读入一行，直到读入null为文件结束
                while ((tempString = reader.readLine()) != null) {
                    // 显示行号
//                tempString = new String(tempString.getBytes("GB2312"), "UTF-8");
//                System.out.println(file.getName().split("\\.")[0] + '\t' + tempString);
                    writer.write(tempString);
                    writer.newLine();
                    line++;
                }
                reader.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                    }
                }
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e1) {
                    }
                }
            }
        }else {
            System.out.println("你好，输出文件已存在！！请确认入参！！");
        }
    }

    /**
     * 以行为单位读取文件，写到另一个文件中
     */
    public static void loadFile(String inFileName, String inCode, String outFileName, String outCode) {
        System.out.println("输入文件地址为：" + inFileName);
        System.out.println("输入文件编码格式为：" + inCode);
        System.out.println("输出文件地址为：" + outFileName);
        System.out.println("输出文件编码格式为：" + outCode);
        File inFile = new File(inFileName);
        File outFile = new File(outFileName);
        if (!outFile.exists()) {
            try {
                outFile.createNewFile(); // 创建文件
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedReader reader = null;
            BufferedWriter writer = null;
            try {
                //  System.out.println("以行为单位读取文件内容，一次读一整行：");
                InputStreamReader isr = new InputStreamReader(new FileInputStream(inFile), inCode); //ANSI编码
                OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(outFile), outCode);
                reader = new BufferedReader(isr);
                writer = new BufferedWriter(osw);
                String tempString = null;
                int line = 1;
                // 一次读入一行，直到读入null为文件结束
                while ((tempString = reader.readLine()) != null) {
                    // 显示行号
//                tempString = new String(tempString.getBytes("GB2312"), "UTF-8");
//                System.out.println(file.getName().split("\\.")[0] + '\t' + tempString);
//                    tempString.trim();
                    writer.write(tempString);
                    writer.newLine();
                    line++;
                }
                reader.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                    }
                }
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e1) {
                    }
                }
            }
        }else {
            System.out.println("== == == == == == == == ==");
            System.out.println("你好，输出文件已存在！请确认参数设置！！");
            System.out.println("== == == == == == == == ==");
        }
    }

        public static void main (String[]args){

//        List<String> list = scanFiles("D:\\工作\\症状");

//            loadFile("D:\\工作\\症状\\感染科\\脂肪肝.csv", "GBK", "D:\\工作\\症状\\感染科\\脂肪肝.txt", "GBK");
            loadFile("D:\\工作\\症状\\20180531\\update_symptom.txt", "UTF-8", "D:\\工作\\症状\\20180531\\symptom.txt", "UTF-8");
        }
    }

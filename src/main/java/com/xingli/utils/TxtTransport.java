package com.xingli.utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @ProjectName: springbootdemo
 * @Package: controller.model
 * @Author: xingli12
 * @Description:
 * @Date: Created in 2018-05-25 13:00
 * @Modified By:
 * @UpdateDate:
 * @Version:
 */
public class TxtTransport {
    public static void main(String[] args) throws Exception {
//        readFileByLines("D:\\工作\\典型症状新\\感染科\\肝硬化.csv");
// List<String> list = getFileName("D:\\工作\\症状\\diyici");
        File file = new File("D:\\工作\\症状\\filelist.txt");
        BufferedReader reader = null;
        //  System.out.println("以行为单位读取文件内容，一次读一整行：");
        reader = new BufferedReader(new FileReader(file));
        String tempString = null;
        int line = 1;
        // 一次读入一行，直到读入null为文件结束
        while ((tempString = reader.readLine()) != null) {
            // 显示行号
            readFileByLines(tempString);
        }
        reader.close();
    }
    public static void mergeFiles(String outFile, String[] files) {
        FileChannel outChannel = null;
//        out.println("Merge " + Arrays.toString(files) + " into " + outFile);
        try {
            outChannel = new FileOutputStream(outFile).getChannel();
            for(String f : files){
                FileChannel fc = new FileInputStream(f).getChannel();
                ByteBuffer bb = ByteBuffer.allocate(4096);

                while(fc.read(bb) != -1){
                    bb.flip();
                    outChannel.write(bb);
                    bb.clear();
                }
                fc.close();
            }
//            out.println("Merged!! ");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {if (outChannel != null) {outChannel.close();}} catch (IOException ignore) {}
        }
    }
    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static void readFileByLines(String fileName) {
        File file = new File(fileName);

        BufferedReader reader = null;
        try {
            //  System.out.println("以行为单位读取文件内容，一次读一整行：");
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "GBK"); //ANSI编码
//            OutputStreamWriter osw = new OutputStreamWriter(new )
            reader = new BufferedReader(isr);
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
//                tempString = new String(tempString.getBytes("GB2312"), "UTF-8");
                System.out.println(file.getName().split("\\.")[0] + '\t' + tempString);
                line++;
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }
    /** *
     * @Description:
     * @param path
     * @return: java.util.List<java.lang.String>
     * @Date: Created in 9:00 2018/5/30/0030
     * @Modified By: xingli12
     */
    public static List<String> getFileName(String path) {
        List<String> list = new ArrayList<>();
        File f = new File(path);
        if (!f.exists()) {
            System.out.println(path + " not exists");
        }
        File fa[] = f.listFiles();
        for (int i = 0; i < fa.length; i++) {
            File fs = fa[i];
            if (fs.isDirectory()) {
//                System.out.println(fs.getName() + " [目录]");
                getFileName(fs.getPath());
            } else {
                Pattern p = Pattern.compile(".*\\.csv");
                if(p.matcher(fs.getName()).matches()) {
                    System.out.println(fs.getPath());
//                    list.add(fs.getPath());
                }
            }
        }
//        System.out.println("list " + list);
        return list;
    }
}


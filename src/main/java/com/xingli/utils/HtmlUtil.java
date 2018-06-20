package com.xingli.utils;

/**
 * @ProjectName: workutils
 * @Package: com.xingli.utils
 * @Author: xingli12
 * @Description:
 * @Date: Created in 2018-06-20 20:53
 * @Modified By:
 * @UpdateDate:
 * @Version:
 */


import java.io.BufferedReader;
        import java.io.File;
        import java.io.FileInputStream;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.util.ArrayList;
        import java.util.List;

        import org.htmlparser.NodeFilter;
        import org.htmlparser.Parser;
        import org.htmlparser.filters.NodeClassFilter;
        import org.htmlparser.filters.OrFilter;
        import org.htmlparser.tags.TableColumn;
        import org.htmlparser.tags.TableRow;
        import org.htmlparser.tags.TableTag;
        import org.htmlparser.util.NodeList;
        import org.htmlparser.util.ParserException;

public class HtmlUtil {

    public static void main(String[] args) throws IOException {
        //要读取的html文件路径
        File f = new File("D:\\disk2\\resource\\进行性肌营养不良20180517\\进行性肌营养不良(progressivemuscular dystrophy, PMD).html");
        // 输入流
        InputStreamReader isr1 = new InputStreamReader(new FileInputStream(f), "UTF-8");
        BufferedReader br = new BufferedReader(isr1);
        // 获取html转换成String
        String s;
        String AllContent = "";
        //按行读取
        while ((s = br.readLine()) != null) {
            AllContent = AllContent + s;
        }
        // 使用后HTML Parser 控件
        Parser myParser;
        NodeList nodeList = null;
        myParser = Parser.createParser(AllContent, "gbk");
        NodeFilter tableFilter = new NodeClassFilter(TableTag.class);
        OrFilter lastFilter = new OrFilter();
        lastFilter.setPredicates(new NodeFilter[] { tableFilter });
        try {
            // 获取标签为table的节点列表
            nodeList = myParser.parse(lastFilter);
            // 循环读取每个table
            for (int i = 0; i <=nodeList.size(); i++) {
                if (nodeList.elementAt(i) instanceof TableTag) {
                    TableTag tag = (TableTag) nodeList.elementAt(i);
                    TableRow[] rows = tag.getRows();
                    System.out.println("----------------------table  " + i + "--------------------------------");
                    // 循环读取每一行
                    for (int j = 0; j < rows.length; j++) {
                        TableRow tr = (TableRow) rows[j];
                        TableColumn[] td = tr.getColumns();
                        // 读取每行的单元格内容
                        for (int k = 0; k < td.length; k++) {
                            String b = td[k].getStringText();
                            System.out.println(b);
                        }
                    }
                }
            }

        } catch (ParserException e) {
            e.printStackTrace();
        }
    }
}


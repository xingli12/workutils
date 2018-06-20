package com.xingli.utils;

/**
 * @program: springboot-first-application
 * @Author: xingli12
 * @Description:
 * @Date: Created in 2018-05-17 15:26
 * @Modified By:
 * @UpdateDate:
 * @Version:
 */
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符处理类
 *
 * @author sbwang@iflytek.com
 * @lastModified
 * @history
 */

public class StringUtil extends StringUtils {

    /**
     * 一位空格字符
     * @return
     */
    public static final String BLANK = " ";
    /**
     * 无空格字符
     * @return
     */
    public static final String REALBLANK = "";

    /**
     *
     * 替换非法字符
     *
     * @param str
     * @return
     * @author sbwang@iflytek.com
     * @created 2013-5-29 下午09:49:10
     * @lastModified
     * @history
     */
    public static String replace(String str) {
        if (StringUtils.isNotEmpty(str)) {
            /*
             * str = str.replace("'", "''").replace("]", "]]").replace("&",
             * "chr(38)").replace("%", "chr(37)").replace("\\", "chr(92)")
             * .replace("\"", "chr(34)").replace("_", "chr(95)");
             */
            str = str.replace("'", "''").replace("%", "\\%")
                    .replace("\\", "\\\\").replace("_", "\\_");
        }
        return str;
    }

    /**
     *
     *  去掉尾部全部特定字符串
     *  @param str
     *  @param removeStr
     *  @return
     *  @author xkfeng@iflytek.com
     *  @created 2013-7-19 上午09:11:30
     *  @lastModified
     *  @history
     */
    public static String removeEndStr(String str,String removeStr){
        String str2 = StringUtils.removeEnd(str, removeStr);
        while(!str2.equals(str)){
            str = str2;
            str2 = StringUtils.removeEnd(str, removeStr);
        }
        return str2;
    }

    /**
     *  去零
     *  @param code 原编码
     *  @param length “0”的长度
     *  @return 去零后的编码
     *  @author jianye
     *  @created 2014-6-18 09:32:10
     *  @lastModified
     *  @history
     */
    public static String toNewsCode(String code, int length) {
        if (StringUtils.isNotEmpty(code)) {
            boolean temp = false;
            code = code.replace("\r", "");
            code = code.replace("\n", "");
            String template = String.format("%0" + length + "d", 0);
            while(!temp){
                if (code.length() > length) {
                    String newstr = code.substring(code.length() - length);
                    if (template.equals(newstr))
                        code = code.substring(0, code.length() - length);
                    else
                        temp = true;
                }else{
                    temp = true;
                }
            }
        }
        return code;
    }

    /**
     *  根绝关键词数组和需要查询表的列名数组拼接查询的sql条件
     *  @param value 存放参数的map
     *  @param searchKeys 关键词数组
     *  @param columNames 列名数组
     *  @return 查询条件的sql语句
     *  @author ycli7
     *  @created 2014年9月4日 上午9:14:16
     *  @lastModified
     *  @history
     */
    public static String makeSearchSql(Map<String,Object> value, String[] searchKeys, String[] columNames){
        if(searchKeys==null||columNames==null){
            return "";
        }
        StringBuffer sb = new StringBuffer(100);
        for(int index=0;index<searchKeys.length;index++){
            if(isBlank(searchKeys[index])){
                continue;
            }
            sb.append(" and (");
            for(String col:columNames){
                sb.append(col).append(" like :key").append(index).append(" or ");
            }
            //去除最后一个“or”，如果有的话
            int last=sb.lastIndexOf("or");
            if(last>0){
                sb.delete(last, last+2);
            }
            sb.append(")");
            value.put("key"+index, "%"+searchKeys[index]+"%");
        }
        return sb.toString();
    }


    /**
     *正则,去除字符串中的空格、回车、换行符、制表符
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            //\s 空白字符 \t制表符 \r回车 \n换行
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static void main(String[] args) {
        String str = "Hell  o  ";
        String newStr = replaceBlank(str);
        System.out.println(newStr);
    }
}

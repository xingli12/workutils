package com.xingli.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Title RegexDemo
 * Package com.xingli.regex
 *
 * @author lx
 * @version v1
 * **
 * ----------Dragon be here!----------/
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃神兽保佑
 * 　　　　┃　　　┃代码无BUG！
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━神兽出没━━━━━━
 * @date 2018-09-28 00:09
 * description
 * modifie
 * updateDate
 */
public class RegexDemo {

    public static void main(String[] args) {
        String pattern = ".*[(复查)|(测试)].*";
        Pattern p = Pattern.compile(pattern);
        String word = "你好，我想一下测试";
        boolean matches = word.matches(pattern);
        System.out.println(matches);
    }
}

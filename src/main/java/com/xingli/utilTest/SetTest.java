package com.xingli.utilTest;

import sun.applet.Main;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author xingli12
 * @projectName workutils
 * @package com.xingli.utilTest
 * @description
 * @date Created in 2018-07-27 11:13
 * @modified By
 * @updateDate
 */
public class SetTest {
    public static void main(String[] args) {
    String string = "[25cb0801db17acb@an, 64:DB:43:D5:0A:5B@mac]";
        String replace = string.replace("[", "");
        System.out.println(replace);
        }

}

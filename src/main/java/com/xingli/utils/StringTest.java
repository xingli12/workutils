package com.xingli.utils;

/**
 * @ProjectName: workutils
 * @Package: com.xingli.utils
 * @Author: xingli12
 * @Description:
 * @Date: Created in 2018-05-30 13:52
 * @Modified By:
 * @UpdateDate:
 * @Version:
 */
public class StringTest {
    public static void main(String[] args) {
        String string = "bhke";

        String[] arr = string.split("b");
        int i = 0;
        for (String s:arr
             ) {
            System.out.println("数组 " + i + ": " + s);
            i++;
        }
    }


}

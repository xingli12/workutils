package com.xingli.utils;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;

/**
 * @author xingli12
 * @projectName workutils
 * @package com.xingli.utils
 * @description
 * @date Created in 2018-07-23 18:08
 * @modified By
 * @updateDate
 */

public class RandomSortTest {

    private static final int SIZE = 30;
    public static void main(String[] args) {
        int[] arr = new int[SIZE];
        for(int i=0;i<SIZE;i++){
            arr[i]=i;
        }
        sort(arr);
        System.out.println();
        List<Integer> integers = Ints.asList(arr);
        System.out.println(Lists.partition(integers,4));


    }


    //重新排列
    public static void sort(int[] arr){
        printArr(arr);
        System.out.println();
        for(int i=0;i<SIZE;i++){
            Random random = new Random();
            int p = random.nextInt(i+1);
            int tmp = arr[i];
            arr[i] = arr[p];
            arr[p] = tmp;
        }
        printArr(arr);
    }

    //打印
    public static void printArr(int[] arr){
        for(int i=0;i<SIZE;i++){
            System.out.print(arr[i]+" ");
        }
    }
}


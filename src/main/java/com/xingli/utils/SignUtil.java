package com.xingli.utils;


import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: springboot-first-application
 * @Author: xingli12
 * @Description:
 * @Date: Created in 2018-05-16 10:07
 * @Modified By:
 * @UpdateDate:
 * @Version:
 */
public class SignUtil {

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        //入参url
       String url = args[0];

       String[] arr = url.split("\\?");
        String host = arr[0];
        String[] arr1 = arr[1].split("\\&");
        for (String a:arr1
             ) {
            if(a.split("=").length==2){
                 map.put(a.split("=")[0],a.split("=")[1]);
            }else{
                 map.put(a.split("=")[0],"");
            }
        }
        //密钥(根据实际修改)
        String appSecret = args[1];
        if(StringUtils.isBlank(appSecret)){
            appSecret = "123456";
        }

        String accessToken = map.get("access_token");
        //设备id 用作个性化检索标识
        String deviceId = map.get("deviceId");
        //流水号，每次请求不同  ，可使用32位UUID
        String transactionId = map.get("transactionId");
        String appKey = map.get("appKey");
        String sign = map.get("sign");
            String hostAndParam = generateSign(host,appSecret,map);
            String sign1 = MD5Util.encode(hostAndParam + appSecret);
            System.out.println(sign1);
            System.out.println(sign);
            System.out.println("新生成的url为：");
            System.out.println(hostAndParam+"&sign="+sign1);

//            boolean isTrue = compareKeyI(sign1,sign);
//
//            System.out.println(isTrue);
       }



    public static String generateSign(String host,String appSecret,Map<String,String> map){

        //排查sing字段，自然排序、转换操作得到  key1=value1&key2=value2
        String parStr = map.entrySet().stream().filter(entry -> !"sign".equals(entry.getKey()))
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("&"));
        String hostAndParam =host+"?"+parStr;
        System.out.println(hostAndParam);

        return hostAndParam;
    }
    /**
     * 验证加密字段（id）
     *
     * @param sign1
     * @param sign
     * @return
     */
    private static boolean compareKeyI(String sign1, String sign) {

        if (sign1.equals(sign)) {
            return true;
        } else {
            return false;
        }
    }

}

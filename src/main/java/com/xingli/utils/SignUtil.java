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
        //参数列表
//        String url = "http://172.31.234.12:8200/v1.0/search/search/contents?access_token=9983bbaa-0012-4c30-a64e-a9cab5a5ee76&appKey=assist_app_1&deviceId=1234567654321&fileId=1764&sourceCode=disease&transactionId=123456987456312145698745&sign=4607a8e24787f94320fba8c2f22dc902";
//        String url = "http://172.31.234.12:8200/v1.0/diagnose/diagnose/test?access_token=9983bbaa-0012-4c30-a64e-a9cab5a5ee76&appKey=assist_app_1&deviceId=1234567654321&icd10=N18.0&transactionId=123456987456312145698746&sign=0bcc5428a550dbb96eaafc50f75f18ba";
//        String url = "http://172.31.234.12:8200/v1.0/diagnose/diagnose/diff?access_token=9983bbaa-0012-4c30-a64e-a9cab5a5ee76&appKey=assist_app_1&deviceId=132151&icd10s=N18.0,D86.0&transactionId=123456987456312145698745&sign=e94f697c5459193b1123f27351362185";
       String url = "http://172.31.234.12:8200/v1.0/diagnose/odiagnose/diseaseSymptom?icd=J06.9&access_token=25168375-fda8-46b8-9415-415b3034abfa&appKey=assist_app_1&deviceId=1234567654321&locationId=001&name=上呼吸道感染&transactionId=1234569874563121456987451&sign=daf3f1d9496ba09b2bd9f3733c7f2070";
       String[] arr = url.split("\\?");
        String host = arr[0];
        String[] arr1 = arr[1].split("\\&");
        for (String a:arr1
             ) {
          map.put(a.split("=")[0],a.split("=")[1]);
        }
        String appSecret = "123456";
        String accessToken = map.get("access_token");
        //设备id 用作个性化检索标识
        String deviceId = map.get("deviceId");
        //流水号，每次请求不同  ，可使用32位UUID
        String transactionId = map.get("transactionId");
        String appKey = map.get("appKey");
        String sign = map.get("sign");
        //appKey和sign不能为空值或者空字符串
        if(StringUtils.isBlank(accessToken) || StringUtils.isBlank(deviceId) ||
                StringUtils.isBlank(transactionId) || StringUtils.isBlank(appKey) || StringUtils.isBlank(sign))
        {
            System.out.println("400001,请求参数无效");
        }else{
            String hostAndParam = generateSign(host,appSecret,map);
            String sign1 = MD5Util.encode(hostAndParam + appSecret);
            System.out.println(sign1);
            System.out.println(sign);
            System.out.println(hostAndParam+"&sign="+sign1);
            boolean isTrue = compareKeyI(sign1,sign);
            System.out.println(isTrue);
       }


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

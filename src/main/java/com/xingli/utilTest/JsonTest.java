package com.xingli.utilTest;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * projectName  workutils
 * package com.xingli.utilTest
 *
 * @author xingli12
 * description
 * @version v1
 * @date Created in 2018-10-29 11:03
 * modified By
 * updateDate
 */
public class JsonTest {
    public static void main(String[] args) throws Exception {
        File file=new File("D:\\工作\\20181029疾病映射核对\\simple_drug_dic(add).txt");
        String content= FileUtils.readFileToString(file,"utf-8");
        JSONObject jsonObject = JSON.parseObject(content);
        Collection<Object> values = jsonObject.values();
        HashSet<String> objectHashSet = new HashSet<>();
        for (Object value: values
             ) {
            JSONArray jsonArray = (JSONArray) value;
            for (Object obj: jsonArray
                 ) {
                objectHashSet.add((String)obj);
            }
        }

        File file1=new File("D:\\工作\\20181029疾病映射核对\\drug_info(new).txt");
        String content1 = FileUtils.readFileToString(file1, "utf-8");
        JSONObject jsonObject1 = JSON.parseObject(content1);
        Set<String> set = jsonObject1.keySet();
        Iterator<String> it = objectHashSet.iterator();
        for(int i=0; i<objectHashSet.size(); i++){
            String str = it.next();
            if(set.contains(str)){
                it.remove();
                i--;
            }
        }


        File file2=new File("D:\\工作\\20181029疾病映射核对\\drug_info.txt");
        String content2 = FileUtils.readFileToString(file2, "utf-8");
        JSONObject jsonObject2 = JSON.parseObject(content2);
        JSONObject jsonObject3 = new JSONObject();
        for (String str: objectHashSet
             ) {
            Object o = jsonObject2.get(str);
            jsonObject3.put(str,o);
        }

        String string = jsonObject3.toJSONString();
        File file3=new File("D:\\工作\\20181029疾病映射核对\\drug_info(add).txt");
        FileUtils.write(file3,string,"utf-8");

    }
}

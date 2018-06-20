package com.xingli.utils;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HTMLSpirit {

    private static Logger log = LoggerFactory.getLogger(HTMLSpirit.class);

    //提取src中的内容
    private static Pattern imgSrcPattern = Pattern.compile("src[\\s]{0,}=[\\s\"\']{0,}([^\"]+)[\\s\"\']");

    private static Pattern imgPatter = Pattern.compile("<\\s*img\\s*([^>]*)\\s*>");


    public static String[] delHTMLTag(String htmlStr) {
        //定义script的正则表达式
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>";

        //定义style的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>";

        //定义HTML标签的正则表达式
        String regEx_Strong = "<strong[^>]+>";

        //定义HTML标签的正则表达式
        String regEx_p = "<p[^>]+>";

        //定义HTML标签的正则表达式
        String regEx_html = "<[^>]+>";

        //String regEx_html = "<[^<img][^>]+>";

        //将html中的<img标签替换为{1}
        Map<Integer, String> imgCache = new HashMap<>();
        htmlStr = filterImg(htmlStr, imgCache);

        //过滤html标签
        htmlStr = htmlStr.replace("&nbsp;", " ");

        //过滤html标签
//        htmlStr = htmlStr.replace(" ", "").replace("&nbsp;", " ");

        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);

        //过滤script标签
        htmlStr = m_script.replaceAll("");

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);

        //过滤style标签
        htmlStr = m_style.replaceAll("");


        Pattern p_Strong = Pattern.compile(regEx_Strong, Pattern.CASE_INSENSITIVE);
        Matcher m_Strong = p_Strong.matcher(htmlStr);

        //过滤strong标签
        htmlStr = m_Strong.replaceAll("\r\n");

        Pattern p_p = Pattern.compile(regEx_p, Pattern.CASE_INSENSITIVE);
        Matcher m_p = p_p.matcher(htmlStr);

        //过滤p标签
        htmlStr = m_p.replaceAll("\r\n");

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);

        //过滤html标签
        htmlStr = m_html.replaceAll("");

        //还原html中的img标签
        htmlStr = returnImg(htmlStr, imgCache);

        //按行解析
        String[] htmlLine = htmlStr.split("\r\n");

        return htmlLine;
    }

    /**
     * 将img图片转换为base64字符串
     *
     * @param str
     * @param path
     * @return
     */
    private static String transformImgToBase64Str(String str, String path, File[] files) {
        Matcher imgSrcMatcher = imgSrcPattern.matcher(str);
        Matcher imgMatcher = imgPatter.matcher(str);
        String imgStr = "";
        if (imgMatcher.find()) {
            imgStr = imgMatcher.group();
        }

        if (imgSrcMatcher.find())
        {
            //利用捕获分组获取src中的值
            String imgSrcValue = imgSrcMatcher.group(1);

            if (imgStr.equals("")) {
                imgStr = imgSrcValue;
            }
            //下载网络图片，替换src的内容
            byte[] datas = null;

            String imgHttpURL = "";
            try {

                log.debug("文件中的图片地址:" +imgSrcValue);

                //很多图片地址采用的是本地下载的图片，url不合法，优先加载本地图片，之后再加载网络资源
                List<String> imgNames = Arrays.asList(files).stream().map(item -> item.getName()).filter(item -> imgSrcValue.endsWith(item)).collect(Collectors.toList());

                //【读取本地图片】
                if(imgNames != null && imgNames.size() > 0)
                {
                    //取其中一个
                    String localImgName = imgNames.get(0);

                    String localImgPath = path + File.separator + localImgName;

                    log.debug("读取本地图片的地址:" + localImgPath);

                    //读取本地的文件

                    datas = CommonFileUtils.readBytes(localImgPath);
                }
                //【读取网络图片】
                else
                {
                    //有的图片是以//开头的地址，需要替换为http才可以下载 //res.wx.qq.com/mmbizwap/zh_CN/htmledition/images/pic/appmsg/pic_reward_qrcode.2x3534dd.png
                    if (imgSrcValue.startsWith("//")) {
                        imgHttpURL = imgSrcValue.replace("//", "http://");
                    }
                    //如果不是以http开头的，需要补上http://
                    else if (!imgSrcValue.startsWith("http") && imgSrcValue.indexOf("/") > -1) {
                        //这里可能存在不合法的src值被拼装为 http:// xxxx;需要处理异常
                        imgHttpURL = "http://" + imgSrcValue;
                    } else {
                        File file = new File(path + imgSrcValue);
                        if (file.exists()) {
                            datas = getImageBinary(path + imgSrcValue);
                        }
                        imgHttpURL = imgSrcValue;
                    }
                }

                //下载图片二进制数据
                if (null == datas) {
                    datas = CommonFileUtils.readImg(imgHttpURL);
                }
            } catch (Exception e) {
                datas = null;
                log.info("--> 下载图片地址：" + imgHttpURL);
                log.error("异常：", e);
            }

            if (datas != null) {
                //data:image/png;base64,
                String base64Str = Base64.encodeBase64String(datas);

                //html标签中img中的src直接赋Base64字符串显示图片，需要在Base64开头增加固定标志：data:image/png;base64,
                base64Str = "<img src=\"data:image/png;base64," + base64Str + "\" />";

                //将二进制数据转换成Base64字符串

//                str = str.replace(imgSrcValue, base64Str);
                str = str.replace(imgStr, base64Str);
            }
        }

        return str;
    }

    /**
     * 读取文件二进制
     *
     * @param Imgpath
     * @return
     */
    public static byte[] getImageBinary(String Imgpath) {
        File f = new File(Imgpath);
        BASE64Encoder encoder = new BASE64Encoder();
        BufferedImage bi;
        try {
            bi = ImageIO.read(f);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "jpg", baos);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 替换img标签，并按照顺序缓存
     *
     * @param htmlStr
     * @return
     */
    private static String filterImg(String htmlStr, Map<Integer, String> imgCache) {
        Matcher imgMatcher = imgPatter.matcher(htmlStr);

        int index = 0;
        //匹配多个img标签
        while (imgMatcher.find()) {
            String imgStr = imgMatcher.group();

            //缓存index和img字符串
            imgCache.put(index, imgStr);

            index++;
        }

        //替换
        for (Map.Entry<Integer, String> entry : imgCache.entrySet()) {
            int i = entry.getKey();

            String imgStrValue = entry.getValue();

            htmlStr = htmlStr.replace(imgStrValue, "{" + i + "}");
        }

        return htmlStr;
    }

    /**
     * 还原html中的img标签
     *
     * @param htmlStr
     * @param imgCache
     * @return
     */
    private static String returnImg(String htmlStr, Map<Integer, String> imgCache) {
        for (Map.Entry<Integer, String> entry : imgCache.entrySet()) {
            int index = entry.getKey();

            String imgStrValue = entry.getValue();

            htmlStr = htmlStr.replace("{" + index + "}", imgStrValue + "\r\n");
        }

        return htmlStr;
    }

   /** *
    * @Description:  读html文件为字符串
    * @param filePath
    * @return: java.lang.String
    * @Date: Created in 15:21 2018/6/1/0001
    * @Modified By: xingli12
    */
    public static String readfile(String filePath) throws Exception {

        StringBuffer sb = new StringBuffer();
        BufferedReader ready;
        try {
            ready = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)), "UTF-8"));
            String temp = "";
            while (null != (temp = ready.readLine())) {
                sb.append(temp + "\r\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();

    }

    public static ArrayList<File> getFiles(String path, ArrayList<File> fileList) throws Exception {
//目标集合fileList
        File file = new File(path);
        if (file.isDirectory()) {
            System.out.println("打开目录：" + path);
            File[] files = file.listFiles();
            System.out.println("目录包含文件：" + files.length);
            for (File fileIndex : files) {
                //如果这个文件是目录，则进行递归搜索
                if (fileIndex.isDirectory()) {
                    getFiles(fileIndex.getPath(), fileList);
                } else {
                    //如果文件是普通文件，则将文件句柄放入集合中
                    if (fileIndex.getName().toLowerCase().contains(".html")) {
                        fileList.add(fileIndex);
                    }
                }
            }
        }
        return fileList;
    }

    /*
    * 获取所指向url的输出流(指所有内容)
    * @urlString url路径  如:http://www.baidu.com
    *返回的String 则为html代码
    * */
//    private static String getHtml(String urlString) {
//        //HttpClient客户端，就好比一个浏览器
//        CloseableHttpClient httpClient = null;
//        //HttpClient上下文
//        HttpClientContext httpClientContext = null;
//        //HttpClient响应
//        CloseableHttpResponse response = null;
//        String content = null;
//        //创建HttpClient客户端,不用多次创建该对象
//        httpClient = HttpClients.createDefault();
//        //创建默认的上下文
//        httpClientContext = HttpClientContext.create();
//        //创建一个GET请求
//        HttpGet getRequest = new HttpGet(urlString);
//        try {
//            //执行上面getRequest请求，并得到响应
//            response = httpClient.execute(getRequest, httpClientContext);
//            //得到response状态码
//            int status = response.getStatusLine().getStatusCode();
//            System.out.println("status----" + status);
//            /**
//             * 这是Apache的一个IO工具类，将InputStream转换为String.
//             * 将response的InputStream流转换为String
//             * 这里自己手动转换也是可以的
//             */
//            content = IOUtils.toString(response.getEntity().getContent());
//        } catch (ClientProtocolException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } finally {
//            getRequest.releaseConnection();
//        }
//        //输出网页内容
//        System.out.println(content);
//        return content;
//    }


    public static void packageStr(Map<String, Integer> map, String str) {
        String[] strs = str.split("\r\n");
        String regEx = "^[第]{0,}[一,二,三,四,五,六,七,八,九,十,\\d]{1,2}[章,节,、,.,\\s]{1,}";
        Pattern pattern = Pattern.compile(regEx);

        for (String words : strs) {
            words = words.trim();
            if (null != words && !words.trim().equals("")) {
                Matcher matcher = pattern.matcher(words);
                boolean rs = matcher.find();
                if (rs) {
                    if (map.get(words) == null) {
                        map.put(words, 1);
                    } else {
                        map.put(words, map.get(words) + 1);
                    }
                }
            }
        }
    }

    public static void sortMap(Map<String, Integer> map) throws IOException {
        //获取entrySet
        Set<Map.Entry<String, Integer>> mapEntries = map.entrySet();

        for (Map.Entry<String, Integer> entry : mapEntries) {
            System.out.println("key:" + entry.getKey() + "   value:" + entry.getValue());
        }

        //使用链表来对集合进行排序，使用LinkedList，利于插入元素
        List<Map.Entry<String, Integer>> result = new LinkedList<>(mapEntries);
        //自定义比较器来比较链表中的元素
        Collections.sort(result, new Comparator<Map.Entry<String, Integer>>() {
            //基于entry的值（Entry.getValue()），来排序链表
            @Override
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {

                return o2.getValue().compareTo(o1.getValue());
            }

        });

        //将排好序的存入到LinkedHashMap(可保持顺序)中，需要存储键和值信息对到新的映射中。
        Map<String, Integer> linkMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> newEntry : result) {
            linkMap.put(newEntry.getKey(), newEntry.getValue());
        }
        //根据entrySet()方法遍历linkMap


        File file = new File("file.csv");
        //构建输出流，同时指定编码
        OutputStreamWriter ow = new OutputStreamWriter(new FileOutputStream(file));

        //写内容
        for (Map.Entry<String, Integer> mapEntry : linkMap.entrySet()) {
            System.out.println("key:" + mapEntry.getKey() + "  value:" + mapEntry.getValue().toString());
            ow.write(mapEntry.getKey());
            ow.write(",");
            ow.write(mapEntry.getValue().toString());
            ow.write(",");
            //写完一行换行
            ow.write("\r\n");
        }
        ow.flush();
        ow.close();
    }


    public static void writeTxt(File file, String str) throws IOException {
        //路径存在大小写区别Linux
        File target = new File(file.getPath().replace(".html", ".txt"));
        //构建输出流，同时指定编码
        OutputStreamWriter ow = new OutputStreamWriter(new FileOutputStream(target), System.getProperty("file.encoding"));
        ow.write(str);
        ow.flush();
        ow.close();
    }

    public static String packageDiv(String[] htmlContents, String path) {
        String res = "";
        int i = 1;
        File htmlParentFile = new File(path);
        //文章文件夹下的所有子文件列表
        File[] files = htmlParentFile.listFiles();
        ConcurrentCalculator concurrentCalculator = new ConcurrentCalculator();
        String p =  concurrentCalculator.sum(htmlContents,path,files);
//        for (String str : htmlContents) {
//            //匹配一行中是否存在img标签，并提取src地址，下载图片二进制数据，并转换成Base64字符串
//            str = transformImgToBase64Str(str, path, files);
//            //划分段落
//            if (null != str && !str.trim().equals("")) {
//                res += "<div id='" + i + "'>" + str + "</div>\r\n";
//                i++;
//            }
//        }
//        ////返回文本字符串
//        return res.trim();
        return p.trim();
    }
    public static String packageImg(String string, String path) {
        File htmlParentFile = new File(path);

        //文章文件夹下的所有子文件列表
        File[] files = htmlParentFile.listFiles();

        return transformImgToBase64Str(string, path, files);
    }

    /**
     * 将html内容转换为txt内容
     *
     * @param file
     * @param htmlContent
     * @throws Exception
     */

    public static void convertHtmlToTxt(File file, String htmlContent, String path) throws Exception {
//        String tempHtmlContent ="";
        Map<String, Integer> map = new HashMap<>();
        String[] htmlContents = HTMLSpirit.delHTMLTag(htmlContent);
        htmlContent = HTMLSpirit.packageDiv(htmlContents, path);
//        tempHtmlContent = htmlContent;
        HTMLSpirit.writeTxt(file, htmlContent);
        HTMLSpirit.packageStr(map, htmlContent);
        HTMLSpirit.sortMap(map);

    }

    public static void main(String[] args) throws Exception {
        File file = new File("d:/disk1/resource/threeCategories/top100/2018.3/Output/共识100/羊水过少 共识/【精彩回顾-产科篇】" +
                "漆洪波教授妊娠34周前胎膜早破的处理/【精彩回顾-产科篇】漆洪波教授妊娠34周前胎膜早破的处理.html");
        String path = "D:\\disk1\\resource\\threeCategories\\top100\\2018.3\\Output\\共识100\\羊水过少 共识\\【精彩回顾-产科篇】漆洪波教授妊娠34周前胎膜早破的处理";
        String str = HTMLSpirit.readfile(file.getPath());
        long start = System.currentTimeMillis();
        convertHtmlToTxt(file, str, path);
        long end = System.currentTimeMillis();
        System.out.println("(2)转换txt耗时："+(end-start)+"毫秒");
    }
}
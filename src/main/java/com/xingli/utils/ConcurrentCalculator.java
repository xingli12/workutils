package com.xingli.utils;

import org.apache.tomcat.util.codec.binary.Base64;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 对于来源字符串数组的数据进行多线程的div包装
* junwang33
 */
public class ConcurrentCalculator {
    private ExecutorService exec;
    //这个地方，纯粹是“一厢情愿”，“并行执行”不受咱们控制，取决于操作系统的“态度”
    private int cpuCoreNumber;
    private String[] numbers;
    private File[] files;
    private String path;
    private List<FutureTask<String>> tasks = new ArrayList<FutureTask<String>>();

    public ConcurrentCalculator() {
//        cpuCoreNumber = Runtime.getRuntime().availableProcessors();
        cpuCoreNumber = 10;
        exec = Executors.newFixedThreadPool(cpuCoreNumber);
    }

    public String sum(final String[] numbers,String path,File[] files) {
        this.numbers = numbers;
        this.files = files;
        this.path = path;
        System.out.println("提交任务总数："+ numbers.length);
        // 根据CPU核心个数拆分任务，创建FutureTask并提交到Executor
        for (int i = 0; i < cpuCoreNumber; i++) {
            int increment = numbers.length / cpuCoreNumber + 1;
            int start = increment * i;
            int end = increment * i + increment;
            if (end > numbers.length){
                end = numbers.length;
            }
            SumCalculator subCalc = new SumCalculator(start, end);
            FutureTask<String> task = new FutureTask<String>(subCalc);
            tasks.add(task);
            if (!exec.isShutdown()) {
//                System.out.println("提交一个任务："+"起始位置："+start+"结束位置："+end);
                exec.submit(task);
            }
        }
        System.out.println("提交任务完毕");
        return getResult();
    }

    /**
     * 迭代每个只任务，获得部分和，相加返回
     */
    public String getResult() {
        String result = "";
        for (Future<String> task : tasks) {
            try {
                // 如果计算未完成则阻塞
                String subSum = task.get();
//                System.out.println("sum结果："+subSum);
                result += subSum;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        exec.shutdown();
        return result;
    }

    class SumCalculator implements Callable<String> {
        //提取src中的内容
        private  Pattern imgSrcPattern = Pattern.compile("src[\\s]{0,}=[\\s\"\']{0,}([^\"]+)[\\s\"\']");
        private  Pattern imgPatter = Pattern.compile("<\\s*img\\s*([^>]*)\\s*>");
        private int start;
        private int end;

        public SumCalculator(int start, int end) {
            this.start = start;
            this.end = end;
        }


        @Override
        public String call() throws Exception {
              String sum = "";
              for (int i = start; i < end; i++) {
                  String str = numbers[i];
                  //匹配一行中是否存在img标签，并提取src地址，下载图片二进制数据，并转换成Base64字符串
                  str = transformImgToBase64Str(str, path, files);
                  //划分段落
                  if (null != str && !str.trim().equals("")) {
                      sum += "<div id='" + i + "'>" + str + "</div>\r\n";
                  }
              }
              return sum;

        }


    public void close() {
        exec.shutdown();
    }

    /**
     * 将img图片转换为base64字符串
     *
     * @param str
     * @param path
     * @return
     */
    private String transformImgToBase64Str(String str, String path, File[] files) {
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

                //很多图片地址采用的是本地下载的图片，url不合法，优先加载本地图片，之后再加载网络资源
                List<String> imgNames = Arrays.asList(files).stream().map(item -> item.getName()).filter(item -> imgSrcValue.endsWith(item)).collect(Collectors.toList());

                //【读取本地图片】
                if(imgNames != null && imgNames.size() > 0)
                {
                    //取其中一个
                    String localImgName = imgNames.get(0);

                    String localImgPath = path + File.separator + localImgName;

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
    public byte[] getImageBinary(String Imgpath) {
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

    }

}

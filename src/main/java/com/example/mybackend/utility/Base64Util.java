package com.example.mybackend.utility;

import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Base64Util {

    /**
     * 图片URL转Base64编码
     * @param imgUrl 图片URL
     * @return Base64编码
     */
    public static String imageUrlToBase64(String imgUrl) {
        URL url = null;
        InputStream is = null;
        ByteArrayOutputStream outStream = null;
        HttpURLConnection httpUrl = null;

        try {
            url = new URL(imgUrl);
            httpUrl = (HttpURLConnection) url.openConnection();
            httpUrl.connect();
            httpUrl.getInputStream();

            is = httpUrl.getInputStream();
            outStream = new ByteArrayOutputStream();

            //创建一个Buffer字符串
            byte[] buffer = new byte[1024];
            //每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len = 0;
            //使用输入流从buffer里把数据读取出来
            while( (len = is.read(buffer)) != -1 ){
                //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                outStream.write(buffer, 0, len);
            }

            // 对字节数组Base64编码
            return Constants.BASE64_PREFIX + encode(outStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(is != null) {
                    is.close();
                }
                if(outStream != null) {
                    outStream.close();
                }
                if(httpUrl != null) {
                    httpUrl.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 图片转字符串
     * @param image 图片Buffer
     * @return Base64编码
     */
    public static String encode(byte[] image){
        return replaceEnter(Base64.encodeBase64String(image));
    }

    /**
     * 字符替换
     * @param str 字符串
     * @return 替换后的字符串
     */
    public static String replaceEnter(String str){
        String reg ="[\n-\r]";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }

    public static void main(String[] args) throws IOException {
        File file = new File("base64.txt");
        if (!file.exists())
            file.createNewFile();
        FileWriter fw = new FileWriter("base64.txt", false);
        PrintWriter pw = new PrintWriter(fw);
        pw.print(Base64Util.imageUrlToBase64("https://img-blog.csdnimg.cn/20210105221008901.png"));
        pw.flush();
    }

}


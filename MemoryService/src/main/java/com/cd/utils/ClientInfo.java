package com.cd.utils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yxl on 15-11-25.
 * 获取客户端信息
 */
public class ClientInfo {
    private String info = "";
    private String explorerVer = "未知";
    private String OSVer = "未知";
    private HttpServletRequest request;

    /*
     * 构造函数
     * 参数：String request.getHeader("user-agent")
     *
     * IE7:Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C)
     * IE8:Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C)
     * Maxthon:Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; Maxthon 2.0)
     * firefox:Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.11) Gecko/20101012 Firefox/3.6.11
     * Chrome:Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/534.7 (KHTML, like Gecko) Chrome/7.0.517.44 Safari/534.7
     * Opera:Opera/9.80 (Windows NT 6.1; U; zh-cn) Presto/2.6.30 Version/10.63
     *
     * 操作系统：
     * Win7 : Windows NT 6.1
     * WinXP : Windows NT 5.1
     */
    public ClientInfo(HttpServletRequest request) {
        if (request!=null){
            this.info = request.getHeader("user-agent");
            this.request = request;
        }
    }

    /*
     * 获取核心浏览器名称
     */
    public String getExplorerName() {
        String str = "未知";
        Pattern pattern = Pattern.compile("");
        Matcher matcher;
        if (info.contains("MSIE")) {
            str = "MSIE"; //微软IE
            pattern = Pattern.compile(str + "\\s([0-9.]+)");
        } else if (info.contains("Firefox")) {
            str = "Firefox"; //火狐
            pattern = Pattern.compile(str + "\\/([0-9.]+)");
        } else if (info.contains("Chrome")) {
            str = "Chrome"; //Google
            pattern = Pattern.compile(str + "\\/([0-9.]+)");
        } else if (info.contains("Opera")) {
            str = "Opera"; //Opera
            pattern = Pattern.compile("Version\\/([0-9.]+)");
        }
        matcher = pattern.matcher(info);
        if (matcher.find()) explorerVer = matcher.group(1);
        return "浏览器为：" + str + "版本号是：" + explorerVer;
    }

    /*
     * 获取浏览器插件名称（例如：遨游、世界之窗等）
     */
    public String getExplorerPlug() {
        String str = "无";
        if (info.contains("Maxthon"))
            str = "Maxthon"; //遨游
        return str;
    }

    /*
     * 获取操作系统名称
     */
    public String getOSName() {
        String str = "未知";
        Pattern pattern = Pattern.compile("");
        Matcher matcher;
        if (info.contains("Windows")) {
            str = "Windows"; //Windows NT 6.1
            pattern = Pattern.compile(str + "\\s([a-zA-Z0-9]+\\s[0-9.]+)");
        }
        matcher = pattern.matcher(info);
        if (matcher.find()) OSVer = matcher.group(1);
        return "操作系统是：" + str + "版本为：" + OSVer;
    }
    /**
     * 获取地址
     * @param params IP地址
     * @param encoding 字符编码
     * @return 返回地址
     * @throws Exception
     */
    public String getAddress(String params, String encoding) throws Exception {
        String path = "http://ip.taobao.com/service/getIpInfo.php";
        String returnStr = this.getRs(path, params, encoding);
        JSONObject jsonObject = JSON.parseObject(returnStr);
        String code = jsonObject.get("code").toString();
        String data = jsonObject.get("data").toString();
        JSONObject jsonObject1 = JSON.parseObject(data);
        if (returnStr != null) {
            if ("0".equals(code)) {
                //buffer.append(decodeUnicode(jsonObject1.get("country").toString()));//国家
                //buffer.append(decodeUnicode(jsonObject1.get("area").toString()));//地区
                return decodeUnicode(jsonObject1.get("region").toString()) +
                        decodeUnicode(jsonObject1.get("city").toString()) +
                        decodeUnicode(jsonObject1.get("county").toString()) +
                        decodeUnicode(jsonObject1.get("isp").toString());
            } else {
                return "获取地址失败?";
            }
        }
        return null;
    }
    /**
     * 获取地址
     * @return 返回地址
     * @throws Exception
     */
    public String getAddress() throws Exception {
        String path = "http://ip.taobao.com/service/getIpInfo.php";
        //String returnStr = this.getRs(path, params, encoding);
        String params = "ip="+getIp();
        String encoding = "utf-8";
        String returnStr = this.getRs(path, params, encoding);
        JSONObject jsonObject = JSON.parseObject(returnStr);
        String code = jsonObject.get("code").toString();
        String data = jsonObject.get("data").toString();
        JSONObject jsonObject1 = JSON.parseObject(data);
        if (returnStr != null) {
            if ("0".equals(code)) {
                //buffer.append(decodeUnicode(jsonObject1.get("country").toString()));//国家
                //buffer.append(decodeUnicode(jsonObject1.get("area").toString()));//地区
                return decodeUnicode(jsonObject1.get("region").toString()) +
                        decodeUnicode(jsonObject1.get("city").toString()) +
                        decodeUnicode(jsonObject1.get("county").toString()) +
                        decodeUnicode(jsonObject1.get("isp").toString());
            } else {
                return "获取地址失败?";
            }
        }
        return null;
    }
        /**
         * 从url获取结果
         *
         * @param path IP库地址
         * @param params ip地址
         * @param encoding 字符编码
         * @return 返回结果
         */
    public String getRs(String path, String params, String encoding) {
        URL url;
        HttpURLConnection connection = null;
        try {
            url = new URL(path);
            connection = (HttpURLConnection) url.openConnection();// 新建连接实例
            connection.setConnectTimeout(2000);// 设置连接超时时间，单位毫秒?
            connection.setReadTimeout(2000);// 设置读取数据超时时间，单位毫秒?
            connection.setDoInput(true);// 是否打开输出流? true|false
            connection.setDoOutput(true);// 是否打开输入流true|false
            connection.setRequestMethod("POST");// 提交方法POST|GET
            connection.setUseCaches(false);// 是否缓存true|false
            connection.connect();// 打开连接端口
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(params);
            out.flush();
            out.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));
            StringBuilder buffer = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert connection != null;
            connection.disconnect();// 关闭连接
        }
        return null;
    }

    /**
     * 字符转码
     *
     * @param theString 字符
     * @return 返回地址
     */
    public static String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuilder buffer = new StringBuilder(len);
        for (int i = 0; i < len; ) {
            aChar = theString.charAt(i++);
            if (aChar == '\\') {
                aChar = theString.charAt(i++);
                if (aChar == 'u') {
                    int val = 0;
                    for (int j = 0; j < 4; j++) {
                        aChar = theString.charAt(i++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                val = (val << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                val = (val << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                val = (val << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed      encoding.");
                        }
                    }
                    buffer.append((char) val);
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    }
                    if (aChar == 'r') {
                        aChar = '\r';
                    }
                    if (aChar == 'n') {
                        aChar = '\n';
                    }
                    if (aChar == 'f') {
                        aChar = '\f';
                    }
                    buffer.append(aChar);
                }
            } else {
                buffer.append(aChar);
            }
        }
        return buffer.toString();
    }

    public  String getIp() {
        String ip = request.getHeader("X-Forwarded-For");
        if(ip!=null && !"unKnown".equalsIgnoreCase(ip)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if(index != -1){
                return ip.substring(0,index);
            }else{
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if(ip!=null && !"unKnown".equalsIgnoreCase(ip)){
            return ip;
        }
        return request.getRemoteAddr();
    }
}

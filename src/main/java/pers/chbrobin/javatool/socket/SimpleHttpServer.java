package pers.chbrobin.javatool.socket;

import pers.chbrobin.javatool.util.HttpRequestUtil;
import pers.chbrobin.javatool.util.HttpServerUtil;

import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.Map;
import java.util.StringTokenizer;

/**
 *
 */
public class SimpleHttpServer implements Runnable {

    private ServerSocket serverSocket;
    private String serverIndexHtml;

    public SimpleHttpServer(Integer port, String serverIndexHtml) {
        try {
            serverSocket = new ServerSocket(port);
            this.serverIndexHtml = serverIndexHtml;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Http Server Running Port:" + port);
    }

    public void run() {
        while (true) {
            try {
                Socket client = serverSocket.accept();//客户机(这里是 IE 等浏览器)已经连接到当前服务器
                if (client != null) {
                    System.out.println("Connect Client:" + client);
                    try {
                        // 第一阶段: 打开输入流
                        InputStream is = client.getInputStream();

                        // 读取第一行, 请求地址
                        String line = HttpServerUtil.readLine(is, 0);
                        //打印请求行
                        System.out.print(line);
                        // < Method > < URL > < HTTP Version > <\r\n>  取的是URL部分
                        String resource = line.substring(line.indexOf('/'), line
                                .lastIndexOf('/') - 5);
                        //获得请求的资源的地址
                        resource = URLDecoder.decode(resource, HttpServerUtil.DEFAULT_ENCODING);//反编码 URL 地址
                        String method = new StringTokenizer(line).nextElement()
                                .toString();// 获取请求方法, GET 或者 POST
                        int contentLength = 0;//如果为POST方法，则会有消息体长度

                        // 读取所有浏览器发送过来的请求参数头部信息
                        do {
                            line = HttpServerUtil.readLine(is, 0);
                            //如果有Content-Length消息头时取出
                            if (line.startsWith("Content-Length")) {
                                contentLength = Integer.parseInt(line.split(":")[1]
                                        .trim());
                            }
                            //打印请求部信息
                            System.out.print(line);
                            //如果遇到了一个单独的回车换行，则表示请求头结束
                        } while (!line.equals("\r\n"));

                        //如果是POST请求，则有请求体
                        if ("POST".equalsIgnoreCase(method)) {
                            //注，这里只是简单的处理表单提交的参数，而对于上传文件这里是不能这样处理的，
                            //因为上传的文件时消息体不只是一行，会有多行消息体
                            String query = HttpServerUtil.readLine(is, contentLength);
                            Map<String, String> map = HttpRequestUtil.parseParam(query);
                            String host = map.get("host");
                            String action = map.get("action");
                            String param = map.get("param");
                            host = URLDecoder.decode(host);
                            action = URLDecoder.decode(action);
                            if(param != null) {
                                param = URLDecoder.decode(param);
                            }
                            String result = HttpRequestUtil.getRequestByPost(host + "/" + action, param, HttpServerUtil.DEFAULT_ENCODING);

                            // 用 writer 对客户端 socket 输出一段 HTML 代码
                            PrintWriter out = new PrintWriter(client.getOutputStream(),true);
                            out.println("HTTP/1.0 200 OK");//返回应答消息,并结束应答
                            out.println("Content-Type:text/html;charset=" + HttpServerUtil.DEFAULT_ENCODING);
                            out.println();// 根据 HTTP 协议, 空行将结束头信息

                            out.println(result);
                            out.close();

                            HttpServerUtil.closeSocket(client);
                        }

                        // load jquery file
                        if (resource.startsWith("/jquery.js")) {
                            HttpServerUtil.fileShow("jquery.js", client);
                            HttpServerUtil.closeSocket(client);
                            continue;
                        }

                        // load default html
                        PrintWriter out = new PrintWriter(client.getOutputStream(),true);
                        out.println("HTTP/1.0 200 OK");//返回应答消息,并结束应答
                        out.println("Content-Type:text/html;charset=" + HttpServerUtil.DEFAULT_ENCODING);
                        out.println();// 根据 HTTP 协议, 空行将结束头信息

                        out.println(serverIndexHtml);

                        out.close();
                        HttpServerUtil.closeSocket(client);
                    } catch (Exception e) {
                        System.out.println("Http Server Error:" + e.getLocalizedMessage());
                    }
                }
            } catch (Exception e) {
                System.out.println("Http Server Error:" + e.getLocalizedMessage());
            }
        }
    }
}
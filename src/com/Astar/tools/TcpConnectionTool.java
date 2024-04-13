package com.Astar.tools;

import com.Astar.resource.Constant;
import com.Astar.resource.ResourceFactory;

import java.io.*;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TcpConnectionTool {
    public static boolean initServer(int port) {
        try {
            // 初始化服务器
            ResourceFactory.serverSocket = new ServerSocket(port);
            // 设置超时时间
            ResourceFactory.serverSocket.setSoTimeout(Constant.Server.TIME_OUT);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getIPAddress() {
        try {
            Process ipStream = Runtime.getRuntime().exec("ipconfig");
            BufferedReader br = new BufferedReader(new InputStreamReader(ipStream.getInputStream(), "GBK"));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String ip = sb.toString();
            Pattern compile = Pattern.compile(
                    "无线局域网适配器 WLAN.*IPv4 地址.*?(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})",
                    Pattern.DOTALL);
            Matcher matcher = compile.matcher(ip);
            String string = null;
            while (matcher.find()) {
                string = matcher.group(1);

            }
            return string;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

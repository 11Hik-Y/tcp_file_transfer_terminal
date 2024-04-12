package com.Astar.tools;

import com.Astar.resource.Constant;
import com.Astar.resource.ResourceFactory;

import java.net.ServerSocket;

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
}

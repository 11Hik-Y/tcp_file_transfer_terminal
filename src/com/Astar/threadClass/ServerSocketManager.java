package com.Astar.threadClass;

import com.Astar.resource.ResourceFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketManager implements Runnable {
    private final int sliceNum;

    public ServerSocketManager(int sliceNum) {
        this.sliceNum = sliceNum;
    }

    @Override
    public void run() {
        // 开始接受客户端请求
        ServerSocket serverSocket = ResourceFactory.serverSocket;
        try {
            // 等待接收到足够的线程数量就停止
            while (ResourceFactory.asServerSockets.size() < sliceNum) {
                Socket socket = serverSocket.accept();
                // 将接收到的请求加入到集合中，方便管理
                ResourceFactory.asServerSockets.add(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

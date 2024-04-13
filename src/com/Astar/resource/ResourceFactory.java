package com.Astar.resource;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ResourceFactory {
    // 服务器套接字
    public static ServerSocket serverSocket;

    // 作为服务端接受的客户端连接集合
    public static final ArrayList<Socket> asServerSockets = new ArrayList<>();

    // 作为客户端发出的所有的与服务端的连接集合
    public static final ArrayList<Socket> asClientSockets = new ArrayList<>();
}

package com.Astar;

import com.Astar.infoClass.Log;
import com.Astar.resource.Constant;
import com.Astar.resource.ResourceFactory;
import com.Astar.tools.TcpConnectionTool;
import com.Astar.type.TransferType;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class App {
    public static final Scanner sc = new Scanner(System.in);

    public static File file;

    public static TransferType transferType;

    public static HashMap<String, String> paramMap;

    public static void main(String[] args) {
        // 获取用户需要传输的文件 以及需要作为服务端还是客户端的参数
        paramMap = processArgs(args);

        // 判断传输的类型
        initTransferType();

        // 初始化需要传输或接受的文件


        switch (transferType) {
            case CLIENT:
                // 选择接收文件的位置
                initReceivePath();
                break;
            case SERVER:
                // 选择发送的文件
                initTransferFile();
                break;
            default:
                Log.error("发生异常，程序退出...\n");
                break;
        }

        // 根据传输类型选择启动服务端或者客户端
        switch (transferType) {
            case CLIENT:
                // 启动客户端
                asClient();
                break;
            case SERVER:
                // 启动服务端
                asServer();
                break;
            default:
                Log.error("发生异常，程序退出...\n");
                break;
        }
    }

    private static void asServer() {
        // 启动服务器
        startServer();

        // 开始接受客户端请求
        ServerSocket serverSocket = ResourceFactory.serverSocket;
        try {
            Socket socket = serverSocket.accept();
            // 将接收到的请求加入到集合中，方便管理
            ResourceFactory.asServerSockets.add(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startServer() {
        // 开始启动服务端
        boolean flag = false;
        if (paramMap.get(Constant.Param.PORT) == null) {
            flag = TcpConnectionTool.initServer(Constant.Server.PORT);
        } else {
            flag = TcpConnectionTool.initServer(Integer.parseInt(paramMap.get(Constant.Param.PORT)));
        }

        // 检查是否启动成功
        if (flag) {
            Log.info("服务器IP地址：%s\n", TcpConnectionTool.getIPAddress());
            Log.info("服务器启动成功，等待客户端连接...\n");
        } else {
            Log.error("服务器启动失败，请检查端口是否被占用\n");
        }
    }

    private static void asClient() {

    }

    private static void initTransferType() {
        if (paramMap != null && paramMap.containsKey(Constant.Param.TYPE_TRANSFER)) {
            switch (paramMap.get(Constant.Param.TYPE_TRANSFER).toLowerCase()) {
                case Constant.Param.SERVER:
                    transferType = TransferType.SERVER;
                    break;
                case Constant.Param.CLIENT:
                    transferType = TransferType.CLIENT;
                    break;
                default:
                    Log.error("传输类型错误\n");
                    getTransferType();
                    break;
            }
        } else {
            getTransferType();
        }
    }

    private static void getTransferType() {
        while (true) {
            Log.info("请输入传输类型：\n");
            String s = sc.nextLine();
            if (Constant.Param.CLIENT.equalsIgnoreCase(s)) {
                transferType = TransferType.CLIENT;
                break;
            } else if (Constant.Param.SERVER.equalsIgnoreCase(s)) {
                transferType = TransferType.SERVER;
                break;
            } else {
                Log.error("传输类型错误\n");
            }
        }
    }

    private static void initReceivePath() {
        if (paramMap != null && paramMap.containsKey(Constant.Param.PATH)) {
            // 初始化需要传输的文件
            file = new File(paramMap.get("path"));
            if (!file.exists() || file.isFile()) {
                file = null;
                getDirPath();
            }
        } else {
            getDirPath();
        }
    }

    private static void getDirPath() {
        Log.info("请输入文件夹路径：\n");
        while (file == null) {
            String filePath = sc.nextLine();
            try {
                file = new File(filePath);
                if (!file.exists() || file.isDirectory()) {
                    Log.error("{} 不是一个文件夹\n", filePath);
                    Log.info("请重新输入：\n");
                    file = null;
                }
            } catch (Exception e) {
                Log.error("文件夹路径错误\n");
                Log.info("请重新输入：\n");
                file = null;
            }
        }
    }

    private static void initTransferFile() {
        if (paramMap != null && paramMap.containsKey(Constant.Param.PATH)) {
            // 初始化需要传输的文件
            file = new File(paramMap.get("path"));
            if (!file.exists() || file.isDirectory()) {
                file = null;
                getFilePath();
            }
        } else {
            getFilePath();
        }
    }

    private static void getFilePath() {
        Log.info("请输入文件路径：\n");
        while (file == null) {
            String filePath = sc.nextLine();
            try {
                file = new File(filePath);
                if (!file.exists() || file.isDirectory()) {
                    Log.error("{} 不是一个文件\n", filePath);
                    Log.info("请重新输入：\n");
                    file = null;
                }
            } catch (Exception e) {
                Log.error("文件路径错误\n");
                Log.info("请重新输入：\n");
                file = null;
            }
        }
    }

    public static HashMap<String, String> processArgs(String[] args) {
        // 判断是否是空参数
        if (args == null || args.length == 0) {
            return null;
        }

        // 遍历寻找参数
        HashMap<String, String> paramMap = new HashMap<>();
        for (String arg : args) {
            // 判断是否是以--开头
            if (arg.startsWith(Constant.Param.PARAM_HEAD)) {
                // 截取等式
                String eq = arg.substring(2);
                // 分割等式
                String[] param = eq.split("=");
                // 截取数量为2，再继续操作
                if (param.length != 2) {
                    continue;
                }
                // 判断是否是文件路径
                if (Constant.Param.PATH.equals(param[0])) {
                    paramMap.put(Constant.Param.PATH, param[1]);
                }
                // 判断是否是传输类型
                if (Constant.Param.TYPE_TRANSFER.equalsIgnoreCase(param[0])) {
                    paramMap.put(Constant.Param.TYPE_TRANSFER, param[1]);
                }
                // 判断是否是端口号
                if (Constant.Param.PORT.equalsIgnoreCase(param[0])) {
                    paramMap.put(Constant.Param.PORT, param[1]);
                }
            }
        }
        return paramMap;
    }
}

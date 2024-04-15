package com.Astar;

import com.Astar.infoClass.FileSliceInfo;
import com.Astar.infoClass.Log;
import com.Astar.resource.Constant;
import com.Astar.resource.ResourceFactory;
import com.Astar.threadClass.ClientFileReceiveThread;
import com.Astar.threadClass.TransferInfoThread;
import com.Astar.threadClass.ServerFileTransferThread;
import com.Astar.tools.FileSliceTool;
import com.Astar.tools.TcpConnectionTool;
import com.Astar.type.TransferType;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class App {
    private static final Scanner sc = new Scanner(System.in);

    private static File file;

    private static TransferType transferType;

    private static HashMap<String, String> paramMap;

    private static final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

    public static void main(String[] args) {
        // 获取用户需要传输的文件 以及需要作为服务端还是客户端的参数
        paramMap = processArgs(args);

        // 判断传输的类型
        initTransferType();

        // 根据传输类型初始化文件位置或文件夹位置
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

        // 获取文件的切片数量
        int sliceNum = paramMap != null && paramMap.containsKey(Constant.Param.SLICE_NUM) ?
                Integer.parseInt(paramMap.get(Constant.Param.SLICE_NUM)) :
                Constant.File.DEFAULT_SLICE_NUM;

        // 根据传输类型选择启动服务端或者客户端
        switch (transferType) {
            case CLIENT:
                // 启动客户端
                asClient();
                break;
            case SERVER:
                // 启动服务端
                asServer(sliceNum);
                break;
            default:
                Log.error("发生异常，程序退出...\n");
                break;
        }

        // 根据发送类型创建相应的线程（使用线程池）
        TransferInfoThread transferInfoThread = null;
        ExecutorService pool = Executors.newFixedThreadPool(sliceNum);
        switch (transferType) {
            case CLIENT:
                // 创建客户端文件传输信息线程
                transferInfoThread = new TransferInfoThread(-1);
                // 启动客户端文件接收线程
                for (int i = 0; i < sliceNum; i++) {
                    pool.execute(
                            new ClientFileReceiveThread(
                                    file.getAbsolutePath(),
                                    ResourceFactory.asClientSockets.get(i),
                                    transferInfoThread
                            )
                    );
                }
                break;
            case SERVER:
                // 创建服务端文件传输信息线程
                transferInfoThread = new TransferInfoThread(file.length());
                // 切片好文件提供给服务端
                ArrayList<FileSliceInfo> fileSliceInfos = FileSliceTool.fileSlice(
                        paramMap.get(Constant.Param.PATH),
                        sliceNum);
                // 启动服务端文件发送线程
                for (int i = 0; i < sliceNum; i++) {
                    pool.execute(
                            new ServerFileTransferThread(
                                    fileSliceInfos.get(i),
                                    ResourceFactory.asServerSockets.get(i),
                                    transferInfoThread
                            )
                    );
                }
                break;
            default:
                Log.error("发生异常，程序退出...\n");
                break;
        }

        // 每隔一秒统计一次文件的下载情况
        executor.scheduleAtFixedRate(transferInfoThread, 1, 1, TimeUnit.SECONDS);

        // 判断是否传输结束
        while (!transferInfoThread.isDone()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 程序结束
        end(pool);
    }

    public static void end(ExecutorService pool) {
        // 关闭线程池
        executor.shutdownNow();
        try {
            // 关闭线程池
            pool.shutdown();
            if (pool.awaitTermination(2, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println();
        Log.info("文件传输完毕，程序退出...\n");
    }

    private static void asServer(int sliceNum) {
        // 启动服务器
        startServer();

        // 接受服务端的请求
        receiveSocket(sliceNum);
    }

    private static void receiveSocket(int sliceNum) {
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
            Log.error("超时仍未有客户端连接，自动退出程序...");
            System.exit(0);
        }
    }

    private static void startServer() {
        // 开始启动服务端
        boolean flag = TcpConnectionTool.initServer(
                paramMap != null && paramMap.containsKey(Constant.Param.PORT) ?
                        // 使用传入的端口启动服务端
                        Integer.parseInt(paramMap.get(Constant.Param.PORT)) :
                        // 使用默认端口启动服务端
                        Constant.Server.DEFAULT_PORT
        );

        // 检查是否启动成功
        if (flag) {
            Log.info("服务器IP地址：{}\n", TcpConnectionTool.getIPAddress());
            Log.info("服务器启动成功，等待客户端连接...\n");
        } else {
            Log.error("服务器启动失败，请检查端口是否被占用\n");
        }
    }

    private static void asClient() {
        // 未传入主机ip
        if (paramMap != null && !paramMap.containsKey(Constant.Param.IP)) {
            getIP();
        }

        // 已经传入主机ip，同时也传入了切片数量
        if (paramMap != null && paramMap.containsKey(Constant.Param.IP) && paramMap.containsKey(Constant.Param.SLICE_NUM)) {
            // 按照传入的切片数量创建socket
            for (int i = 0; i < Integer.parseInt(paramMap.get(Constant.Param.SLICE_NUM)); i++) {
                createClientSocket();
            }
        }

        // 已经传入主机ip，但是未传入切片数量
        if (paramMap != null && paramMap.containsKey(Constant.Param.IP) && !paramMap.containsKey(Constant.Param.SLICE_NUM)) {
            // 按照传入的切片数量创建socket
            for (int i = 0; i < Constant.File.DEFAULT_SLICE_NUM; i++) {
                createClientSocket();
            }
        }

        Log.info("客户端启动成功，准备接收文件...\n");
    }

    private static void createClientSocket() {
        try {
            Socket socket = new Socket(
                    // 主机ip
                    paramMap.get(Constant.Param.IP),
                    // 检测如果传入了端口，则使用传入的端口，否则使用默认端口
                    paramMap.containsKey(Constant.Param.PORT) ?
                            Integer.parseInt(paramMap.get(Constant.Param.PORT)) :
                            Constant.Server.DEFAULT_PORT
            );
            ResourceFactory.asClientSockets.add(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getIP() {
        while (true) {
            Log.info("请输入主机ip：\n");
            String ip = sc.nextLine();
            if (ip != null && ip.matches("(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})|(localhost)")) {
                // 输入正确，进入下一步
                paramMap.put(Constant.Param.IP, ip);
                break;
            }

            // 输入错误，重新输入
            Log.error("主机ip错误\n");
        }
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
                Log.error("{} 不是一个文件夹\n", paramMap.get("path"));
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
                if (!file.exists() || file.isFile()) {
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
        paramMap.put(Constant.Param.PATH, file.getAbsolutePath());
    }

    private static void initTransferFile() {
        if (paramMap != null && paramMap.containsKey(Constant.Param.PATH)) {
            // 初始化需要传输的文件
            file = new File(paramMap.get(Constant.Param.PATH));
            if (!file.exists() || file.isDirectory()) {
                Log.error("{} 不是一个文件\n", paramMap.get(Constant.Param.PATH));
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
        paramMap.put(Constant.Param.PATH, file.getAbsolutePath());
    }

    public static HashMap<String, String> processArgs(String[] args) {
        // 判断是否是空参数
        if (args == null || args.length == 0) {
            // 初始化一个空的map
            return new HashMap<>();
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
                switch (param[0].toLowerCase()) {
                    case Constant.Param.PATH:
                        // 文件路径参数
                        String s = param[1].endsWith("\"") && param[1].startsWith("\"") ?
                                param[1].substring(1, param[1].length() - 1) :
                                param[1];
                        paramMap.put(Constant.Param.PATH, s);
                        break;
                    case Constant.Param.TYPE_TRANSFER:
                        // 传输类型参数
                        paramMap.put(Constant.Param.TYPE_TRANSFER, param[1]);
                        break;
                    case Constant.Param.PORT:
                        // 端口号参数
                        paramMap.put(Constant.Param.PORT, param[1]);
                        break;
                    case Constant.Param.IP:
                        // ip地址参数
                        paramMap.put(Constant.Param.IP, param[1]);
                        break;
                    case Constant.Param.SLICE_NUM:
                        // 切片数量参数
                        paramMap.put(Constant.Param.SLICE_NUM, param[1]);
                        break;
                    default:
                        break;
                }
            }
        }
        return paramMap;
    }
}

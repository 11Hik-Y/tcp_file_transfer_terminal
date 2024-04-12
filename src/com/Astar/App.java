package com.Astar;

import com.Astar.infoClass.Log;
import com.Astar.resource.Constant;
import com.Astar.tools.TcpConnectionTool;
import com.Astar.type.TransferType;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

public class App {
    public static final Scanner sc = new Scanner(System.in);

    public static File file;

    public static TransferType transferType;

    public static void main(String[] args) {
        // 获取用户需要传输的文件 以及需要作为服务端还是客户端的参数
        HashMap<String, String> paramMap = processArgs(args);

        // 初始化需要传输的文件
        initTransferFile(paramMap);

        // 判断传输的类型
        initTransferType(paramMap);

        // 获取好文件之后开始建立服务器与客户端的连接
        if (TcpConnectionTool.initServer(Constant.Server.PORT)) {
            Log.info("服务器启动成功，等待客户端连接...\n");
        }

    }

    private static void initTransferType(HashMap<String, String> paramMap) {
        if (paramMap != null && paramMap.containsKey(Constant.Param.TYPE_TRANSFER)) {
            switch (paramMap.get(Constant.Param.TYPE_TRANSFER)) {
                case Constant.Param.SERVER:
                    transferType = TransferType.SERVER;
                    break;
                case Constant.Param.CLIENT:
                    transferType = TransferType.CLIENT;
                    break;
                default:
                    while (true) {
                        Log.error("传输类型错误\n");
                        Log.info("请重新输入：\n");
                        String s = sc.nextLine();
                        if (Constant.Param.CLIENT.equals(s)) {
                            transferType = TransferType.CLIENT;
                            break;
                        } else if (Constant.Param.SERVER.equals(s)) {
                            transferType = TransferType.SERVER;
                            break;
                        }
                    }
            }
        }
    }

    private static void initTransferFile(HashMap<String, String> paramMap) {
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
                if (Constant.Param.TYPE_TRANSFER.equals(param[0])) {
                    paramMap.put(Constant.Param.TYPE_TRANSFER, param[1]);
                }
            }
        }
        return paramMap;
    }
}

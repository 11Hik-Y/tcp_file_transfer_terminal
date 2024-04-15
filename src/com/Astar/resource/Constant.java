package com.Astar.resource;

public final class Constant {


    public static final class Param {
        // 参数头
        public static final String PARAM_HEAD = "--";

        // 路径参数
        public static final String PATH = "path";

        // 端口号
        public static final String PORT = "port";

        // 主机ip
        public static final String IP = "ip";

        // 传输类型
        public static final String TYPE_TRANSFER = "type";

        // 切片数量参数
        public static final String SLICE_NUM = "slicenum";
        // 接收服务端超时时间，单位 s
        public static final String TIME_OUT = "timeout";

        public static final String SERVER = "server";
        public static final String CLIENT = "client";

        // 定义完成字节
        public static final byte COMPLETE = 0x0A;

        // 默认缓存大小
        public static final int DEFAULT_BUFFER_SIZE = 128 * 1024;

        // 兆字节
        public static final double MB = 1024d * 1024d;

        // 千字节
        public static final double KB = 1024d;
    }

    public static final class File {
        // 默认切片数量
        public static final int DEFAULT_SLICE_NUM = 4;
    }

    public static final class Server {
        // 默认端口
        public static final int DEFAULT_PORT = 6666;

        // 默认超时时间
        public static int TIME_OUT = 30 * 1000;
    }

    public static final class Client {
        // 默认端口
        public static final int DEFAULT_PORT = 6666;
    }
}

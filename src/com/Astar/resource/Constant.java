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

        public static final String SERVER = "server";
        public static final String CLIENT = "client";

        // 定义完成字节
        public static final byte COMPLETE = 0x0A;

        // 默认缓存大小
        public static final int DEFAULT_BUFFER_SIZE = 128 * 1024;
    }

    public static final class File {
        // 默认切片数量
        public static final int DEFAULT_SLICE_NUM = 4;
    }

    public static final class Server {
        // 默认端口
        public static final int DEFAULT_PORT = 6666;

        // 默认超时时间
        public static final int TIME_OUT = 30 * 1000;
    }

    public static final class ANSI {
        // ANSI所有的颜色字符
        public static final String ANSI_RESET = "\u001B[0m";
        public static final String ANSI_BLACK = "\u001B[30m";
        public static final String ANSI_RED = "\u001B[31m";
        public static final String ANSI_GREEN = "\u001B[32m";
        public static final String ANSI_YELLOW = "\u001B[33m";
        public static final String ANSI_BLUE = "\u001B[34m";
        public static final String ANSI_PURPLE = "\u001B[35m";
        public static final String ANSI_CYAN = "\u001B[36m";
        public static final String ANSI_WHITE = "\u001B[37m";
        public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
        public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
        public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
        public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
        public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
        public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
        public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
        public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    }
}

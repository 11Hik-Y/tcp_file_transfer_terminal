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
}

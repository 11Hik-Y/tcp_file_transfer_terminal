package com.Astar.infoClass;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

    public static void info(String msg, Object... args) {
        print("INFO", msg, args);
    }

    public static void info(String msg, String color, Object... args) {
        System.out.print(color);
        print("INFO", msg, args);
    }

    public static void error(String msg, Object... args) {
        print("ERROR", msg, args);
    }

    public static void error(String msg, String color, Object... args) {
        System.out.print(color);
        print("ERROR", msg, args);
    }

    private static void print(String type, String msg, Object... args) {
        String threadName = Thread.currentThread().getName();
        // 使用ANSI打印带有颜色的文字
        System.out.print("[" + type + "] " +
                threadName + " " +
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " " +
                String.format(msg.replace("{}", "%s"), args)
        );
    }
}

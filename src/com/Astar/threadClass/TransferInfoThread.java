package com.Astar.threadClass;

import com.Astar.resource.Constant;

import java.util.concurrent.atomic.LongAdder;

public class TransferInfoThread implements Runnable {
    private long totalSize;

    private final LongAdder transferSize;

    private long preSize;

    public TransferInfoThread(long totalSize) {
        this.totalSize = totalSize;
        this.transferSize = new LongAdder();
        this.preSize = 0;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public void addTransferSize(long size) {
        transferSize.add(size);
    }

    public boolean isDone() {
        return transferSize.longValue() == totalSize;
    }

    @Override
    public void run() {
        // 计算文件的总大小 单位：MB
        String fileSize = String.format("%.2f", totalSize / Constant.Param.MB);

        // 计算每秒的下载速度 kb
        int speed = (int) ((transferSize.doubleValue() - preSize) / Constant.Param.KB);
        preSize = transferSize.longValue();

        // 剩余文件的大小
        double remainSize = totalSize - transferSize.doubleValue();

        // 计算剩余时间
        String remainTime = String.format("%.1f", remainSize / Constant.Param.KB / speed);

        if ("Infinity".equalsIgnoreCase(remainTime)) {
            remainTime = "-";
        }

        // 已下载的大小
        String currentFileSize = String.format("%.2f", transferSize.doubleValue() / Constant.Param.MB);

        String downInfo = String.format("文件大小：%s MB，已传输：%s MB，速度：%s KB/s，剩余：%.2f MB，剩余时间：%s s\t\t",
                fileSize, currentFileSize, speed, remainSize / Constant.Param.MB, remainTime);

        System.out.print("\r" + downInfo);
    }
}

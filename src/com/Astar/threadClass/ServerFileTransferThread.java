package com.Astar.threadClass;

import com.Astar.infoClass.FileSliceInfo;
import com.Astar.infoClass.Log;
import com.Astar.resource.Constant;

import java.io.*;
import java.net.Socket;

public class ServerFileTransferThread implements Runnable {
    private final FileSliceInfo fileSliceInfo;
    private final Socket socket;

    private final TransferInfoThread transferInfoThread;

    private final int bufferSize;

    public ServerFileTransferThread(FileSliceInfo fileSliceInfo, Socket socket, TransferInfoThread transferInfoThread, int bufferSize) {
        this.fileSliceInfo = fileSliceInfo;
        this.socket = socket;
        this.transferInfoThread = transferInfoThread;
        this.bufferSize = bufferSize * 1024;
    }

    @Override
    public void run() {
        try (
                // 使用try-with-resources自动关闭资源
                RandomAccessFile raf = new RandomAccessFile(fileSliceInfo.getFilePath(), "r");
                // 创建序列化缓冲输出流
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                // 创建缓冲输入流
                BufferedInputStream bis = new BufferedInputStream(socket.getInputStream())
        ) {
            System.out.println(bufferSize);
            // 直接向客户端写入FileSliceInfo对象
            oos.writeObject(fileSliceInfo);

            // 等待接收客户端的确认
            while (bis.read() != Constant.Param.COMPLETE) {
                continue;
            }

            // 定位要读取文件的对应位置
            raf.seek(fileSliceInfo.getSliceStartIndex());

            // 缓冲区读取和发送文件
            byte[] buffer = new byte[bufferSize];
            int len;
            long total = 0;
            while ((len = raf.read(buffer)) != -1) {
                // 如果读取到的数据大于分片文件大小，则只读取到分片文件的大小即可
                if (total + len > fileSliceInfo.getSliceSize()) {
                    len = (int) (fileSliceInfo.getSliceSize() - total);
                }
                oos.write(buffer, 0, len);
                total += len;
                transferInfoThread.addTransferSize(len);
            }
            // 刷新缓冲区
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            Log.error("传输发生异常，程序终止...");
            System.exit(-1);
        } finally {
            try {
                // 关闭socket连接
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 打印分片文件接收完成
            System.out.print("\r");
            Log.info("分片文件 {} {} 发送完成\t\t\t\n", fileSliceInfo.getFileName(), fileSliceInfo.getSliceNum());
        }
    }
}

package com.Astar.threadClass;

import com.Astar.infoClass.FileSliceInfo;
import com.Astar.infoClass.Log;
import com.Astar.resource.Constant;

import java.io.*;
import java.net.Socket;

public class ServerFileTransferThread implements Runnable {
    private final FileSliceInfo fileSliceInfo;
    private final Socket socket;

    public ServerFileTransferThread(FileSliceInfo fileSliceInfo, Socket socket) {
        this.fileSliceInfo = fileSliceInfo;
        this.socket = socket;
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
            // 直接向客户端写入FileSliceInfo对象
            oos.writeObject(fileSliceInfo);

            // 等待接收客户端的确认
            if (bis.read() != Constant.Param.COMPLETE) {
                // 未读取到正确信息，直接退出程序
                Log.error("传输发生异常，程序终止...");
                System.exit(-1);
            }

            // 定位要读取文件的对应位置
            raf.seek(fileSliceInfo.getSliceStartIndex());

            // 使用循环和缓冲区读取和发送文件
            byte[] buffer = new byte[Constant.Param.DEFAULT_BUFFER_SIZE];
            int len;
            long total = 0;
            while ((len = raf.read(buffer)) != -1) {
                // 如果读取到的数据大于分片文件大小，则只读取到分片文件的大小即可
                if (total + len > fileSliceInfo.getSliceSize()){
                    len = (int) (fileSliceInfo.getSliceSize() - total + 1);
                }
                oos.write(buffer, 0, len);
                total += len;
            }
            // 刷新缓冲区
            oos.flush();
        } catch (IOException e) {
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
            Log.info("分片文件 {} {} 发送完成\n", fileSliceInfo.getFileName(), fileSliceInfo.getSliceNum());
        }
    }
}

package com.Astar.threadClass;

import com.Astar.infoClass.FileSliceInfo;
import com.Astar.infoClass.Log;
import com.Astar.resource.Constant;

import java.io.*;
import java.net.Socket;

public class ClientFileReceiveThread implements Runnable {
    private final String dirPath;
    private final Socket socket;
    private final TransferInfoThread transferInfoThread;

    public ClientFileReceiveThread(String dirPath, Socket socket, TransferInfoThread transferInfoThread) {
        this.dirPath = dirPath;
        this.socket = socket;
        this.transferInfoThread = transferInfoThread;
    }

    @Override
    public void run() {
        FileSliceInfo fileSliceInfo = null;
        RandomAccessFile raf = null;
        try (
                // 创建缓冲输出流
                BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
                // 创建缓冲输入流
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())
        ) {
            // 接受FileSliceInfo对象
            fileSliceInfo = (FileSliceInfo) ois.readObject();
            // 重新设置一下文件的大小
            transferInfoThread.setTotalSize(fileSliceInfo.getFileSize());

            // 发送接受完成信号
            bos.write(Constant.Param.COMPLETE);
            bos.flush();

            // 根据文件名和路径去创建文件
            raf = new RandomAccessFile(dirPath + "\\" + fileSliceInfo.getFileName(), "rw");

            // 定位要处理的文件的位置
            raf.seek(fileSliceInfo.getSliceStartIndex());

            // 默认128 * 1024大小的缓冲区，足够保证读取一次，接收到完整参数
            byte[] buffer = new byte[Constant.Param.DEFAULT_BUFFER_SIZE];
            int len;
            long total = 0;
            while ((len = ois.read(buffer)) != -1) {
                if (total + len > fileSliceInfo.getSliceSize()) {
                    len = (int) (fileSliceInfo.getSliceSize() - total);
                }
                raf.write(buffer, 0, len);
                total += len;
                transferInfoThread.addTransferSize(len);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.error("传输发生异常，程序终止...");
            System.exit(-1);
        } finally {
            try {
                // 关闭socket连接
                assert raf != null;
                raf.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 打印分片文件接收完成
            System.out.print("\r");
            Log.info("分片文件 {} {} 接收完成\n", fileSliceInfo.getFileName(), fileSliceInfo.getSliceNum());
        }
    }
}

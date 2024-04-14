package com.Astar.threadClass;

import com.Astar.infoClass.FileSliceInfo;
import com.Astar.infoClass.Log;
import com.Astar.resource.Constant;

import java.io.*;
import java.net.Socket;

public class ClientFileReceiveThread implements Runnable {
    private final String dirPath;
    private final Socket socket;

    public ClientFileReceiveThread(String dirPath, Socket socket) {
        this.dirPath = dirPath;
        this.socket = socket;
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

            // 发送接受完成信号
            bos.write(Constant.Param.COMPLETE);
            bos.flush();

            // 根据文件名和路径去创建文件
            raf = new RandomAccessFile(dirPath + "\\" + fileSliceInfo.getFileName(), "w");

            // 定位要处理的文件的位置
            raf.seek(fileSliceInfo.getSliceStartIndex());

            // 默认128 * 1024大小的缓冲区，足够保证读取一次，接收到完整参数
            byte[] buffer = new byte[Constant.Param.DEFAULT_BUFFER_SIZE];
            int len;
            long total = 0;
            while ((len = ois.read(buffer)) != -1){
                if (total + len > fileSliceInfo.getSliceSize()){
                    len = (int) (fileSliceInfo.getSliceSize() - total + 1);
                }
                raf.write(buffer, 0, len);
                total += len;
            }

        } catch (Exception e) {
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
            Log.info("分片文件 {} {} 接收完成\n", fileSliceInfo.getFileName(), fileSliceInfo.getSliceNum());
        }
    }
}

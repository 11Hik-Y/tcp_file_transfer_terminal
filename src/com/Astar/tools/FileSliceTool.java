package com.Astar.tools;

import com.Astar.infoClass.FileSliceInfo;

import java.io.File;
import java.util.ArrayList;

public class FileSliceTool {
    public static ArrayList<FileSliceInfo> fileSlice(String path, int sliceNum) {
        ArrayList<FileSliceInfo> fileSliceInfo = new ArrayList<>();

        File file = new File(path);
        long len = file.length();
        // 根据切片数量进行文件切分，得到每个切分文件的信息
        for (int i = 0; i < sliceNum; i++) {
            long sliceLength =
                    // 判断是否是最后一个分片
                    i == sliceNum - 1 ?
                            // 返回剩余的切片长度
                            len / sliceNum + len % sliceNum :
                            // 返回每个切片的长度
                            len / sliceNum;
            long endIndex =
                    i == sliceNum - 1 ?
                            len - 1 :
                            (i + 1) * sliceLength - 1;
            fileSliceInfo.add(
                    new FileSliceInfo(
                            file.getPath(),
                            file.getName(),
                            len,
                            i,
                            sliceLength,
                            // 使索引从0开始，方便后面的RandomAccessFile使用
                            i * (len / sliceNum),
                            endIndex
                    )
            );
        }
        return fileSliceInfo;
    }
}

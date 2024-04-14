package com.Astar.infoClass;

import java.io.Serializable;

public class FileSliceInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    // 切片文件路径
    private String filePath;
    // 切片文件名
    private String fileName;
    // 总文件大小
    private long fileSize;
    // 切片编号
    private int sliceNum;
    // 切片大小
    private long sliceSize;
    // 切片开始索引
    private long sliceStartIndex;
    // 切片结束索引
    private long sliceEndIndex;


    public FileSliceInfo() {
    }

    public FileSliceInfo(String filePath, String fileName, long fileSize, int sliceNum, long sliceSize, long sliceStartIndex, long sliceEndIndex) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.sliceNum = sliceNum;
        this.sliceSize = sliceSize;
        this.sliceStartIndex = sliceStartIndex;
        this.sliceEndIndex = sliceEndIndex;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public int getSliceNum() {
        return sliceNum;
    }

    public void setSliceNum(int sliceNum) {
        this.sliceNum = sliceNum;
    }

    public long getSliceSize() {
        return sliceSize;
    }

    public void setSliceSize(long sliceSize) {
        this.sliceSize = sliceSize;
    }

    public long getSliceStartIndex() {
        return sliceStartIndex;
    }

    public void setSliceStartIndex(long sliceStartIndex) {
        this.sliceStartIndex = sliceStartIndex;
    }

    public long getSliceEndIndex() {
        return sliceEndIndex;
    }

    public void setSliceEndIndex(long sliceEndIndex) {
        this.sliceEndIndex = sliceEndIndex;
    }

    public String toString() {
        return "FileSliceInfo{filePath = " + filePath + ", fileName = " + fileName + ", fileSize = " + fileSize + ", sliceNum = " + sliceNum + ", sliceSize = " + sliceSize + ", sliceStartIndex = " + sliceStartIndex + ", sliceEndIndex = " + sliceEndIndex + "}";
    }
}

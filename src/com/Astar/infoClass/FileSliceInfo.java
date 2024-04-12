package com.Astar.infoClass;

public class FileSliceInfo {
    // 切片文件名
    private String fileName;
    // 总文件大小
    private long fileSize;
    // 切片编号
    private long sliceNum;
    // 切片大小
    private long sliceSize;
    // 切片开始索引
    private long sliceStartIndex;
    // 切片结束索引
    private long sliceEndIndex;


    public FileSliceInfo() {
    }

    public FileSliceInfo(String fileName, long fileSize, long sliceNum, long sliceSize, long sliceStartIndex, long sliceEndIndex) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.sliceNum = sliceNum;
        this.sliceSize = sliceSize;
        this.sliceStartIndex = sliceStartIndex;
        this.sliceEndIndex = sliceEndIndex;
    }

    /**
     * 获取
     * @return fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 设置
     * @param fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 获取
     * @return fileSize
     */
    public long getFileSize() {
        return fileSize;
    }

    /**
     * 设置
     * @param fileSize
     */
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * 获取
     * @return sliceNum
     */
    public long getSliceNum() {
        return sliceNum;
    }

    /**
     * 设置
     * @param sliceNum
     */
    public void setSliceNum(long sliceNum) {
        this.sliceNum = sliceNum;
    }

    /**
     * 获取
     * @return sliceSize
     */
    public long getSliceSize() {
        return sliceSize;
    }

    /**
     * 设置
     * @param sliceSize
     */
    public void setSliceSize(long sliceSize) {
        this.sliceSize = sliceSize;
    }

    /**
     * 获取
     * @return sliceStartIndex
     */
    public long getSliceStartIndex() {
        return sliceStartIndex;
    }

    /**
     * 设置
     * @param sliceStartIndex
     */
    public void setSliceStartIndex(long sliceStartIndex) {
        this.sliceStartIndex = sliceStartIndex;
    }

    /**
     * 获取
     * @return sliceEndIndex
     */
    public long getSliceEndIndex() {
        return sliceEndIndex;
    }

    /**
     * 设置
     * @param sliceEndIndex
     */
    public void setSliceEndIndex(long sliceEndIndex) {
        this.sliceEndIndex = sliceEndIndex;
    }

    public String toString() {
        return "FileSliceInfo{fileName = " + fileName + ", fileSize = " + fileSize + ", sliceNum = " + sliceNum + ", sliceSize = " + sliceSize + ", sliceStartIndex = " + sliceStartIndex + ", sliceEndIndex = " + sliceEndIndex + "}";
    }
}

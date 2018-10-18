package com.example.problem3.model;

public class FileInfoResponse extends Response{

    private int totalChunks;

    private long fileSize;

    private String fileUrl;

    public FileInfoResponse(int totalChunks,long fileSize,String fileUrl){
        this.fileSize = fileSize;
        this.totalChunks = totalChunks;
        this.fileUrl = fileUrl;
    }

    public FileInfoResponse(String error){
        super(true,error);
    }

    public long getFileSize() {
        return fileSize;
    }

    public int getTotalChunks() {
        return totalChunks;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}

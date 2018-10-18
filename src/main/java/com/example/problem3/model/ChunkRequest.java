package com.example.problem3.model;

public class ChunkRequest {

    private int chunkNum;

    private String fileUrl;

    public ChunkRequest(int chunkNum,String fileUrl){
        this.chunkNum = chunkNum;
        this.fileUrl = fileUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public int getChunkNum() {
        return chunkNum;
    }

    public void setChunkNum(int chunkNum) {
        this.chunkNum = chunkNum;
    }
}

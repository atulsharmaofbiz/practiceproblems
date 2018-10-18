package com.example.problem3.model;

public class ChunkResponse extends Response{

    private int totalChunks;

    private int currentChunk;

    private byte[] chunkData;

    private String fileUrl;

    public ChunkResponse(boolean error,String errorDesc){
        super(error,errorDesc);
    }

    public ChunkResponse(int totalChunks,int currentChunk,byte[] chunkData,String fileUrl){
        this.totalChunks = totalChunks;
        this.currentChunk = currentChunk;
        this.chunkData = chunkData;
        this.fileUrl = fileUrl;
    }

    public int getTotalChunks() {
        return totalChunks;
    }

    public int getCurrentChunk() {
        return currentChunk;
    }

    public byte[] getChunkData() {
        return chunkData;
    }

    public String getFileUrl() {
        return fileUrl;
    }
}

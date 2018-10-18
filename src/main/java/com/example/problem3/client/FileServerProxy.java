package com.example.problem3.client;

import com.example.problem3.model.ChunkRequest;
import com.example.problem3.model.ChunkResponse;
import com.example.problem3.model.FileInfoRequest;
import com.example.problem3.model.FileInfoResponse;
import com.example.problem3.server.FileServer;

import java.util.concurrent.ExecutionException;

public class FileServerProxy {

    private FileServer fileServer;

    public FileServerProxy(FileServer fileServer){
        this.fileServer = fileServer;
    }

    public ChunkResponse getFileChunk(ChunkRequest chunkRequest) throws ExecutionException, InterruptedException {
        return fileServer.getChunk(chunkRequest);
    }

    public FileInfoResponse getFileChunkInfo(FileInfoRequest fileInfoRequest){
        return fileServer.getFileInfo(fileInfoRequest);
    }

}

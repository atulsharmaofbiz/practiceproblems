package com.example.problem3.server;


import com.example.problem3.FileUtil;
import com.example.problem3.model.*;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.*;

public class FileServer {

    private static Logger logger = Logger.getLogger(FileServer.class);

    private ExecutorService executorService;

    private boolean isEnabled;

    public static final int POOL_SIZE = 10;

    public void start() {
        executorService = Executors.newFixedThreadPool(POOL_SIZE);
        logger.info("Started File Server....");
    }

    public FileInfoResponse getFileInfo(FileInfoRequest fileInfoRequest) {
        FileInfoResponse response = null;
        try {
            File file = new File(fileInfoRequest.getFileUrl());
            return buildFileInfoResponse(file);
        } catch (Exception ex) {
            logger.error(ex);
            response = new FileInfoResponse(ex.getMessage());
            return response;
        }
    }

    public ChunkResponse getChunk(ChunkRequest chunkRequest) throws ExecutionException, InterruptedException {
        Future<ChunkResponse> future=executorService.submit(new Callable<ChunkResponse>() {

            @Override
            public ChunkResponse call() throws Exception {
                return readChunk(chunkRequest);
            }
        });
        return future.get();
    }

    private ChunkResponse readChunk(ChunkRequest chunkRequest) {
        try {
            byte[] chunkData=FileUtil.readChunk(chunkRequest.getChunkNum(), chunkRequest.getFileUrl());
            int totalChunks = FileUtil.numOfChunks(new File(chunkRequest.getFileUrl()).length());
            return new ChunkResponse(totalChunks,chunkRequest.getChunkNum(),chunkData,chunkRequest.getFileUrl());
        } catch (IOException e) {
            return new ChunkResponse(true,e.getMessage());
        }

    }

    private FileInfoResponse buildFileInfoResponse(File file) {
        int numOfChunks = FileUtil.numOfChunks(file.length());
        long fileSize = file.length();
        return new FileInfoResponse(numOfChunks, fileSize, file.getAbsolutePath());
    }

}
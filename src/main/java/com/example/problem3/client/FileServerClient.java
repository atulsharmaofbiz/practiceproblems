package com.example.problem3.client;

import com.example.problem3.FileUtil;
import com.example.problem3.model.ChunkRequest;
import com.example.problem3.model.ChunkResponse;
import com.example.problem3.model.FileInfoRequest;
import com.example.problem3.model.FileInfoResponse;
import com.example.problem3.server.FileServer;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class FileServerClient {

    private FileServerProxy proxy;

    private static final int POOL_SIZE=10;

    public static final String TMPDIR = "/Users/atul/testtenders/test-file-server/out";

    public static final Logger logger = Logger.getLogger(FileServerClient.class);

    ExecutorService executors = Executors.newFixedThreadPool(POOL_SIZE);

    public FileServerClient(FileServerProxy proxy){
        this.proxy = proxy;
    }

    public void downloadFile(String fileUrl,String tgtFilePath) throws IOException, ExecutionException, InterruptedException {
        FileInfoRequest fileInfoRequest = new FileInfoRequest(fileUrl);
        FileInfoResponse fileInfo = proxy.getFileChunkInfo(fileInfoRequest);
        startDownloadTasks(fileInfo,tgtFilePath);
    }

    private void startDownloadTasks(FileInfoResponse fileInfoResponse,String tgtFilePath) throws IOException, ExecutionException, InterruptedException {
        List<Future> futures = new LinkedList<>();
        for(int i=0;i<fileInfoResponse.getTotalChunks();i++){
            ChunkRequest chunkRequest = new ChunkRequest(i,fileInfoResponse.getFileUrl());
            Future future=executors.submit(()->{
                try {
                    sendChunkRequest(chunkRequest);
                } catch (Exception e) {
                    logger.error(e);
                    System.out.println(e);
                    handleErrorResponse(e);
                }
            });
            futures.add(future);
        }
        System.out.println("Sent All Request");
        System.out.println("all request completed");
        for (int i=0;i<futures.size();i++){
            futures.get(i).get();
        }
        String chunkDir=FileUtil.getChunkDirPath(TMPDIR,fileInfoResponse.getFileUrl());
        FileUtil.combineChunks(chunkDir,tgtFilePath);
        new File(chunkDir).delete();
        logger.info("Combined All Chunks->"+tgtFilePath);
        System.out.println();
    }

    private void sendChunkRequest(ChunkRequest chunkRequest) throws IOException, ExecutionException, InterruptedException {
        handleChunkResponse(proxy.getFileChunk(chunkRequest));
    }

    public void handleChunkResponse(ChunkResponse chunkResponse)throws IOException {
        //write chunk to dir
        String chunkFilePath=FileUtil.getChunkFilePath(TMPDIR,chunkResponse.getFileUrl(),
                chunkResponse.getCurrentChunk());

        String parent=Paths.get(chunkFilePath).getParent().toString();
        boolean parentExists=FileUtil.fileExists(parent);
        if(parentExists==false){
            synchronized (this){
                if(parentExists==false)
                    FileUtil.createDir(parent);
            }
        }
        FileUtil.writeToFile(chunkResponse.getChunkData(),chunkFilePath);
    }

    public void handleErrorResponse(Exception ex){

    }

}

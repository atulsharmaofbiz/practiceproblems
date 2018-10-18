package com.example.problem3.model;

public class FileInfoRequest {

    private String fileUrl;

    public FileInfoRequest(String fileUrl){
        this.fileUrl = fileUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }
}

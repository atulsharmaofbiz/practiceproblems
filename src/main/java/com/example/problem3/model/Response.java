package com.example.problem3.model;

public class Response {

    private boolean isError;

    private String error;

    public Response(){

    }

    public Response(boolean isError,String error){
        this.isError = isError;
        this.error = error;
    }

}

package com.example;

public class CustomException extends Exception {

    public CustomException(String message,Exception ex){
        super(message,ex);
    }
}

package com.example.problem1;

public class CustomException extends Exception {

    public CustomException(String message,Exception ex){
        super(message,ex);
    }
}

package com.learnspringboot.demo.exception.custom_exception;

public class NotPermissionException extends Exception{
    public NotPermissionException(String str){
        super(str);
    }
}
package com.learnspringboot.demo.exception.custom_exception;

public class MyException extends Exception{
    public MyException(String string){
        super(string);
    }
}

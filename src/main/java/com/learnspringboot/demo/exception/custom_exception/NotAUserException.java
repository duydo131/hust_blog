package com.learnspringboot.demo.exception.custom_exception;

public class NotAUserException extends Exception{
    public NotAUserException(){
        super("You don't user.");
    }
}

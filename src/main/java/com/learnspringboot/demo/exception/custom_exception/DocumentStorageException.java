package com.learnspringboot.demo.exception.custom_exception;

public class DocumentStorageException extends  Exception{
    public DocumentStorageException(String string){
        super(string);
    }
}

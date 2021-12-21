package com.learnspringboot.demo.utils;

public interface IGenAccount {
    String genPassword(int length);
    String genUsername(String email) throws Exception;
}

package com.learnspringboot.demo.utils.impl;

import com.learnspringboot.demo.utils.IGenAccount;

import java.util.Random;

public class GenAccount implements IGenAccount {
    private String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
    private String specialCharacters = "!@#$%^&*(){}|[]<>?/";
    private String numbers = "1234567890";
    private String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
    private int lengthOfCombinedChar = combinedChars.length();
    Random random = new Random();

    @Override
    public String genPassword(int length) {
        random.setSeed(System.currentTimeMillis());
        StringBuilder password = new StringBuilder();
        while(length-- > 0) password.append(combinedChars.charAt(random.nextInt(lengthOfCombinedChar)));
        return password.toString();
    }

    @Override
    public String genUsername(String email) {
        int index = email.indexOf('@');
        return email.substring(0,index);
    }
}

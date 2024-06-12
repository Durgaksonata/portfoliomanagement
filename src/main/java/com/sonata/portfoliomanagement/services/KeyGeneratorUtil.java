//package com.sonata.portfoliomanagement.controllers;
//
//import javax.crypto.KeyGenerator;
//import javax.crypto.SecretKey;
//import java.security.NoSuchAlgorithmException;
//import java.security.SecureRandom;
//import java.util.Base64;
//
//public class KeyGeneratorUtil {
//
//    public static String generateRandomKey() throws NoSuchAlgorithmException {
//        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
//        keyGen.init(256); // for AES-256
//        SecretKey secretKey = keyGen.generateKey();
//        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
//    }
//
//    public static String getEnvKey() {
//        return System.getenv("AES_SECRET_KEY");
//    }
//}
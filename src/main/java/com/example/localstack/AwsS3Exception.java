package com.example.localstack;

public class AwsS3Exception extends RuntimeException{
    public AwsS3Exception(String message) {
        super(message);
    }
}

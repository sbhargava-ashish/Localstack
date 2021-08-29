package com.example.localstack;

import com.example.localstack.service.S3Service;
import com.example.localstack.service.SqsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootApplication
public class LocalstackApplication implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(LocalstackApplication.class);

    @Autowired
    private S3Service s3Service;

    @Autowired
    private SqsService sqsService;

    public static void main(String[] args) {
        SpringApplication.run(LocalstackApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        logger.debug("Adding files to cats bucket");
        IntStream.range(1, 20).forEach(i -> {
            String fileName = s3Service.addFile(new ByteArrayInputStream("data".getBytes()),
                    "cats", "fileName" + i);
            String transferredFileName = s3Service.transfer("cats", fileName, "macts", fileName + "_macts");
            sqsService.sendMessage(QUEUE_URL,
                    "file transferred with name" + transferredFileName);
        });

        s3Service.listBucketObject("macts");
        sqsService.receiveMessages(QUEUE_URL);
    }

    // this is just for test showcase
    public static final String QUEUE_URL = "http://localhost:4566/000000000000/macts";


}

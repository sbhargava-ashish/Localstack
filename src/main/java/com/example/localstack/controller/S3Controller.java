package com.example.localstack.controller;

import com.example.localstack.model.FileTransferRequest;
import com.example.localstack.service.S3Service;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class S3Controller {

    private final S3Service s3Service;

    public S3Controller(final S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PutMapping
    public void transferFile(@Valid @RequestBody FileTransferRequest request) {
        s3Service.transfer(request.getFromBucket(), request.getFromBucketFileName(),
                request.getToBucket(), request.getToBucketFileName());
    }

}

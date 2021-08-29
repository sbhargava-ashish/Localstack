package com.example.localstack.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FileTransferRequest {

    @NotNull
    private String fromBucket;

    @NotNull
    private String fromBucketFileName;

    @NotNull
    private String toBucket;

    @NotNull
    private String toBucketFileName;


}

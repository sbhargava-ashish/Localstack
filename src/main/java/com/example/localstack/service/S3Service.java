package com.example.localstack.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.Copy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.amazonaws.services.s3.transfer.TransferManager;

import java.io.InputStream;
import java.util.List;


@Service
public class S3Service {

    private final Logger logger = LoggerFactory.getLogger(S3Service.class);

    private final AmazonS3 amazonS3;

    private final TransferManager transferManager;

    private final SqsService sqsService;

    public S3Service(AmazonS3 amazonS3, TransferManager transferManager, SqsService sqsService) {
        this.amazonS3 = amazonS3;
        this.transferManager = transferManager;
        this.sqsService = sqsService;
    }

    public String createBucket(final String bucketName) {
        try {
            CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
            Bucket bucket = amazonS3.createBucket(createBucketRequest);
            logger.debug("Bucket created with name {}", bucketName);
            return bucket.getName();
        } catch (Exception ex) {
            logger.error("Exception while creating bucket", ex);
        }
        return bucketName;
    }

    public void listBucket() {
        try {
            List<Bucket> buckets = amazonS3.listBuckets();
            buckets.stream().forEach(bucket -> logger.debug(bucket.getName()));
        } catch (Exception ex) {
            logger.error("Error", ex);
        }
    }

    public void listBucketObject(String bucketName) {
        try {
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
            listObjectsRequest.setBucketName(bucketName);
            ObjectListing objectListing = amazonS3.listObjects(listObjectsRequest);
            objectListing.getObjectSummaries().forEach(s3ObjectSummary -> {
                logger.debug("File name in bucket {} in bucketName", s3ObjectSummary.getKey());
            });
        } catch (Exception ex) {
            logger.error("Error", ex);
        }

    }

    public String addFile(InputStream file, String bucketName, String fileName) {
        try {
            ObjectMetadata data = new ObjectMetadata();
            PutObjectRequest putObjectRequest =
                    new PutObjectRequest(bucketName, fileName, file, data);
            PutObjectResult putObjectResult = amazonS3.putObject(putObjectRequest);
            logger.debug("File created with name {} in bucket {}", fileName, bucketName);
            return fileName;
        } catch (Exception ex) {
            logger.error("Error", ex);
        }
        return null;
    }

    public String transfer(String fromBucket, String fromKey, String toBucket, String toKey) {
        try {
            CopyObjectRequest copyObjectRequest =
                    new CopyObjectRequest(fromBucket, fromKey, toBucket, toKey);
            Copy copy = transferManager.copy(copyObjectRequest);
            copy.waitForCompletion();
            logger.debug("file copied");
            return toKey;
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }

        return null;
    }


}

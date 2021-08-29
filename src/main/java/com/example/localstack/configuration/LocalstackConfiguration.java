package com.example.localstack.configuration;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("localstack")
public class LocalstackConfiguration {

    private static final String DUMMY_KEY = "dummy";
    private static final String DUMMY_SECRET = "dummy-key";
    public static final String ENDPOINT_URL = "http://127.0.0.1:4566";

    @Bean
    AmazonS3 amazonS3() {
        final AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard().
                withEndpointConfiguration(buildEndpointConfiguration(ENDPOINT_URL)).
                withCredentials(buildAWSCredentialsProvider()).build();
       return amazonS3;
    }

    @Bean
    TransferManager transferManager(AmazonS3 amazonS3) {
         final TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(amazonS3).build();
        return transferManager;
    }


    @Bean
    AmazonSQSAsync amazonSQSAsync() {
        AmazonSQSAsync amazonSQSAsync = AmazonSQSAsyncClientBuilder
                .standard()
                .withEndpointConfiguration(buildEndpointConfiguration(ENDPOINT_URL))
                .withCredentials(buildAWSCredentialsProvider()).build();
        return amazonSQSAsync;
    }

    private AwsClientBuilder.EndpointConfiguration buildEndpointConfiguration(final String endpointUrl) {
        return new AwsClientBuilder.EndpointConfiguration(endpointUrl, Regions.AP_SOUTHEAST_2.getName());
    }


    @Bean
    public AWSCredentialsProvider buildAWSCredentialsProvider() {
        return new AWSStaticCredentialsProvider(new BasicAWSCredentials(DUMMY_KEY, DUMMY_SECRET));
    }
}

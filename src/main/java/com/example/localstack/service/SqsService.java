package com.example.localstack.service;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.*;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class SqsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AmazonSQSAsync amazonSQSAsync;

    public SqsService(AmazonSQSAsync amazonSQSAsync) {
        this.amazonSQSAsync = amazonSQSAsync;
    }

    @PostConstruct
    public void createSqsQueue(){
        try {
            CreateQueueRequest createQueueRequest = new CreateQueueRequest("macts");
            CreateQueueResult queueResult = amazonSQSAsync.createQueue(createQueueRequest);
            logger.debug("Queue created with queue url {}", queueResult.getQueueUrl());
        } catch (Exception ex) {
            logger.error("Exception while creating sqs queue", ex);
        }
    }

    public void sendMessage(String queueUrl, String message) {
        try {
            SendMessageRequest sendMessageRequest = new SendMessageRequest(queueUrl, message);
            SendMessageResult sendMessageResult = amazonSQSAsync.sendMessage(sendMessageRequest);
            logger.debug("Message send with id {}",sendMessageResult.getMessageId());
        } catch (Exception ex) {
            logger.error("Exception while sending message", ex);
        }
    }

    public void receiveMessages(String queueUrl) {
        try {
            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
            receiveMessageRequest.setMaxNumberOfMessages(10);
            ReceiveMessageResult receiveMessageResult = amazonSQSAsync.receiveMessage(receiveMessageRequest);
            receiveMessageResult.getMessages().forEach(message -> {
                logger.debug("Message in queue {}" , message);
            });
        } catch (Exception ex) {
            logger.error("Exception", ex);
        }
    }
 }

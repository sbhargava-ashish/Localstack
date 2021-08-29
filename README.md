# Localstack
Localstack with s3 and sqs and trasferManager

Note : this is just done to showcase the basic functionality and this can be used to implement much better use case to use amazon services

Please check the files :

1. Configuration :

LocalstackConfiguration : this is how we initialise amazon different client and this one have s3, transferManager and amazonSqs for now.

2. Runner :

Main class contaain run method which crates the files in cats bucket also copies into macts bucket and also send message 
to sqs which is retried at the end by the same class.


3. Services :

SqsService and S3 service contains couple of utility methods to interat with s3 and sqs

4. Controller: 

basic controller to transfer 

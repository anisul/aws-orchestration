package com.example.bucket.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.example.bucket.types.BucketVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.security.x509.AttributeNameEnumeration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AmazonS3ClientService {

    private AmazonS3 s3client;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;
    @Value("${amazonProperties.bucketName}")
    private String bucketName;
    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    @PostConstruct
    private void initializeAmazon() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = AmazonS3Client.builder()
                .withRegion(Regions.EU_WEST_3)
                .withForceGlobalBucketAccessEnabled(true)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    public String uploadFile(String bucketName, MultipartFile multipartFile) {
        String fileUrl = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            String fileName = generateFileName(multipartFile);
            fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
            uploadFileTos3bucket(bucketName, fileName, file);
            file.delete();
        } catch (Exception e) {
           e.printStackTrace();
        }
        return fileUrl;
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    private void uploadFileTos3bucket(String bucketName, String fileName, File file) {
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    public void deleteFileFromS3Bucket(String bucketName, String fileName) {
        //String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        s3client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
    }

    public S3ObjectInputStream downloadFile(String bucketName, String fileName) {
        S3Object s3Object = s3client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        return inputStream;
    }

    public List<BucketVO> getListOfAllBucket() {
        List<Bucket> buckets = s3client.listBuckets();
        List<BucketVO> bucketVOList = new ArrayList<>();

        for (Bucket bucket: buckets) {
            BucketVO bucketVO = new BucketVO();
            bucketVO.setName(bucket.getName());
            bucketVO.setCreationDate(bucket.getCreationDate());

            bucketVOList.add(bucketVO);
        }
        return bucketVOList;
    }

    public Bucket createBucket(String name, String region) {
        Bucket bucket = null;
        if (!s3client.doesBucketExistV2(name)) {
            if (region.equals("ap-southeast-1")) {
                return s3client.createBucket(new CreateBucketRequest(name, Region.AP_Singapore));
            } else if (region.equals("eu-central-1")) {
                return s3client.createBucket(new CreateBucketRequest(name, Region.EU_Frankfurt));
            } else if (region.equals("ca-central-1")){
                return s3client.createBucket(new CreateBucketRequest(name, Region.CA_Central));
            }
        }
        return bucket;
    }
}

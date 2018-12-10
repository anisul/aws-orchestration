package com.example.bucket.types;

import org.springframework.web.multipart.MultipartFile;

public class ObjectVO {
    String bucketName;
    String fileName;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "ObjectVO{" +
                "bucketName='" + bucketName + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}

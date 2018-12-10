package com.example.bucket.types;

public class CreateInstanceInput {
    String imageId;
    String instanceType;
    String keyName;
    int count;
    String tagName;
    String tagValue;
    String securityGroupName;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    public String getSecurityGroupName() {
        return securityGroupName;
    }

    public void setSecurityGroupName(String securityGroupName) {
        this.securityGroupName = securityGroupName;
    }

    @Override
    public String toString() {
        return "CreateInstanceInput{" +
                "imageId='" + imageId + '\'' +
                ", instanceType='" + instanceType + '\'' +
                ", keyName='" + keyName + '\'' +
                ", count=" + count +
                ", tagName='" + tagName + '\'' +
                ", tagValue='" + tagValue + '\'' +
                ", securityGroupName='" + securityGroupName + '\'' +
                '}';
    }
}

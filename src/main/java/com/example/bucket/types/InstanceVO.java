package com.example.bucket.types;

public class InstanceVO {
    String imageId;
    String subnetId;
    String instanceId;
    String instanceType;
    String state;
    String keyName;
    String kernelId;
    String privateIp;
    String publicIp;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getSubnetId() {
        return subnetId;
    }

    public void setSubnetId(String subnetId) {
        this.subnetId = subnetId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getKernelId() {
        return kernelId;
    }

    public void setKernelId(String kernelId) {
        this.kernelId = kernelId;
    }

    public String getPrivateIp() {
        return privateIp;
    }

    public void setPrivateIp(String privateIp) {
        this.privateIp = privateIp;
    }

    public String getPublicIp() {
        return publicIp;
    }

    public void setPublicIp(String publicIp) {
        this.publicIp = publicIp;
    }

    @Override
    public String toString() {
        return "InstanceVO{" +
                "imageId='" + imageId + '\'' +
                ", subnetId='" + subnetId + '\'' +
                ", instanceId='" + instanceId + '\'' +
                ", instanceType='" + instanceType + '\'' +
                ", state='" + state + '\'' +
                ", keyName='" + keyName + '\'' +
                ", kernelId='" + kernelId + '\'' +
                ", privateIp='" + privateIp + '\'' +
                ", publicIp='" + publicIp + '\'' +
                '}';
    }
}

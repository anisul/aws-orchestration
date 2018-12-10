package com.example.bucket.types;

public class GetInstancesInput {
    String keyName;
    String keyValue;

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    @Override
    public String toString() {
        return "GetInstancesInput{" +
                "keyName='" + keyName + '\'' +
                ", keyValue='" + keyValue + '\'' +
                '}';
    }
}

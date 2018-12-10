package com.example.bucket.types;

public class CreateKeypairInput {
    public String keyName;

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    @Override
    public String toString() {
        return "CreateKeypairInput{" +
                "keyName='" + keyName + '\'' +
                '}';
    }
}

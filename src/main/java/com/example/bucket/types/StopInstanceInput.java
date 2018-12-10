package com.example.bucket.types;

public class StopInstanceInput {
    String instanceId;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    @Override
    public String toString() {
        return "StopInstanceInput{" +
                "instanceId='" + instanceId + '\'' +
                '}';
    }
}

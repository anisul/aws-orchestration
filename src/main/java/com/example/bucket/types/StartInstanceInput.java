package com.example.bucket.types;

public class StartInstanceInput {
    String instanceId;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    @Override
    public String toString() {
        return "StartInstanceInput{" +
                "instanceId='" + instanceId + '\'' +
                '}';
    }
}

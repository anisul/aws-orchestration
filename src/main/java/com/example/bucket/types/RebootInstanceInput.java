package com.example.bucket.types;

public class RebootInstanceInput {
    String instanceId;

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    @Override
    public String toString() {
        return "RebootInstanceInput{" +
                "instanceId='" + instanceId + '\'' +
                '}';
    }
}

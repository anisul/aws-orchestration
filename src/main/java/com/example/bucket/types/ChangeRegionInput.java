package com.example.bucket.types;

public class ChangeRegionInput {
    String region;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return "ChangeRegionInput{" +
                "region='" + region + '\'' +
                '}';
    }
}

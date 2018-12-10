package com.example.bucket.types;

import java.util.Date;

public class BucketVO {
    public String name;
    public String region;
    public Date creationDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "BucketVO{" +
                "name='" + name + '\'' +
                ", region='" + region + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}

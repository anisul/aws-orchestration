package com.example.bucket.types;

public class CreateSecurityGroupInput {
    String name;
    String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CreateSecurityGroupInput{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

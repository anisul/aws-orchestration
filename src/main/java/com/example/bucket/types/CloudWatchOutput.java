package com.example.bucket.types;

public class CloudWatchOutput {
    double avgCPU;
    double netIn;
    double netOut;

    public double getAvgCPU() {
        return avgCPU;
    }

    public void setAvgCPU(double avgCPU) {
        this.avgCPU = avgCPU;
    }

    public double getNetIn() {
        return netIn;
    }

    public void setNetIn(double netIn) {
        this.netIn = netIn;
    }

    public double getNetOut() {
        return netOut;
    }

    public void setNetOut(double netOut) {
        this.netOut = netOut;
    }

    @Override
    public String toString() {
        return "CloudWatchOutput{" +
                "avgCPU=" + avgCPU +
                ", netIn=" + netIn +
                ", netOut=" + netOut +
                '}';
    }
}

package com.example.bucket.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import com.amazonaws.services.pi.model.DataPoint;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

@Service
public class AmazonCloudWatchService {
    private AmazonCloudWatch amazonCloudWatchClient;
    public static final Regions DEFAULT_REGION = Regions.EU_WEST_3;

    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    @PostConstruct
    private void initializeAmazon(){
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.amazonCloudWatchClient = AmazonCloudWatchClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(DEFAULT_REGION)
                .build();

        AWSSimpleSystemsManagement ssm = AWSSimpleSystemsManagementClientBuilder
                .standard()
                .withRegion(DEFAULT_REGION)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    public void reInitializeAmazon(Regions regions) {
        this.amazonCloudWatchClient = null;
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.amazonCloudWatchClient = AmazonCloudWatchClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(regions)
                .build();
    }

    public double getInstanceAverageLoad(String instanceId, String metricName) {
        long offsetInMilliseconds = 1000 * 60 * 60;
        GetMetricStatisticsRequest request = new GetMetricStatisticsRequest()
                .withStartTime(new Date(new Date().getTime() - offsetInMilliseconds))
                .withNamespace("AWS/EC2")
                .withPeriod(60 * 60)
                .withDimensions(new Dimension().withName("InstanceId").withValue(instanceId))
                .withMetricName(metricName)
                .withStatistics("Average", "Maximum")
                .withEndTime(new Date());
        GetMetricStatisticsResult getMetricStatisticsResult = this.amazonCloudWatchClient.getMetricStatistics(request);

        double result = 0;
        List dataPoint = getMetricStatisticsResult.getDatapoints();
        for (Object aDataPoint : dataPoint) {
            Datapoint dp = (Datapoint) aDataPoint;
            result = dp.getAverage();
        }

        return result;
    }

    public double getLoad(String instanceId, String metricName) {
        long offsetInMilliseconds = 1000 * 60 * 60;
        GetMetricStatisticsRequest request = new GetMetricStatisticsRequest()
                .withStartTime(new Date(new Date().getTime() - offsetInMilliseconds))
                .withNamespace("AWS/EC2")
                .withPeriod(60 * 60)
                .withDimensions(new Dimension().withName("InstanceId").withValue(instanceId))
                .withMetricName(metricName)
                .withStatistics("Sum")
                .withEndTime(new Date());

        GetMetricStatisticsResult getMetricStatisticsResult = this.amazonCloudWatchClient.getMetricStatistics(request);
        List<Datapoint> dataPoints = getMetricStatisticsResult.getDatapoints();

        if (dataPoints.isEmpty()) {
            return 0;
        } else {
            return dataPoints.get(dataPoints.size() - 1).getSum();
        }
    }

}

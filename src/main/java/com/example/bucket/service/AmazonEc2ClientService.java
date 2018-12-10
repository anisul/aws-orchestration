package com.example.bucket.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AmazonEc2ClientService {
    private AmazonEC2 ec2Client;
    public static final Regions DEFAULT_REGION = Regions.EU_WEST_3;
    private Regions currentRegion = DEFAULT_REGION;


    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    @Value("${amazonProperties.secretKey}")
    private String secretKey;


    @PostConstruct
    private void initializeAmazon(){
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.ec2Client = AmazonEC2Client
                .builder()
                .withRegion(DEFAULT_REGION)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    public void reInitializeAmazon(Regions regions){
        this.ec2Client = null;
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.ec2Client = AmazonEC2Client
                .builder()
                .withRegion(regions)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();

        currentRegion = regions;
    }

    public List<KeyPairInfo> getAllKeyPair() {
        List<KeyPairInfo> output = new ArrayList<>();
        DescribeKeyPairsResult result = ec2Client.describeKeyPairs();

        for (KeyPairInfo keyPairInfo : result.getKeyPairs()) {
            output.add(keyPairInfo);
        }

        return output;
    }

    public List<Region> getAllRegions() {
        List<Region> regionList = new ArrayList<>();
        DescribeRegionsResult regionsResult = ec2Client.describeRegions();

        for (Region region: regionsResult.getRegions()) {
            regionList.add(region);
        }
        return regionList;
    }

    public List<AvailabilityZone> getAllAvailableZone() {
        List<AvailabilityZone> availabilityZoneList = new ArrayList<>();
        DescribeAvailabilityZonesResult availabilityZonesResult = ec2Client.describeAvailabilityZones();

        for (AvailabilityZone availabilityZone: availabilityZonesResult.getAvailabilityZones()) {
            availabilityZoneList.add(availabilityZone);
        }
        return availabilityZoneList;
    }

    public List<SecurityGroup> getAllSecurityGroup() {
        DescribeSecurityGroupsRequest request = new DescribeSecurityGroupsRequest();
        DescribeSecurityGroupsResult result = ec2Client.describeSecurityGroups(request);
        return result.getSecurityGroups();
    }

    public String createSecurityGroup(String name, String description) {
        //create security group
        CreateSecurityGroupRequest request = new CreateSecurityGroupRequest()
                .withGroupName(name)
                .withDescription(description);

        CreateSecurityGroupResult result = ec2Client.createSecurityGroup(request);

        //allow network traffic (HTTP traffic from any IP address)
        IpRange ipRange = new IpRange().withCidrIp("0.0.0.0/0");
        IpPermission ipPermission = new IpPermission()
                .withIpv4Ranges(Arrays.asList(new IpRange[] { ipRange }))
                .withIpProtocol("tcp")
                .withFromPort(80)
                .withToPort(80);

        // attach the ipRange instance to an AuthorizeSecurityGroupIngressRequest
        AuthorizeSecurityGroupIngressRequest authorizeSecurityGroupIngressRequest
                = new AuthorizeSecurityGroupIngressRequest()
                .withGroupName(name)
                .withIpPermissions(ipPermission);
        ec2Client.authorizeSecurityGroupIngress(authorizeSecurityGroupIngressRequest);

        return result.getGroupId();
    }

    public String createKeyPair(String keyName) {
        String privateKey;
        CreateKeyPairRequest createKeyPairRequest = new CreateKeyPairRequest()
                .withKeyName(keyName);
        CreateKeyPairResult createKeyPairResult = ec2Client.createKeyPair(createKeyPairRequest);
        privateKey= createKeyPairResult.getKeyPair().getKeyMaterial();
        return privateKey;
    }

    public String createInstance(String imageId,
                                 String instanceType,
                                 String keyName,
                                 int count,
                                 String tagName,
                                 String tagValue,
                                 String securityGroupName) {

        String createdInstanceId;

        TagSpecification tagSpecification = new TagSpecification();
        tagSpecification.setResourceType(ResourceType.Instance);

        List<Tag> tags = new ArrayList<>();
        Tag tag = new Tag().withKey(tagName).withValue(tagValue);
        tags.add(tag);

        if (!tags.isEmpty()) {
            tagSpecification.setTags(tags);
        }

        RunInstancesRequest runInstancesRequest = new RunInstancesRequest()
                .withImageId(imageId)
                .withInstanceType(instanceType)
                .withKeyName(keyName)
                .withMinCount(1)
                .withMaxCount(count)
                .withSecurityGroups(securityGroupName).withTagSpecifications(tagSpecification);
        RunInstancesResult result = ec2Client.runInstances(runInstancesRequest);

        String reservationId = result.getReservation().getReservationId();
        return reservationId;
    }

    public List<Instance> getAllInstancesByTag(String tagName, String tagValue) {
        List<Instance> output = new ArrayList<>();
        DescribeInstancesRequest request = new DescribeInstancesRequest();

        List<String> values = new ArrayList<>();
        values.add(tagValue);

        Filter filter = new Filter("tag:" + tagName, values);

        DescribeInstancesResult result = ec2Client.describeInstances(request.withFilters(filter));

        List<Reservation> reservations = result.getReservations();

        for (Reservation reservation: reservations) {
            List<Instance> instances = reservation.getInstances();
            for (Instance instance: instances) {
                output.add(instance);
            }
        }
        return output;
    }

    public void startInstance(String instanceId) {
        StartInstancesRequest request = new StartInstancesRequest()
                .withInstanceIds(instanceId);

        ec2Client.startInstances(request);
    }

    public void stopInstance(String instanceId) {
        StopInstancesRequest request = new StopInstancesRequest()
                .withInstanceIds(instanceId);

        ec2Client.stopInstances(request);
    }

    public void rebootInstance(String instanceId) {
        RebootInstancesRequest request = new RebootInstancesRequest()
                .withInstanceIds(instanceId);

        ec2Client.rebootInstances(request);
    }

    public Regions getCurrentRegion() {
        return currentRegion;
    }

    public void setCurrentRegion(Regions currentRegion) {
        this.currentRegion = currentRegion;
    }
}

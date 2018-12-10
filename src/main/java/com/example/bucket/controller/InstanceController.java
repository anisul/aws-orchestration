package com.example.bucket.controller;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedParallelScanList;
import com.amazonaws.services.ec2.model.*;
import com.example.bucket.service.AmazonCloudWatchService;
import com.example.bucket.service.AmazonEc2ClientService;
import com.example.bucket.types.*;
import com.example.bucket.utils.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ec2/")
public class InstanceController {
    private AmazonEc2ClientService amazonEc2ClientService;
    private AmazonCloudWatchService amazonCloudWatchService;

    @Autowired
    InstanceController(AmazonEc2ClientService amazonEc2ClientService, AmazonCloudWatchService amazonCloudWatchService) {
        this.amazonEc2ClientService = amazonEc2ClientService;
        this.amazonCloudWatchService = amazonCloudWatchService;
    }

    //get all available regions
    @GetMapping("/regions")
    public List<Region> getAllRegions() {
        return amazonEc2ClientService.getAllRegions();
    }

    //get all availability zone in current Region
    @GetMapping("/available-zones")
    public List<AvailabilityZone> getAllAvailabilityZone() {
        return amazonEc2ClientService.getAllAvailableZone();
    }

    //get all instance list
    @PostMapping("/instances")
    public GetInstancesOutput getAllInstances(@RequestBody GetInstancesInput input) {
        GetInstancesOutput output = new GetInstancesOutput();
        List<Instance> instances = new ArrayList<>();
        List<InstanceVO> result = new ArrayList<>();

        instances = amazonEc2ClientService.getAllInstancesByTag(input.getKeyName(), input.getKeyValue());

        for (Instance instance: instances) {
            InstanceVO ivo = new InstanceVO();

            ivo.setImageId(instance.getImageId());
            ivo.setInstanceId(instance.getInstanceId());
            ivo.setInstanceType(instance.getInstanceType());
            ivo.setKernelId(instance.getKernelId());
            ivo.setState(instance.getState().getName());
            ivo.setSubnetId(instance.getSubnetId());
            ivo.setKeyName(instance.getKeyName());
            ivo.setPrivateIp(instance.getPrivateIpAddress());
            ivo.setPublicIp(instance.getPublicIpAddress());

            result.add(ivo);
        }

        output.setInstances(result);
        return output;
    }

    @GetMapping("/current-region")
    public Regions getCurrentRegion() {
        return amazonEc2ClientService.getCurrentRegion();
    }

    @PostMapping("/change-region")
    public ResponseEntity changeRegion(@RequestBody ChangeRegionInput input) {
        try {
            amazonEc2ClientService.reInitializeAmazon(AppUtil.getRegionsFromString(input.getRegion()));
            amazonCloudWatchService.reInitializeAmazon(AppUtil.getRegionsFromString(input.getRegion()));
            return ResponseEntity.status(HttpStatus.OK).body("Successfully changed the region.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Couldn't change region ("+ e.getMessage() +")");
        }
    }

    //get all security group
    @GetMapping("/security-groups")
    public List<SecurityGroup> getAllSecurityGroups() {
        return amazonEc2ClientService.getAllSecurityGroup();
    }


    @GetMapping("/key-pairs")
    public List<KeyPairInfo> getAllKeyPairs() {
        return amazonEc2ClientService.getAllKeyPair();
    }

    //create keypair
    @PostMapping("/create-keypair")
    public ResponseEntity createKeypair(@RequestBody CreateKeypairInput input) {
        try {
            String privateKey;
            privateKey = amazonEc2ClientService.createKeyPair(input.getKeyName());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SuccessMessage("Successfully created key-pair."));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ErrorMessage("Couldn't create the key-pair"));
        }
    }

    //create security group
    @PostMapping("/create-security-group")
    public ResponseEntity createSecurityGroup(@RequestBody CreateSecurityGroupInput input) {
        try {
            String securityGroupId;
            securityGroupId = amazonEc2ClientService.createSecurityGroup(input.getName(), input.getDescription());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SuccessMessage("Security group '"+ input.getName()+"' has been created (id: "+ securityGroupId+")"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ErrorMessage("Couldn't create the security group ("+ e.getMessage() + ")"));
        }
    }

    //- create instance
    @PostMapping("/create-instance")
    public ResponseEntity createInstance(@RequestBody CreateInstanceInput input) {
        try {
            String instanceId;
            instanceId = amazonEc2ClientService.createInstance(
                    input.getImageId(),
                    input.getInstanceType(),
                    input.getKeyName(),
                    input.getCount(),
                    input.getTagName(),
                    input.getTagValue(),
                    input.getSecurityGroupName()
            );

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new SuccessMessage("Successfully created intance (ID: " + instanceId + ")"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ErrorMessage("Couldn't create the instance ("+ e.getMessage() +")"));
        }
    }


    //start instance
    @PostMapping("/start-instance")
    public ResponseEntity startInstance(@RequestBody StartInstanceInput input) {
        try {
            amazonEc2ClientService.startInstance(input.getInstanceId());
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessMessage("Starting instance '"+ input.getInstanceId() +"'..."));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ErrorMessage("Couldn't start instance (" + e.getMessage() + ")"));
        }
    }


    //stop instance
    @PostMapping("/stop-instance")
    public ResponseEntity stopInstance(@RequestBody StopInstanceInput input) {
        try {
            amazonEc2ClientService.stopInstance(input.getInstanceId());
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessMessage("Stopping instance '"+ input.getInstanceId() +"'..."));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ErrorMessage("Couldn't stop instance (" + e.getMessage() + ")"));
        }
    }


    //reboot instance
    @PostMapping("/reboot-instance")
    public ResponseEntity rebootInstance(@RequestBody RebootInstanceInput input) {
        try {
            amazonEc2ClientService.rebootInstance(input.getInstanceId());
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessMessage("Rebooting instance '"+ input.getInstanceId() +"'..."));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ErrorMessage("Couldn't reboot instance (" + e.getMessage() + ")"));
        }
    }

    @PostMapping("/cloud-watch")
    public ResponseEntity cloudWatch(@RequestBody CloudWatchInput input) {
        try {
            CloudWatchOutput output = new CloudWatchOutput();
            output.setAvgCPU(amazonCloudWatchService.getInstanceAverageLoad(input.getInstanceId(), "CPUUtilization"));
            output.setNetIn(amazonCloudWatchService.getLoad(input.getInstanceId(), "NetworkPacketsIn"));
            output.setNetOut(amazonCloudWatchService.getLoad(input.getInstanceId(), "NetworkPacketsOut"));
            return ResponseEntity.status(HttpStatus.OK).body(output);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ErrorMessage("Couldn't fetch CloudWatch data (" + e.getMessage() + ")"));
        }
    }
}

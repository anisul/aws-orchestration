package com.example.bucket.controller;

import com.amazonaws.util.IOUtils;
import com.example.bucket.service.AmazonS3ClientService;
import com.example.bucket.types.BucketVO;
import com.example.bucket.types.ErrorMessage;
import com.example.bucket.types.ObjectVO;
import com.example.bucket.types.SuccessMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/storage/")
public class BucketController {

    private AmazonS3ClientService amazonS3ClientService;

    @Autowired
    BucketController(AmazonS3ClientService amazonS3ClientService) {
        this.amazonS3ClientService = amazonS3ClientService;
    }


    @PostMapping("/uploadFile")
    public ResponseEntity uploadFile(@RequestPart(value = "file") MultipartFile file, @RequestPart(value = "bucketName") String bucketName) {

        try {
            long uploadStart = System.currentTimeMillis();
            String fileName = this.amazonS3ClientService.uploadFile(bucketName, file);
            long uploadfinish = System.currentTimeMillis();
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessMessage("Successfully uploaded file as: '"
                    + fileName + "' in bucket : '" + bucketName + "' (upload time: "+ (uploadfinish-uploadStart) + "ms)"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorMessage((e.getMessage())));
        }

    }

    @PostMapping("/download")
    public ResponseEntity getObject(@RequestBody ObjectVO objectVO, HttpServletResponse response) {
        try {
            InputStream inputStream = amazonS3ClientService.downloadFile(objectVO.getBucketName(), objectVO.getFileName());
            //response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment; filename="+ objectVO.getFileName() + "\"");
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
            inputStream.close();
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessMessage("Successfully downloaded file : '"
                    + objectVO.getFileName() + "' from bucket : '" + objectVO.getBucketName() +"'"));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorMessage((e.getMessage())));
        }
    }


    @PostMapping("/deleteFile")
    public ResponseEntity deleteFile(@RequestBody ObjectVO objectVO) {
        try {
            this.amazonS3ClientService.deleteFileFromS3Bucket(objectVO.getBucketName(), objectVO.getFileName());
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessMessage("Successfully deleted file '"
                    + objectVO.getFileName() + "' from bucket '" + objectVO.getBucketName() +"'"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorMessage((e.getMessage())));
        }
    }


    @GetMapping("/buckets")
    public List<BucketVO> listOfBuckets(){
        return amazonS3ClientService.getListOfAllBucket();
    }


    @PostMapping("/create-bucket")
    public ResponseEntity createBucket(@RequestBody BucketVO bucketVO) {
        try {
            amazonS3ClientService.createBucket(bucketVO.getName(), bucketVO.getRegion());
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessMessage("Successfully created bucket: "
                    + bucketVO.getName() + " in region: " + bucketVO.getRegion()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorMessage((e.getMessage())));
        }
    }
}

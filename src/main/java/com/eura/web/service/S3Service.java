package com.eura.web.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.internal.Mimetypes;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;

import java.io.IOException;

@Service
public class S3Service {
    private AmazonS3 s3Client;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
    }

    // upload 메서드 | 단일 파일 업로드
    public void upload(MultipartFile file, String key) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(Mimetypes.getInstance().getMimetype(file.getOriginalFilename()));
        uploadToS3(new PutObjectRequest(bucket, key, file.getInputStream(), metadata).withCannedAcl(CannedAccessControlList.PublicRead), key);
    }

    // PutObjectRequest는 Aws s3 버킷에 업로드할 객체 메타 데이터와 파일 데이터로 이루어져 있다.
    private String uploadToS3(PutObjectRequest putObjectRequest, String key) {
        String _url = "";
        try {
            this.s3Client.putObject(putObjectRequest);
            // System.out.println(String.format("[%s] upload complete", putObjectRequest.getKey()));
            // System.out.println(s3Client.getUrl(bucket, key).toString());
            _url = s3Client.getUrl(bucket, key).toString();
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _url;
    }

    // 복사 메서드
    public void copy(String orgkey, String copyKey) throws Exception {
        try {
            // copy 객체 생성
            CopyObjectRequest copyObjectRequest = new CopyObjectRequest(bucket, orgkey, bucket, copyKey);

            // copy
            this.s3Client.copyObject(copyObjectRequest);

            // System.out.printf(String.format("Finish copying [%s] to [%s]"), orgkey, copyKey);
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
    }

    // 삭제 메서드
    public void delete(String key) throws Exception {
        try {
            // Delete 객체 생성
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, key);

            // Delete
            this.s3Client.deleteObject(deleteObjectRequest);

            // System.out.printf(key);
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
    }
}

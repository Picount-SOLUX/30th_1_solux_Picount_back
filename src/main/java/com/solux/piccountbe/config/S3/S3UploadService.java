package com.solux.piccountbe.config.S3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 파일 업로드
    public String saveFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".")); // 확장자 추출
        String uuidFileName = UUID.randomUUID().toString() + extension; //uuid로 파일 이름 중복 방지 - prefix

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, uuidFileName, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, uuidFileName).toString();
    }

    // 파일 삭제
    public void deleteImage(String originalFilename)  {
        amazonS3.deleteObject(bucket, originalFilename);
    }
}
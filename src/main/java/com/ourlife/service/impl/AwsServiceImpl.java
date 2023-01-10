package com.ourlife.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ourlife.repository.ImgsRepository;
import com.ourlife.service.AwsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AwsServiceImpl implements AwsService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;

    private final ImgsRepository imgsRepository;

    public List<String> uploadFiles(List<MultipartFile> multipartFiles){
        List<String> fileNameList = new ArrayList<>();

        multipartFiles.forEach(file -> {
            String fileName = createFileName(file.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try(InputStream inputStream = file.getInputStream()){
                amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            }catch (IOException e){
                throw new StringIndexOutOfBoundsException();
                // 임시 exception
            }

            fileNameList.add(fileName);
        });

        return fileNameList;
    }

    public void deleteFile(String fileName){
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }

    public String getFile(String fileName){
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    private String createFileName(String originalFilename) {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFilename));
    }

    private String getFileExtension(String originalFilename) {
        try{
            return originalFilename.substring(originalFilename.lastIndexOf("."));
        }catch (StringIndexOutOfBoundsException e){
            throw new StringIndexOutOfBoundsException("이미지가 없습니다.");
            //오류 추가?
        }
    }


}

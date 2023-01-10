package com.ourlife.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AwsService {

    public List<String> uploadFiles(List<MultipartFile> multipartFiles);

    public void deleteFile(String fileName);
}

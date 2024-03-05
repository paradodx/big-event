package com.ipp.controller;

import com.ipp.pojo.Result;
import com.ipp.utils.AliyunOSSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FileUploadController {
    @Autowired
    private AliyunOSSUtil aliyunOSSUtil;
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {
        String url = aliyunOSSUtil.upload(file);
        return Result.success(url);
    }
}

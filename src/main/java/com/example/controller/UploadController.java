package com.example.controller;

import com.example.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${upload.url.prefix}")
    private String urlPrefix;

    @PostMapping("/avatar")
    public Result<?> uploadAvatar(@RequestParam("file") MultipartFile file, @RequestParam("openid") String openid) {
        if (file.isEmpty()) {
            return Result.error("请选择文件");
        }

        try {
            // 确保上传目录存在
            String avatarPath = uploadPath + "/avatar";
            File uploadDir = new File(avatarPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String filename = UUID.randomUUID().toString() + extension;

            // 保存文件
            Path filePath = Paths.get(avatarPath, filename);
            Files.write(filePath, file.getBytes());

            // 返回文件访问URL
            String fileUrl = urlPrefix + "/avatar/" + filename;
            log.info("头像上传成功 - openid: {}, url: {}", openid, fileUrl);

            return Result.success(new UploadResult(fileUrl));
        } catch (IOException e) {
            log.error("头像上传失败 - openid: " + openid, e);
            return Result.error("文件上传失败：" + e.getMessage());
        }
    }

    // 上传结果类
    private static class UploadResult {
        private String url;

        public UploadResult(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
} 
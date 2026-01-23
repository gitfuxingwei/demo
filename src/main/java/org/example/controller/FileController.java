package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.utils.MinIOUtils;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("/api/file")
@Slf4j
public class FileController {
    @Resource
    private MinIOUtils minIOUtils;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("上传失败: 文件为空");
            }

            // 检查文件大小 (限制为100MB)
            long maxSize = 100L * 1024 * 1024; // 100MB
            if (file.getSize() > maxSize) {
                return ResponseEntity.badRequest().body("上传失败: 文件大小不能超过100MB");
            }

            // 获取原始文件名和扩展名
            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null || !originalFileName.contains(".")) {
                return ResponseEntity.badRequest().body("上传失败: 文件没有扩展名");
            }

            String suffixName = originalFileName.substring(originalFileName.lastIndexOf("."));

            // 验证文件扩展名
            String[] allowedExtensions = {".jpg", ".jpeg", ".png", ".gif", ".pdf", ".doc", ".docx", ".xls", ".xlsx", ".txt", ".zip"};
            boolean isValidExtension = false;
            for (String ext : allowedExtensions) {
                if (ext.equalsIgnoreCase(suffixName)) {
                    isValidExtension = true;
                    break;
                }
            }

            if (!isValidExtension) {
                return ResponseEntity.badRequest().body("上传失败: 不允许的文件类型: " + suffixName);
            }

            // 使用MinIO工具类上传文件
            String fileUrl = minIOUtils.uploadFile(file);

            // 返回成功信息，包括文件访问URL
            return ResponseEntity.ok("文件上传成功: " + fileUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("上传失败: 处理过程中出现异常: " + e.getMessage());
        }
    }

    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {
        try {
            // 从配置文件获取默认存储桶名称
            String bucketName = minIOUtils.getDefaultBucketName();
            // 使用MinIO工具类下载文件
            minIOUtils.downloadFile(bucketName, fileName, response);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("下载文件失败: {}", e.getMessage());
        }
    }

    @GetMapping("/preview/{fileName}")
    public ResponseEntity<String> getPreviewUrl(@PathVariable String fileName) {
        try {
            // 获取bucket
            String bucketName = minIOUtils.getDefaultBucketName();
            // 获取预览链接
            log.info("获取预览链接: {}", bucketName);
            String previewUrl = minIOUtils.getPreviewUrl(bucketName, fileName);
            return ResponseEntity.ok(previewUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("获取预览链接失败: " + e.getMessage());
        }
    }
}

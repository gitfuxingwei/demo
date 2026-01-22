package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/file")
@Slf4j
public class FileController {
    private static final String FILE_DIRECTORY = "D:\\txhy\\AAAA\\";
    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("上传失败: 文件为空");
            }

            // 检查文件大小 (限制为100MB)
            long maxSize = 10L * 1024 * 1024 * 1024; // 100MB
            if (file.getSize() > maxSize) {
                return ResponseEntity.badRequest().body("上传失败: 文件大小不能超过100MB");
            }

            // 获取原始文件名和扩展名
            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null || !originalFileName.contains(".")) {
                return ResponseEntity.badRequest().body("上传失败: 文件没有扩展名");
            }

            String suffixName = originalFileName.substring(originalFileName.lastIndexOf("."));

            // 验证文件扩展名（防止恶意文件上传）
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

            // 生成唯一文件名
            String fileName = UUID.randomUUID() + suffixName;

            // 确保目录存在
            Path uploadDir = Paths.get(FILE_DIRECTORY);
            Files.createDirectories(uploadDir);

            // 创建目标文件路径
            Path filePath = uploadDir.resolve(fileName);
            File dest = filePath.toFile();

            // 转移文件到目标位置
            file.transferTo(dest);

            // 返回成功信息
            return ResponseEntity.ok("文件上传成功: " + dest.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("上传失败: 服务器内部错误");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("上传失败: 处理过程中出现异常");
        }
    }

    @GetMapping("/download")
    @ResponseBody
    public ResponseEntity<Resource> download(@RequestParam("fileName") String fileName, HttpServletRequest request) {
        try {
            Path filePath = Paths.get(FILE_DIRECTORY).resolve(fileName).normalize();
            if (!filePath.toFile().exists()) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            String contentType;
            try {
                contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            } catch (Exception ex) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}

package org.example.utils;

import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.util.RandomUtil;
import io.minio.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class MinIOUtils {
    @Resource
    private MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String defaultBucketName;

    public String getDefaultBucketName() {
        return defaultBucketName;
    }

    @Value("${minio.preview-expire}")
    private Integer previewExpire;

    /**
     * 上传文件
     * @param file 上传文件
     * @return 文件访问路径（预览链接）
     */
    public String uploadFile(MultipartFile file) throws Exception {
        return uploadFile(file, defaultBucketName);
    }

    /**
     * 上传文件
     * @param file 上传文件
     * @param bucketName 存储桶名称
     * @return 文件访问路径
     */
    public String uploadFile(MultipartFile file, String bucketName) throws Exception {

        // 处理文件名
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new IllegalArgumentException("文件没有扩展名");
        }
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = RandomUtil.randomString(16) + suffix; // 16位随机字符串+后缀

        // 上传文件到 MinIO
        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        }

        // 返回文件预览链接
        return getPreviewUrl(bucketName, fileName);
    }

    /**
     * 获取文件预览链接（带签名，过期自动失效）
     * @param bucketName 存储桶名称
     * @param fileName 文件名
     * @return 预览链接
     */
    public String getPreviewUrl(String bucketName, String fileName) throws Exception {
        // 生成签名 URL
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .method(Method.GET)
                        .expiry(previewExpire, TimeUnit.SECONDS)
                        .build()
        );
    }

    /**
     * 下载文件
     * @param bucketName 存储桶名称
     * @param fileName 文件名
     * @param response 响应对象
     */
    public void downloadFile(String bucketName, String fileName, HttpServletResponse response) throws Exception {
        // 1. 获取文件信息
        StatObjectResponse stat = minioClient.statObject(
                StatObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .build()
        );

        // 2. 设置响应头
        response.setContentType(stat.contentType());
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));

        // 3. 读取文件流并写入响应
        try (InputStream in = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .build()
        ); OutputStream out = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        }
    }

    /**
     * 删除文件
     * @param bucketName 存储桶名称
     * @param fileName 文件名
     */
    public void deleteFile(String bucketName, String fileName) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .build()
        );
        log.info("文件 {} 已从存储桶 {} 中删除", fileName, bucketName);
    }
}

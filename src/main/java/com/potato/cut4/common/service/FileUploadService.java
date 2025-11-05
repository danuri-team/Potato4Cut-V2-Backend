package com.potato.cut4.common.service;

import com.potato.cut4.common.config.R2Config;
import com.potato.cut4.common.exception.CustomException;
import com.potato.cut4.common.exception.ErrorCode;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploadService {

  private final S3Client s3Client;
  private final R2Config r2Config;

  private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
      "image/jpeg", "image/jpg", "image/png", "image/webp"
  );
  private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

  public String uploadImage(MultipartFile file, String directory) {
    validateImage(file);

    String fileName = generateFileName(file.getOriginalFilename());
    String key = directory + "/" + fileName;

    try (InputStream inputStream = file.getInputStream()) {
      PutObjectRequest putObjectRequest = PutObjectRequest.builder()
          .bucket(r2Config.getBucketName())
          .key(key)
          .contentType(file.getContentType())
          .build();

      s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, file.getSize()));

      String imageUrl = r2Config.getPublicUrl() + "/" + key;
      log.info("File uploaded successfully: {}", imageUrl);
      return imageUrl;

    } catch (IOException e) {
      log.error("Failed to upload file: {}", e.getMessage(), e);
      throw new CustomException(ErrorCode.FILE_UPLOAD_FAILED);
    }
  }

  public void deleteImage(String imageUrl) {
    try {
      String key = extractKeyFromUrl(imageUrl);

      DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
          .bucket(r2Config.getBucketName())
          .key(key)
          .build();

      s3Client.deleteObject(deleteObjectRequest);
      log.info("File deleted successfully: {}", key);

    } catch (Exception e) {
      log.error("Failed to delete file: {}", e.getMessage(), e);
      throw new CustomException(ErrorCode.FILE_DELETE_FAILED);
    }
  }

  private void validateImage(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "파일이 비어있습니다.");
    }

    if (!ALLOWED_IMAGE_TYPES.contains(file.getContentType())) {
      throw new CustomException(ErrorCode.INVALID_FILE_TYPE,
          "지원하는 이미지 형식: JPEG, PNG, WEBP");
    }

    if (file.getSize() > MAX_FILE_SIZE) {
      throw new CustomException(ErrorCode.FILE_SIZE_EXCEEDED,
          "파일 크기는 10MB를 초과할 수 없습니다.");
    }
  }

  private String generateFileName(String originalFilename) {
    String extension = "";
    if (originalFilename != null && originalFilename.contains(".")) {
      extension = originalFilename.substring(originalFilename.lastIndexOf("."));
    }
    return UUID.randomUUID() + extension;
  }

  private String extractKeyFromUrl(String imageUrl) {
    return imageUrl.replace(r2Config.getPublicUrl() + "/", "");
  }
}

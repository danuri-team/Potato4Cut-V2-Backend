package com.potato.cut4.common.service;

import com.potato.cut4.common.config.R2Config;
import com.potato.cut4.common.exception.CustomException;
import com.potato.cut4.common.exception.ErrorCode;
import com.potato.cut4.presentation.dto.response.PreSignedUrlResponse;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploadService {

  private final S3Client s3Client;
  private final S3Presigner s3Presigner;
  private final R2Config r2Config;

  private static final long MAX_FILE_SIZE = 10485760; // 10MB
  private static final String DEFAULT_FILE_TYPE = ".png";
  private static final String DEFAULT_CONTENT_TYPE = "image/png";
  private static final Duration DEFAULT_EXPIRE_TIME = Duration.ofMinutes(30);

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

  private String extractKeyFromUrl(String imageUrl) {
    return imageUrl.replace(r2Config.getPublicUrl() + "/", "");
  }

  public String buildImageUrl(String objectKey) {
    return r2Config.getPublicUrl() + "/" + objectKey;
  }

  public PreSignedUrlResponse generatePreSignedUrlForUpload(String directory, Long fileSize) {
    if (fileSize > MAX_FILE_SIZE) {
      throw new CustomException(ErrorCode.FILE_SIZE_EXCEEDED);
    }

    String fileName = UUID.randomUUID() + DEFAULT_FILE_TYPE;
    String key = directory + "/" + fileName;

    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(r2Config.getBucketName())
        .key(key)
        .contentType(DEFAULT_CONTENT_TYPE)
        .contentLength(fileSize)
        .build();

    PutObjectPresignRequest preSignRequest = PutObjectPresignRequest.builder()
        .signatureDuration(DEFAULT_EXPIRE_TIME)
        .putObjectRequest(putObjectRequest)
        .build();

    PresignedPutObjectRequest preSignedRequest = s3Presigner.presignPutObject(preSignRequest);

    String finalUrl = r2Config.getPublicUrl() + "/" + key;

    log.info("Generated preSigned URL for upload - key: {}", key);

    return new PreSignedUrlResponse(
        preSignedRequest.url().toString(),
        key,
        finalUrl,
        DEFAULT_CONTENT_TYPE,
        fileSize
    );
  }
}

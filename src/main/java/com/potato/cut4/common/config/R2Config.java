package com.potato.cut4.common.config;

import java.net.URI;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
@ConfigurationProperties(prefix = "cloudflare.r2")
@Getter
@RequiredArgsConstructor
public class R2Config {

  private String accessKey;
  private String secretKey;
  private String bucketName;
  private String accountId;
  private String endpoint;
  private String publicUrl;

  public void setAccessKey(String accessKey) {
    this.accessKey = accessKey;
  }

  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }

  public void setBucketName(String bucketName) {
    this.bucketName = bucketName;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
  }

  public void setPublicUrl(String publicUrl) {
    this.publicUrl = publicUrl;
  }

  @Bean
  public S3Client s3Client() {
    AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

    return S3Client.builder()
        .region(Region.US_EAST_1)
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .endpointOverride(URI.create(endpoint))
        .build();
  }

  @Bean
  public S3Presigner s3Presigner() {
    AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

    return S3Presigner.builder()
        .region(Region.US_EAST_1)
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .endpointOverride(URI.create(endpoint))
        .build();
  }
}

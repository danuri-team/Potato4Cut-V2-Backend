package com.potato.cut4.presentation.dto.response;

public record PreSignedUrlResponse(
    String uploadUrl,
    String key,
    String fileUrl,
    String contentType,
    long maxFileSize
) {

}

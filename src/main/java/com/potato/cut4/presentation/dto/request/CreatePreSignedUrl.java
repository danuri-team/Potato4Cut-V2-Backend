package com.potato.cut4.presentation.dto.request;

import jakarta.validation.constraints.Min;

public record CreatePreSignedUrl(
    @Min(value = 0, message = "파일 사이즈는 0보다 커야 합니다.")
    long fileSize
) {

}

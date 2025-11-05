package com.potato.cut4.presentation.dto.request;

import com.potato.cut4.persistence.domain.type.ReportType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateReportRequest {

  @NotNull(message = "신고 유형은 필수입니다.")
  private ReportType type;

  @NotNull(message = "신고 대상 ID는 필수입니다.")
  private UUID targetId;

  @NotBlank(message = "신고 사유는 필수입니다.")
  @Size(max = 1000, message = "신고 사유는 1000자 이하여야 합니다.")
  private String reason;
}

package com.example.findex.api;

import com.example.findex.dto.autosyncconfigs.AutoSyncConfigsDto;
import com.example.findex.dto.autosyncconfigs.request.AutoSyncConfigsUpdatedRequest;
import com.example.findex.dto.indexdata.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "자동 연동 설정 API")
public interface AutoSyncConfigApi {

  @Operation(
      summary = "자동 연동 설정 수정",
      description = "자동 연동 설정을 수정합니다."
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "자동 연동 설정 수정 성공",
          content = @Content(
              schema = @Schema(implementation = AutoSyncConfigsDto.class)
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "잘못된 요청 (유효하지 않은 데이터 값 등)",
          content = @Content(
              schema = @Schema(implementation = ErrorResponse.class)
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "수정할 자동 연동 설정을 찾을 수 없음"
      ),
      @ApiResponse(
          responseCode = "500",
          description = "서버 오류",
          content = @Content(
              schema = @Schema(implementation = ErrorResponse.class)
          )
      )
  })
  ResponseEntity<AutoSyncConfigsDto> updateAutoSyncConfigs(
      @Parameter(description = "자동 연동 설정 ID") Long id, AutoSyncConfigsUpdatedRequest request);

}

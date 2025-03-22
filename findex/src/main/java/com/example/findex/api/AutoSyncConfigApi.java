package com.example.findex.api;

import com.example.findex.dto.autosyncconfigs.response.AutoSyncConfigsDto;
import com.example.findex.dto.autosyncconfigs.response.CursorPageResponseAutoSyncConfigDto;
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

  @Operation(
      summary = "자동 연동 설정 목록 조회",
      description = "자동 연동 설정 목록을 조회합니다. 필터링, 정렬, 커서 기반 페이지네이션을 지원합니다."
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "자동 연동 설정 목록 조회 성공",
          content = @Content(
              schema = @Schema(
                  implementation = CursorPageResponseAutoSyncConfigDto.class
              )
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "잘못된 요청 (유효하지 않은 필터 값 등)",
          content = @Content(
              schema = @Schema(implementation = ErrorResponse.class)
          )
      ),
      @ApiResponse(
          responseCode = "500",
          description = "서버 오류",
          content = @Content(
              schema = @Schema(implementation = ErrorResponse.class)
          )
      )
  })
  ResponseEntity<CursorPageResponseAutoSyncConfigDto> findAutoSyncConfigsList(
      @Parameter(description = "지수 정보 ID") Long indexInfoId,
      @Parameter(description = "활성화 여부") Boolean enabled,
      @Parameter(description = "이전 페이지 마지막 요소 ID") Long idAfter,
      @Parameter(description = "커서 (다음 페이지 시작점)") String cursor,
      @Parameter(description = "정렬 필드 (indexInfo.indexName, enabled)") String sortField,
      @Parameter(description = "정렬 방향 (asc, desc)") String sortDirection,
      @Parameter(description = "페이지 크기") int size
  );
}

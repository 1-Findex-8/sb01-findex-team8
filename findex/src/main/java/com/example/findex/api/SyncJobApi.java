package com.example.findex.api;

import com.example.findex.dto.indexdata.response.ErrorResponse;
import com.example.findex.dto.syncjobs.request.IndexDataSyncRequest;
import com.example.findex.dto.syncjobs.response.CursorPageResponseSyncJobDto;
import com.example.findex.dto.syncjobs.response.SyncJobsDto;
import com.example.findex.entity.JobType;
import com.example.findex.entity.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "연동 작업 API")
public interface SyncJobApi {

  @Operation(
      summary = "지수 정보 연동",
      description = "Open API를 통해 지수 정보를 연동합니다."
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "202",
          description = "연동 작업 생성 성정",
          content = @Content(
              array = @ArraySchema(schema = @Schema(implementation = SyncJobsDto.class))
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "잘못된 요청 (유효하지 않은 데이터 값 등)",
          content = @Content(
              schema = @Schema(
                  implementation = ErrorResponse.class
              )
          )
      ),
      @ApiResponse(
          responseCode = "500",
          description = "서버 오류",
          content = @Content(
              schema = @Schema(
                  implementation = ErrorResponse.class
              )
          )
      )
  })
  ResponseEntity<List<SyncJobsDto>> syncIndexInfos(HttpServletRequest request);

  @Operation(
      summary = "지수 데이터 연동",
      description = "Open API를 통해 지수 데이터를 연동합니다."
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "202",
          description = "연동 작업 생성 성정",
          content = @Content(
              array = @ArraySchema(schema = @Schema(implementation = SyncJobsDto.class))
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "잘못된 요청 (유효하지 않은 데이터 값 등)",
          content = @Content(
              schema = @Schema(
                  implementation = ErrorResponse.class
              )
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "지수 정보를 찾을 수 없음",
          content = @Content(
              schema = @Schema(
                  implementation = ErrorResponse.class
              )
          )
      ),
      @ApiResponse(
          responseCode = "500",
          description = "서버 오류",
          content = @Content(
              schema = @Schema(
                  implementation = ErrorResponse.class
              )
          )
      )
  })
  ResponseEntity<List<SyncJobsDto>> syncIndexData(
      IndexDataSyncRequest request, HttpServletRequest httpServletRequest);

  @Operation(
      summary = "연동 작업 목록 조회",
      description = "연동 작업 목록을 조회합니다. 필터링, 정렬, 커서 기반 페이지네이션을 지원합니다."
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "연동 작업 목록 조회 성공",
          content = @Content(
              schema = @Schema(
                  implementation = CursorPageResponseSyncJobDto.class
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
  ResponseEntity<CursorPageResponseSyncJobDto> findSyncJobsList(
      @Parameter(description = "연동 작업 유형 (INDEX_INFO, INDEX_DATA)", schema = @Schema(implementation = JobType.class)) JobType jobType,
      @Parameter(description = "지수 정보 ID") Long indexInfoId,
      @Parameter(description = "대상 날짜 (부터)") LocalDate baseDateFrom,
      @Parameter(description = "대상 날짜 (까지)") LocalDate baseDateTo,
      @Parameter(description = "작업자") String worker,
      @Parameter(description = "작업 날짜 (부터)") LocalDateTime jobTimeFrom,
      @Parameter(description = "작업 날짜 (까지)") LocalDateTime jobTimeTo,
      @Parameter(description = "작업 상태 (SUCCESS, FAILED)", schema = @Schema(implementation = Result.class)) Result status,
      @Parameter(description = "이전 페이지 마지막 요소 ID") Long idAfter,
      @Parameter(description = "커서 (다음 페이지 시작점)") String cursor,
      @Parameter(description = "정렬 필드 (targetDate, jobTime)") String sortField,
      @Parameter(description = "정렬 방향 (asc, desc)") String sortDirection,
      @Parameter(description = "페이지 크기") int size
  );
}

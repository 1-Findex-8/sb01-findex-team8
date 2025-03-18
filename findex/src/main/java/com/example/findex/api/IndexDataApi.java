package com.example.findex.api;

import com.example.findex.dto.indexdata.data.IndexDataDto;
import com.example.findex.dto.indexdata.request.IndexDataCreateRequest;
import com.example.findex.dto.indexdata.response.CursorPageResponseIndexDataDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import com.example.findex.dto.indexdata.response.ErrorResponse;

@Tag(name = "지수 데이터 API")
public interface IndexDataApi {

  //POST /api/index-data
  @Operation(
      summary = "지수 데이터 등록",
      description = "새로운 지수 데이터를 등록합니다."
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "201",
          description = "지수 데이터 생성 성공",
          content = @Content(
              schema = @Schema(
                  implementation = IndexDataDto.class
              )
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
          description = "참조하는 지수 정보를 찾을 수 없음",
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
  ResponseEntity<?> createIndexData(IndexDataCreateRequest indexDataCreateRequest);


  //Get /api/index-data
  @Operation(
      summary = "지수 데이터 목록 조회",
      description = "지수 데이터 목록을 조회합니다. 필터링, 정렬, 커서 기반 페이지네이션을 지원합니다."
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "지수 데이터 목록 조회 성공",
          content = @Content(
              schema = @Schema(implementation = CursorPageResponseIndexDataDto.class)
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
  ResponseEntity<CursorPageResponseIndexDataDto> getIndexDataList(
      @Parameter(description = "지수 정보 ID") Long indexInfoId,
      @Parameter(description = "시작 일자") LocalDate startDate,
      @Parameter(description = "종료 일자") LocalDate endDate,
      @Parameter(description = "이전 페이지 마지막 요소 ID") Long idAfter,
      @Parameter(description = "커서 (다음 페이지 시작점)") String cursor,
      @Parameter(description = "정렬 필드 (baseDate, marketPrice, closingPrice, highPrice, lowPrice, versus, fluctuationRate, tradingQuantity, tradingPrice, marketTotalAmount)") String sortField,
      @Parameter(description = "정렬 방향 (asc, desc)") String sortDirection,
      @Parameter(description = "페이지 크기") int size
  );
}

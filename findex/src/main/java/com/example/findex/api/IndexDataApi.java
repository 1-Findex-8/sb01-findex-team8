package com.example.findex.api;

import com.example.findex.dto.indexdata.data.IndexDataDto;
import com.example.findex.dto.indexdata.request.IndexDataCreateRequest;
import com.example.findex.dto.indexdata.request.IndexDataUpdateRequest;
import com.example.findex.dto.indexdata.response.CursorPageResponseIndexDataDto;
import com.example.findex.dto.indexdata.response.ErrorResponse;
import com.example.findex.dto.indexdata.response.IndexChartDto;
import com.example.findex.dto.indexdata.response.IndexPerformanceDto;
import com.example.findex.dto.indexdata.response.RankedIndexPerformanceDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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
              schema = @Schema(
                  implementation = CursorPageResponseIndexDataDto.class
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

  //PATCH /api/index-data/{id}
  @Operation(
      summary = "지수 데이터 수정",
      description = "기존 지수 데이터를 수정합니다."
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "지수 데이터 수정 성공",
          content = @Content(
              schema = @Schema(implementation = IndexDataDto.class)
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
          description = "수정할 지수 데이터를 찾을 수 없음",
  //DELETE /api/index-data/{id}
  @Operation(
      summary = "지수 데이터 삭제",
      description = "지수 데이터를 삭제합니다."
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "204",
          description = "지수 데이터 삭제 성공"
      ),
      @ApiResponse(
          responseCode = "404",
          description = "삭제할 지수 데이터를 찾을 수 없음",
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
  ResponseEntity<IndexDataDto> updateIndexData(
      @Parameter(description = "지수 데이터 ID") Long id,@RequestBody IndexDataUpdateRequest indexDataUpdateRequest);

  ResponseEntity<Void> deleteIndexData(@Parameter(description = "지수 데이터 ID",required = true) Long indexInfoId);
  @Operation(summary = "관심 지수 성과 조회", description = "사용자의 관심 지수 목록을 기준으로 성과를 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "관심 지수 성과 조회 성공", content = @Content(schema = @Schema(implementation = IndexPerformanceDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청")})
  ResponseEntity<List<IndexPerformanceDto>> getIndexFavoritePerformanceRank(
      @Parameter(description = "기간 타입 (DAILY, WEEKLY, MONTHLY)", required = true, example = "DAILY") @RequestParam String periodType
  );

  @Operation(summary = "지수 성과 랭킹 조회", description = "특정 지수의 성과를 랭킹별로 조회합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "지수 성과 랭킹 조회 성공", content = @Content(schema = @Schema(implementation = RankedIndexPerformanceDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청")})
  ResponseEntity<List<RankedIndexPerformanceDto>> getIndexPerformanceRank(
      @Parameter(description = "기간 타입 (DAILY, WEEKLY, MONTHLY)", required = true, example = "DAILY") @RequestParam String periodType,

      @Parameter(description = "지수 정보 ID", required = true, example = "1") @RequestParam int indexInfoId,

      @Parameter(description = "조회할 최대 개수", required = true, example = "10") @RequestParam int limit
  );

  @Operation(summary = "지수 데이터 CSV 다운로드", description = "지수 데이터를 CSV 파일로 다운로드합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "CSV 파일 다운로드 성공", content = @Content(mediaType = "text/csv")),
      @ApiResponse(responseCode = "400", description = "잘못된 요청")})
  ResponseEntity<InputStreamResource> getIndexDataCsv(
      @Parameter(description = "지수 정보 ID", example = "1") @RequestParam(value = "indexInfoId", required = false) Long indexInfoId,

      @Parameter(description = "시작 날짜 (YYYY-MM-DD)", example = "2024-01-01") @RequestParam(value = "startDate", required = false) LocalDate startDate,

      @Parameter(description = "종료 날짜 (YYYY-MM-DD)", example = "2024-01-31") @RequestParam(value = "endDate", required = false) LocalDate endDate,

      @Parameter(description = "정렬 필드", example = "baseDate", required = false) @RequestParam(value = "sortField", required = false, defaultValue = "baseDate") String sortField,

      @Parameter(description = "정렬 방향 (asc 또는 desc)", example = "desc", required = false) @RequestParam(value = "sortDirection", required = false, defaultValue = "desc") String sortDirection
  );

    @Operation(
            summary = "지수 차트 데이터 조회",
            description = "주어진 기간(periodType)과 지수 ID(indexInfoId)에 대한 차트 데이터를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공적으로 차트 데이터를 반환함",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IndexChartDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (예: 유효하지 않은 periodType)"),
            @ApiResponse(responseCode = "404", description = "해당 indexInfoId에 대한 데이터 없음")
    })
    ResponseEntity<IndexChartDto> getIndexChart(
            @PathVariable @Parameter(description = "지수 정보 ID", example = "1") int indexInfoId,
            @RequestParam @Parameter(description = "조회 기간 타입 (예: daily, weekly, monthly)", example = "daily") String periodType
    );
}

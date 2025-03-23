package com.example.findex.api;

import com.example.findex.dto.indexinfo.IndexInfoDto;
import com.example.findex.dto.indexinfo.IndexInfoSummaryDto;
import com.example.findex.dto.indexinfo.SortDirectionType;
import com.example.findex.dto.indexinfo.request.UpdateIndexInfoRequest;
import com.example.findex.dto.indexinfo.response.CursorPageResponseIndexInfoDto;
import com.example.findex.dto.indexdata.response.ErrorResponse;
import com.example.findex.dto.indexinfo.request.CreateIndexInfoRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "지수 정보 API")
public interface IndexInfoApi {

  //POST /api/index-infos
  @Operation(
      summary = "지수 정보 등록",
      description = "새로운 지수 정보를 등록합니다."
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "201",
          description = "지수 정보 생성 성공",
          content = @Content(
              schema = @Schema(
                  implementation = IndexInfoDto.class
              )
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "잘못된 요청 (필수 필드 누락 등)",
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
  ResponseEntity<IndexInfoDto> createIndexInfo(CreateIndexInfoRequest createIndexInfoRequest);

  //Get /api/index-infos
  @Operation(
      summary = "지수 정보 목록 조회",
      description = "지수 정보 목록을 조회합니다. 필터링, 정렬, 커서 기반 페이지네이션을 지원합니다."
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "지수 정보 목록 조회 성공",
          content = @Content(
              schema = @Schema(
                  implementation = CursorPageResponseIndexInfoDto.class
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
  ResponseEntity<CursorPageResponseIndexInfoDto> findIndexInfoList(
      @Parameter(description = "지수 분류명") String indexClassification,
      @Parameter(description = "지수명") String indexName,
      @Parameter(description = "즐겨찾기 여부") Boolean favorite,
      @Parameter(description = "이전 페이지 마지막 요소 ID") Long idAfter,
      @Parameter(description = "커서 (다음 페이지 시작점)") String cursor,
      @Parameter(description = "정렬 필드 (indexClassification, indexName, employedItemsCount)") String sortField,
      @Parameter(description = "정렬 방향 (asc, desc)") SortDirectionType sortDirection,
      @Parameter(description = "페이지 크기") Integer size
  );

  //Get /api/index-infos/{id}
  @Operation(
      summary = "지수 정보 조회",
      description = "ID로 지수 정보를 조회합니다."
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "지수 정보 조회 성공",
          content = @Content(
              schema = @Schema(
                  implementation = IndexInfoDto.class
              )
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "조회할 지수 정보를 찾을 수 없음",
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
  ResponseEntity<IndexInfoDto> findIndexInfo(@Parameter(description = "지수 정보 ID",required = true) Long indexInfoId);


  //PATCH /api/index-infos/{id}
  @Operation(
      summary = "지수 정보 수정",
      description = "기존 지수 정보를 수정합니다."
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "지수 정보 수정 성공",
          content = @Content(
              schema = @Schema(implementation = IndexInfoDto.class)
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "잘못된 요청 (유효하지 않은 정보 값 등)",
          content = @Content(
              schema = @Schema(implementation = ErrorResponse.class)
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "수정할 지수 정보를 찾을 수 없음",
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
  ResponseEntity<IndexInfoDto> updateIndexInfo(
      @Parameter(description = "지수 정보 ID") Long id, @RequestBody UpdateIndexInfoRequest updateIndexInfoRequest);

  //DELETE /api/index-infos/{id}
  @Operation(
      summary = "지수 정보 삭제",
      description = "지수 정보를 삭제합니다. 관련된 지수 데이터도 함께 삭제됩니다."
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "204",
          description = "지수 정보 삭제 성공"
      ),
      @ApiResponse(
          responseCode = "404",
          description = "삭제할 지수 정보를 찾을 수 없음",
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
  ResponseEntity<Void> deleteIndexInfo(@Parameter(description = "지수 정보 ID",required = true) Long indexInfoId);

  //Get /api/index-infos/summaries
  @Operation(
      summary = "지수 정보 요약 목록 조회",
      description = "지수 ID, 분류, 이름만 포함한 전체 지수 목록을 조회합니다."
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "지수 정보 요약 목록 조회 성공",
          content = @Content(
              schema = @Schema(
                  implementation = IndexInfoSummaryDto.class
              )
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
  ResponseEntity<List<IndexInfoSummaryDto>> findIndexInfoSummaryList();

}

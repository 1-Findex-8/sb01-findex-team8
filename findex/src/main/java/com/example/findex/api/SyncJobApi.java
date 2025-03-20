package com.example.findex.api;

import com.example.findex.dto.indexdata.response.ErrorResponse;
import com.example.findex.dto.syncjobs.response.SyncJobsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
          description = "연동 작업 생성 성공",
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
  public ResponseEntity<List<SyncJobsDto>> syncIndexInfos(HttpServletRequest request);

}

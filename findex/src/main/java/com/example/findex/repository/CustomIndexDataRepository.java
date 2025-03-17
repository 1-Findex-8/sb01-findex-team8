package com.example.findex.repository;

import com.example.findex.dto.indexdata.data.IndexDataDto;
import com.example.findex.dto.indexdata.response.CursorPageResponseIndexDataDto;
import java.time.LocalDate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CustomIndexDataRepository {
  // 마지막 게시글 id, 검색 및 필터링 조건, 페이지에 불러올 게시글의 수
  CursorPageResponseIndexDataDto getIndexDataList(long indexInfold, LocalDate startDate, LocalDate endDate, long idAfter, String cursor,String sortField, String sortDirection, int size);

  }

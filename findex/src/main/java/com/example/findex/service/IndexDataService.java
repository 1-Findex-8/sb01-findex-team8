package com.example.findex.service;

import com.example.findex.dto.data.IndexDataDto;
import com.example.findex.dto.request.IndexDataCreateRequest;

public interface IndexDataService {

  IndexDataDto create(IndexDataCreateRequest indexDataCreateRequest);
  //List<CursorPageResponseIndexDataDto> findAllByIndexInfold(long indexInfold);
}

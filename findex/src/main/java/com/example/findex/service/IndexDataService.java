package com.example.findex.service;

import com.example.findex.dto.indexdata.data.IndexDataDto;
import com.example.findex.dto.indexdata.request.IndexDataCreateRequest;

public interface IndexDataService {

  IndexDataDto create(IndexDataCreateRequest indexDataCreateRequest);
  //List<CursorPageResponseIndexDataDto> findAllByIndexInfold(long indexInfold);
}

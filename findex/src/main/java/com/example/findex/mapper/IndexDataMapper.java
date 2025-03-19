package com.example.findex.mapper;

import com.example.findex.dto.indexdata.data.IndexDataDto;
import com.example.findex.entity.IndexData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IndexDataMapper {

  @Mapping(source = "indexInfo.id",target = "indexInfoId")
  IndexDataDto toDto(IndexData indexData);


}

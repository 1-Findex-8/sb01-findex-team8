package com.example.findex.mapper;

import com.example.findex.dto.indexdata.data.IndexDataDto;
import com.example.findex.entity.IndexData;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IndexDataMapper {

  IndexDataDto toDto(IndexData indexData);
}

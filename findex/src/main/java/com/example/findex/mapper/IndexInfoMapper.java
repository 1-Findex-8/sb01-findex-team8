package com.example.findex.mapper;

import com.example.findex.dto.IndexInfo.IndexInfoDto;
import com.example.findex.entity.IndexInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IndexInfoMapper {

  IndexInfoDto toDto(IndexInfo indexInfo);
}

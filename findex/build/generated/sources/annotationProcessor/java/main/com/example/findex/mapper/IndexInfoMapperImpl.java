package com.example.findex.mapper;

import com.example.findex.dto.indexinfo.IndexInfoDto;
import com.example.findex.entity.IndexInfo;
import com.example.findex.entity.SourceType;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-19T13:45:48+0900",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.10.2.jar, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class IndexInfoMapperImpl implements IndexInfoMapper {

    @Override
    public IndexInfoDto toDto(IndexInfo indexInfo) {
        if ( indexInfo == null ) {
            return null;
        }

        Long id = null;
        String indexClassification = null;
        String indexName = null;
        int employeeItemsCount = 0;
        LocalDate basePointInTime = null;
        BigDecimal baseIndex = null;
        SourceType sourceType = null;
        boolean favorite = false;

        id = indexInfo.getId();
        indexClassification = indexInfo.getIndexClassification();
        indexName = indexInfo.getIndexName();
        employeeItemsCount = indexInfo.getEmployeeItemsCount();
        basePointInTime = indexInfo.getBasePointInTime();
        baseIndex = indexInfo.getBaseIndex();
        sourceType = indexInfo.getSourceType();
        favorite = indexInfo.isFavorite();

        IndexInfoDto indexInfoDto = new IndexInfoDto( id, indexClassification, indexName, employeeItemsCount, basePointInTime, baseIndex, sourceType, favorite );

        return indexInfoDto;
    }
}

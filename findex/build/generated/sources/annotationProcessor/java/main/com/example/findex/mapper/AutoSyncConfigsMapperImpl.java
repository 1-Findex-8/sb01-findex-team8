package com.example.findex.mapper;

import com.example.findex.dto.autosyncconfigs.AutoSyncConfigsDto;
import com.example.findex.entity.AutoSyncConfigs;
import com.example.findex.entity.IndexInfo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-19T13:45:47+0900",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.10.2.jar, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class AutoSyncConfigsMapperImpl implements AutoSyncConfigsMapper {

    @Override
    public AutoSyncConfigsDto toAutoSyncConfigsDto(AutoSyncConfigs autoSyncConfigs) {
        if ( autoSyncConfigs == null ) {
            return null;
        }

        Long indexInfoId = null;
        String indexClassification = null;
        String indexName = null;
        boolean enabled = false;
        Long id = null;

        indexInfoId = autoSyncConfigsIndexInfoId( autoSyncConfigs );
        indexClassification = autoSyncConfigsIndexInfoIndexClassification( autoSyncConfigs );
        indexName = autoSyncConfigsIndexInfoIndexName( autoSyncConfigs );
        enabled = autoSyncConfigs.isActive();
        id = autoSyncConfigs.getId();

        AutoSyncConfigsDto autoSyncConfigsDto = new AutoSyncConfigsDto( id, indexInfoId, indexClassification, indexName, enabled );

        return autoSyncConfigsDto;
    }

    @Override
    public List<AutoSyncConfigsDto> toAutoSyncConfigsDtoList(List<AutoSyncConfigs> autoSyncConfigsList) {
        if ( autoSyncConfigsList == null ) {
            return null;
        }

        List<AutoSyncConfigsDto> list = new ArrayList<AutoSyncConfigsDto>( autoSyncConfigsList.size() );
        for ( AutoSyncConfigs autoSyncConfigs : autoSyncConfigsList ) {
            list.add( toAutoSyncConfigsDto( autoSyncConfigs ) );
        }

        return list;
    }

    private Long autoSyncConfigsIndexInfoId(AutoSyncConfigs autoSyncConfigs) {
        IndexInfo indexInfo = autoSyncConfigs.getIndexInfo();
        if ( indexInfo == null ) {
            return null;
        }
        return indexInfo.getId();
    }

    private String autoSyncConfigsIndexInfoIndexClassification(AutoSyncConfigs autoSyncConfigs) {
        IndexInfo indexInfo = autoSyncConfigs.getIndexInfo();
        if ( indexInfo == null ) {
            return null;
        }
        return indexInfo.getIndexClassification();
    }

    private String autoSyncConfigsIndexInfoIndexName(AutoSyncConfigs autoSyncConfigs) {
        IndexInfo indexInfo = autoSyncConfigs.getIndexInfo();
        if ( indexInfo == null ) {
            return null;
        }
        return indexInfo.getIndexName();
    }
}

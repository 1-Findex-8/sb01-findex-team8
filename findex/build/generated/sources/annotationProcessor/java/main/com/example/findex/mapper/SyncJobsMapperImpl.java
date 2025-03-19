package com.example.findex.mapper;

import com.example.findex.dto.syncjobs.response.SyncJobsDto;
import com.example.findex.entity.IndexInfo;
import com.example.findex.entity.JobType;
import com.example.findex.entity.Result;
import com.example.findex.entity.SyncJobs;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-19T13:45:48+0900",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.10.2.jar, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class SyncJobsMapperImpl implements SyncJobsMapper {

    @Override
    public SyncJobsDto toSyncJobsDto(SyncJobs syncJobs) {
        if ( syncJobs == null ) {
            return null;
        }

        Long indexInfoId = null;
        Long id = null;
        JobType jobType = null;
        LocalDate targetDate = null;
        String worker = null;
        LocalDateTime jobTime = null;
        Result result = null;

        indexInfoId = syncJobsIndexInfoId( syncJobs );
        id = syncJobs.getId();
        jobType = syncJobs.getJobType();
        targetDate = syncJobs.getTargetDate();
        worker = syncJobs.getWorker();
        jobTime = syncJobs.getJobTime();
        result = syncJobs.getResult();

        SyncJobsDto syncJobsDto = new SyncJobsDto( id, jobType, indexInfoId, targetDate, worker, jobTime, result );

        return syncJobsDto;
    }

    @Override
    public List<SyncJobsDto> toSyncJobsDtoList(List<SyncJobs> syncJobsList) {
        if ( syncJobsList == null ) {
            return null;
        }

        List<SyncJobsDto> list = new ArrayList<SyncJobsDto>( syncJobsList.size() );
        for ( SyncJobs syncJobs : syncJobsList ) {
            list.add( toSyncJobsDto( syncJobs ) );
        }

        return list;
    }

    private Long syncJobsIndexInfoId(SyncJobs syncJobs) {
        IndexInfo indexInfo = syncJobs.getIndexInfo();
        if ( indexInfo == null ) {
            return null;
        }
        return indexInfo.getId();
    }
}

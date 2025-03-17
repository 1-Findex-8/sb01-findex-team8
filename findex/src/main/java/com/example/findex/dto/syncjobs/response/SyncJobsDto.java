package com.example.findex.dto.syncjobs.response;

import com.example.findex.entity.JobType;
import com.example.findex.entity.Result;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record SyncJobsDto(
    Long id,
    JobType jobType,
    Long indexInfoId,
    LocalDate targetDate,
    String worker,
    LocalDateTime jobTime,
    Result result
) {

}

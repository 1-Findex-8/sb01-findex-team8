package com.example.findex.repository.syncjob;

import com.example.findex.entity.JobType;
import com.example.findex.entity.Result;
import com.example.findex.entity.SyncJobs;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SyncJobsCustomRepository {

  Page<SyncJobs> findSyncJobsList(
      JobType jobType, Long indexInfoId, LocalDate baseDateFrom, LocalDate baseDateTo,
      String worker, LocalDateTime jobTimeFrom, LocalDateTime jobTimeTo, Result status,
      Long idAfter, Long cursor, Pageable pageable
  );
}

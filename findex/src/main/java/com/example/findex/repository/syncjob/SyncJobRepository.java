package com.example.findex.repository.syncjob;

import com.example.findex.entity.IndexInfo;
import com.example.findex.entity.JobType;
import com.example.findex.entity.SyncJobs;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SyncJobRepository extends JpaRepository<SyncJobs, Long>, SyncJobsCustomRepository{

  Optional<SyncJobs> findTop1ByIndexInfoAndJobTypeOrderByJobTimeDesc(IndexInfo indexInfo, JobType jobType);
}

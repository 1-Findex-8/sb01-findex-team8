package com.example.findex.repository.syncjob;

import com.example.findex.entity.SyncJobs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SyncJobRepository extends JpaRepository<SyncJobs, Long>, SyncJobsCustomRepository{

}

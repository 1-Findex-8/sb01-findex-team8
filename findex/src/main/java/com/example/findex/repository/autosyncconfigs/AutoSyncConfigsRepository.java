package com.example.findex.repository.autosyncconfigs;

import com.example.findex.entity.AutoSyncConfigs;
import com.example.findex.entity.IndexInfo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutoSyncConfigsRepository extends JpaRepository<AutoSyncConfigs, Long>, AutoSyncConfigsCustomRepository{

  Optional<AutoSyncConfigs> findByIndexInfo(IndexInfo indexInfo);
}

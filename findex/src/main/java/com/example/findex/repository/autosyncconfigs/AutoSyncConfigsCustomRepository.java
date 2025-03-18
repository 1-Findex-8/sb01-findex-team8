package com.example.findex.repository.autosyncconfigs;

import com.example.findex.entity.AutoSyncConfigs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AutoSyncConfigsCustomRepository {

  Page<AutoSyncConfigs> findAutoSyncConfigsList(
      Long indexInfoId, Boolean enabled, Long idAfter, Pageable pageable);

}

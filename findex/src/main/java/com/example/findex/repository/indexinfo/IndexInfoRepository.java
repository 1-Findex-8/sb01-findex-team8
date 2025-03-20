package com.example.findex.repository.indexinfo;

import com.example.findex.entity.IndexInfo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndexInfoRepository extends JpaRepository<IndexInfo, Long>, IndexInfoRepositoryCustom {

  IndexInfo save(IndexInfo indexInfo);

  Optional<IndexInfo> findById(Long indexInfoId);

  List<IndexInfo> findAll();

  List<IndexInfo> findByFavorite(Boolean favorite);

  Optional<IndexInfo> findByIndexClassificationAndIndexName(String classification, String name);

  List<IndexInfo> findByIndexClassification(String classification);  // 임시 추가, 수정 필요

  Boolean existsByIndexClassificationAndIndexName(String classification, String name);

  void deleteById(Long indexInfoId);
}

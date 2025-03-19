package com.example.findex.repository;

import com.example.findex.entity.IndexInfo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexInfoRepository extends JpaRepository<IndexInfo, Long> {
  
  IndexInfo save(IndexInfo indexInfo);

  Optional<IndexInfo> findById(Long indexInfoId);

  List<IndexInfo> findAll();

  List<IndexInfo> findByIndexClassificationContaining(String indexClassification);

  List<IndexInfo> findByIndexNameContaining(String indexName);

  List<IndexInfo> findByFavorite(boolean favorite);

  boolean existsByIndexClassificationAndIndexName(String classification, String name);

  void deleteById(Long indexInfoId);

  Optional<IndexInfo> findByIndexClassificationAndIndexName(String classification, String name);

  List<IndexInfo> findByIndexClassification(String classification);  // 임시 추가, 수정 필요

}

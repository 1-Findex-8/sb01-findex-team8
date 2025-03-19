package com.example.findex.repository;

import com.example.findex.dto.indexinfo.IndexInfoDto;
import com.example.findex.entity.IndexInfo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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


  @Query("SELECT i FROM IndexInfo i WHERE " +
      "(i.indexClassification LIKE %:indexClassification% OR :indexClassification IS NULL) AND " +
      "(i.indexName LIKE %:indexName% OR :indexName IS NULL) AND " +
      "(i.favorite = :favorite) AND " +
      "(i.id > :idAfter OR :idAfter IS NULL) ORDER BY " +
      "CASE WHEN :sortField = 'indexClassification' THEN i.indexClassification END ASC, " +
      "CASE WHEN :sortField = 'indexName' THEN i.indexName END ASC, " +
      "CASE WHEN :sortField = 'employedItemsCount' THEN i.employeeItemsCount END ASC")
  List<IndexInfoDto> findByFilters(@Param("indexClassification") String indexClassification,
      @Param("indexName") String indexName,
      @Param("favorite") boolean favorite,
      @Param("idAfter") Long idAfter,
      Pageable pageable);

}

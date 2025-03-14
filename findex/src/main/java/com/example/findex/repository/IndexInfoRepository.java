package com.example.findex.repository;

import com.example.findex.entity.IndexInfo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexInfoRepository extends JpaRepository<IndexInfo, Long> {

  Optional<IndexInfo> findById(Long id);
}

package com.example.findex.entity;

import com.example.findex.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SyncJobs extends BaseEntity {

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private JobType jobType;

  private LocalDate targetDate;

  @Column(nullable = false)
  private String worker;

  @Column(nullable = false)
  private LocalDateTime jobTime;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private Result result;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "index_info_id", nullable = false)
  private IndexInfo indexInfo;
}

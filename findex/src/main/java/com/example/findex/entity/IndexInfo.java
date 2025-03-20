package com.example.findex.entity;

import com.example.findex.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "index_info")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IndexInfo extends BaseEntity {

  @Column(nullable = false)
  private String indexClassification;

  @Column(nullable = false)
  private String indexName;

  @Column(nullable = false)
  private Integer employedItemsCount;

  @Column(nullable = false)
  private LocalDate basePointInTime;

  @Column(nullable = false)
  private BigDecimal baseIndex;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private SourceType sourceType;

  @Column(nullable = false)
  private Boolean favorite = false;

  public void updateEmployedItemsCount(Integer employedItemsCount) {
    this.employedItemsCount = employedItemsCount;
  }

  public void updateBasePointInTime(LocalDate basePointInTime) {
    this.basePointInTime = basePointInTime;
  }

  public void updateBaseIndex(BigDecimal baseIndex) {
    this.baseIndex = baseIndex;
  }

  public void updateFavorite(Boolean favorite) { this.favorite = favorite; }
}

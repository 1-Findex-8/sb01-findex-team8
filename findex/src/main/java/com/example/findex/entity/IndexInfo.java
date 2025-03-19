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
import lombok.Setter;

@Entity
@Table(name = "index_info")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IndexInfo extends BaseEntity {

  @Column(nullable = false)
  private String indexClassification;

  @Column(nullable = false)
  private String indexName;

  @Column(nullable = false)
  private int employeeItemsCount;

  @Column(nullable = false)
  private LocalDate basePointInTime;

  @Column(nullable = false)
  private BigDecimal baseIndex;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private SourceType sourceType;

  @Column(nullable = false)
  private boolean favorite = false;

  public void updateEmployeeItemsCount(int employeeItemsCount) {
    this.employeeItemsCount = employeeItemsCount;
  }

  public void updateBasePointInTime(LocalDate basePointInTime) {
    this.basePointInTime = basePointInTime;
  }

  public void updateBaseIndex(BigDecimal baseIndex) {
    this.baseIndex = baseIndex;
  }
}

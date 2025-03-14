package com.example.findex.entity;

import com.example.findex.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IndexData extends BaseEntity {

  @Column(nullable = false)
  private LocalDate baseDate;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private SourceType sourceType;

  @Column(nullable = false)
  private BigDecimal marketPrice;

  @Column(nullable = false)
  private BigDecimal closingPrice;

  @Column(nullable = false)
  private BigDecimal highPrice;

  @Column(nullable = false)
  private BigDecimal lowPrice;

  @Column(nullable = false)
  private BigDecimal variation;

  @Column(nullable = false)
  private BigDecimal fluctuationRate;

  @Column(nullable = false)
  private Long tradingQuantity;

  @Column(nullable = false)
  private BigDecimal tradingPrice;

  @Column(nullable = false)
  private BigDecimal marketCapitalization;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "index_info_id", nullable = false)
  private IndexInfo indexInfo;
}

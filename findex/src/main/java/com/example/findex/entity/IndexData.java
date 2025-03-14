package com.example.findex.entity;

import com.example.findex.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="index_data")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IndexData extends BaseEntity {
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "index_info_id", nullable = false)
  private IndexInfo indexInfo;

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
  private BigDecimal variation; //dto에서 versus로 사용

  @Column(nullable = false)
  private BigDecimal fluctuationRate;

  @Column(nullable = false)
  private Long tradingQuantity;

  @Column(nullable = false)
  private Long tradingPrice;

  @Column(nullable = false)
  private Long marketCapitalization; //dto 에서 marketTotalAmount

  public IndexData(IndexInfo indexInfo, LocalDate baseDate, SourceType sourceType,
      BigDecimal marketPrice, BigDecimal closingPrice, BigDecimal highPrice, BigDecimal lowPrice,
      BigDecimal variation, BigDecimal fluctuationRate, Long tradingQuantity,
      Long tradingPrice,
      Long marketCapitalization) {
    this.indexInfo = indexInfo;
    this.baseDate = baseDate;
    this.sourceType = sourceType;
    this.marketPrice = marketPrice;
    this.closingPrice = closingPrice;
    this.highPrice = highPrice;
    this.lowPrice = lowPrice;
    this.variation = variation;
    this.fluctuationRate = fluctuationRate;
    this.tradingQuantity = tradingQuantity;
    this.tradingPrice = tradingPrice;
    this.marketCapitalization = marketCapitalization;
  }
}


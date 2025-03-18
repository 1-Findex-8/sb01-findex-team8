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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="index_data")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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
  private BigDecimal versus; //dto에서 versus로 사용

  @Column(nullable = false)
  private BigDecimal fluctuationRate;

  @Column(nullable = false)
  private Long tradingQuantity;

  @Column(nullable = false)
  private Long tradingPrice;

  @Column(nullable = false)
  private Long marketTotalAmount; //dto 에서 marketTotalAmount

  public void updateMarketPrice(BigDecimal marketPrice) {
    this.marketPrice = marketPrice;
  }

  public void updateClosingPrice(BigDecimal closingPrice) {
    this.closingPrice = closingPrice;
  }

  public void updateHighPrice(BigDecimal highPrice) {
    this.highPrice = highPrice;
  }

  public void updateLowPrice(BigDecimal lowPrice) {
    this.lowPrice = lowPrice;
  }

  public void updateVariation(BigDecimal variation) {
    this.variation = variation;
  }

  public void updateFluctuationRate(BigDecimal fluctuationRate) {
    this.fluctuationRate = fluctuationRate;
  }

  public void updateTradingQuantity(Long tradingQuantity) {
    this.tradingQuantity = tradingQuantity;
  }
}


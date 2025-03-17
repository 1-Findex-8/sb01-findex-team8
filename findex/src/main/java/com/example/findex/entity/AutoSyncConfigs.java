package com.example.findex.entity;

import com.example.findex.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AutoSyncConfigs extends BaseEntity {

  @Column(nullable = false)
  private boolean active = false;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "index_info_id", nullable = false)
  private IndexInfo indexInfo;

  public void updateActive(boolean active) {
    this.active = active;
  }
}

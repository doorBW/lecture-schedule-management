package com.zerobase.schedulemanagement.domain.entity;

import com.zerobase.schedulemanagement.entry.dto.schdule.UpdateScheduleParam;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "schedule")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Schedule {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private Long startAt;

  @Column(nullable = false)
  private Long endAt;

  @Column(nullable = false)
  private Boolean isDone;

  @CreatedDate
  @Column(updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;

  @Column(updatable = false)
  private Long createdBy;

  private Long updatedBy;

  public void apply(UpdateScheduleParam param){
    this.title = param.getTitle();
    this.description = param.getDescription();
    this.startAt = param.getStartAt();
    this.endAt = param.getEndAt();
    this.updatedBy = param.getMemberId();
  }
}

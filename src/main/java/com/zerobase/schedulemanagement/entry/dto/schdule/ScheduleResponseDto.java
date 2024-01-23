package com.zerobase.schedulemanagement.entry.dto.schdule;

import com.zerobase.schedulemanagement.domain.entity.Schedule;
import java.time.ZoneId;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScheduleResponseDto {

  private Long id;
  private String title;
  private String description;
  private Long startAt;
  private Long endAt;
  private List<Long> participationIds;
  private Boolean isDone;
  private Long createdAt;
  private Long updatedAt;
  private Long createdBy;
  private Long updatedBy;

  public static ScheduleResponseDto from(Schedule schedule, List<Long> participationIds) {
    return ScheduleResponseDto.builder()
                              .id(schedule.getId())
                              .title(schedule.getTitle())
                              .description(schedule.getDescription())
                              .startAt(schedule.getStartAt())
                              .endAt(schedule.getEndAt())
                              .participationIds(participationIds)
                              .isDone(schedule.getIsDone())
                              .createdAt(
                                  schedule.getCreatedAt().atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli())
                              .updatedAt(
                                  schedule.getUpdatedAt().atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli())
                              .createdBy(schedule.getCreatedBy())
                              .updatedBy(schedule.getUpdatedBy())
                              .build();
  }
}

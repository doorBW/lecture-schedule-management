package com.zerobase.schedulemanagement.entry.dto;

import com.zerobase.schedulemanagement.domain.entity.Schedule;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimplifiedScheduleResponseDto {

  private Long id;
  private String title;
  private Long startAt;
  private Long endAt;
  private Boolean isDone;

  public static SimplifiedScheduleResponseDto from(Schedule schedule) {
    return SimplifiedScheduleResponseDto.builder()
                                        .id(schedule.getId())
                                        .title(schedule.getTitle())
                                        .startAt(schedule.getStartAt())
                                        .endAt(schedule.getEndAt())
                                        .isDone(schedule.getIsDone())
                                        .build();
  }
}

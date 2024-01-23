package com.zerobase.schedulemanagement.entry.dto.schdule;

import com.zerobase.schedulemanagement.domain.entity.Schedule;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateScheduleParam {

  private Long memberId;
  private String title;
  private String description;
  private Long startAt;
  private Long endAt;
  private Boolean isDone;
  private List<Long> participationIds;

  public Schedule toEntity() {
    return Schedule.builder()
                   .title(this.title)
                   .description(this.description)
                   .startAt(this.startAt)
                   .endAt(this.endAt)
                   .isDone(false)
                   .createdBy(this.memberId)
                   .updatedBy(this.memberId)
                   .build();
  }
}

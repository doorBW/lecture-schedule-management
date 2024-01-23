package com.zerobase.schedulemanagement.entry.dto.schdule;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateScheduleRequestDto {

  private String title;
  private String description;
  private Long startAt;
  private Long endAt;
  private List<Long> participationIds;

  public UpdateScheduleParam toParam(Long scheduleId, Long memberId) {
    return UpdateScheduleParam.builder()
                              .scheduleId(scheduleId)
                              .memberId(memberId)
                              .title(this.title)
                              .description(this.description)
                              .startAt(this.startAt)
                              .endAt(endAt)
                              .participationIds(this.participationIds)
                              .build();
  }
}

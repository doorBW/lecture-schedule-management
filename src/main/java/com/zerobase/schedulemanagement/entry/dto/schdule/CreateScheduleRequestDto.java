package com.zerobase.schedulemanagement.entry.dto.schdule;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateScheduleRequestDto {

  private String title;
  private String description;
  private Long startAt;
  private Long endAt;
  private List<Long> participationIds;

  public CreateScheduleParam toParam(Long memberId) {
    return CreateScheduleParam.builder()
                              .memberId(memberId)
                              .title(this.title)
                              .description(this.description)
                              .startAt(this.startAt)
                              .endAt(this.endAt)
                              .participationIds(this.participationIds)
                              .build();
  }
}

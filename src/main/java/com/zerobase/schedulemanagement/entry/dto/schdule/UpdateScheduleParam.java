package com.zerobase.schedulemanagement.entry.dto.schdule;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateScheduleParam {

  private Long scheduleId;
  private Long memberId;
  private String title;
  private String description;
  private Long startAt;
  private Long endAt;
  private Boolean isDone;
  private List<Long> participationIds;
}

package com.zerobase.schedulemanagement.entry.controller;

import com.zerobase.schedulemanagement.domain.service.ScheduleService;
import com.zerobase.schedulemanagement.entry.dto.ResponseDto;
import com.zerobase.schedulemanagement.entry.dto.ScheduleResponseDto;
import com.zerobase.schedulemanagement.entry.dto.SimplifiedScheduleResponseDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/schedules")
@RequiredArgsConstructor
public class ScheduleController {

  private final ScheduleService scheduleService;

  @GetMapping
  public ResponseDto<List<SimplifiedScheduleResponseDto>> getSchedules(
      @RequestHeader(name = "MEMBER-ID") Long memberId) {
    return ResponseDto.of(scheduleService.getSchedules(memberId).stream()
                                         .map(SimplifiedScheduleResponseDto::from)
                                         .collect(Collectors.toList()));
  }

  @GetMapping("/{id}")
  public ResponseDto<ScheduleResponseDto> getSchedule(
      @RequestHeader(name = "MEMBER-ID") Long memberId,
      @PathVariable(name = "id") Long scheduleId) {
    return ResponseDto.of(scheduleService.getSchedule(scheduleId, memberId));
  }
}

package com.zerobase.schedulemanagement.domain.service;

import com.zerobase.schedulemanagement.domain.entity.MemberSchedule;
import com.zerobase.schedulemanagement.domain.entity.Schedule;
import com.zerobase.schedulemanagement.entry.dto.ResponseCode;
import com.zerobase.schedulemanagement.entry.dto.ScheduleResponseDto;
import com.zerobase.schedulemanagement.infra.exception.ScheduleManagementException;
import com.zerobase.schedulemanagement.infra.persistence.MemberScheduleRepository;
import com.zerobase.schedulemanagement.infra.persistence.ScheduleRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {

  private final ScheduleRepository scheduleRepository;
  private final MemberScheduleRepository memberScheduleRepository;

  public List<Schedule> getSchedules(Long memberId) {
    List<MemberSchedule> memberSchedules = memberScheduleRepository.findAllByMemberId(memberId);

    return scheduleRepository.findAllByIdIn(memberSchedules.stream()
                                                           .map(memberSchedule -> memberSchedule.getSchedule().getId())
                                                           .collect(Collectors.toList()));
  }

  public ScheduleResponseDto getSchedule(Long scheduleId, Long memberId) {
    Schedule schedule = scheduleRepository.findById(scheduleId)
                                          .orElseThrow(() -> new ScheduleManagementException(ResponseCode.NO_SCHEDULE));

    List<MemberSchedule> memberSchedules = memberScheduleRepository.findAllByScheduleId(schedule.getId());
    List<Long> participationIds = memberSchedules.stream().map(memberSchedule -> memberSchedule.getMember().getId())
                                                 .toList();

    if (!participationIds.contains(memberId)) {
      throw new ScheduleManagementException(ResponseCode.NOT_PARTICIPATED_SCHEDULE);
    }

    return ScheduleResponseDto.from(schedule, participationIds);
  }
}

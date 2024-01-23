package com.zerobase.schedulemanagement.domain.service;

import com.zerobase.schedulemanagement.domain.entity.Member;
import com.zerobase.schedulemanagement.domain.entity.MemberSchedule;
import com.zerobase.schedulemanagement.domain.entity.Schedule;
import com.zerobase.schedulemanagement.entry.dto.ResponseCode;
import com.zerobase.schedulemanagement.entry.dto.schdule.CreateScheduleParam;
import com.zerobase.schedulemanagement.entry.dto.schdule.ScheduleResponseDto;
import com.zerobase.schedulemanagement.entry.dto.schdule.UpdateScheduleParam;
import com.zerobase.schedulemanagement.infra.exception.ScheduleManagementException;
import com.zerobase.schedulemanagement.infra.persistence.MemberRepository;
import com.zerobase.schedulemanagement.infra.persistence.MemberScheduleRepository;
import com.zerobase.schedulemanagement.infra.persistence.ScheduleRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {

  private final MemberRepository memberRepository;
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

  @Transactional(readOnly = false)
  public Long createSchedule(CreateScheduleParam param) {
    // member validation
    List<Long> participationIds = param.getParticipationIds();
    // 1. 일정을 생성하는 사람은, 해당 일정에 참여자이어야 한다.
    if (!participationIds.contains(param.getMemberId())) {
      throw new ScheduleManagementException(ResponseCode.NOT_PARTICIPATED_SCHEDULE);
    }
    // 2. 일정에 참여하는 사람은, 존재하는 유저이어야 한다.
    List<Member> participationMembers = memberRepository.findAllByIdIn(participationIds);
    if (!isParticipationsExistMember(participationMembers, participationIds)) {
      throw new ScheduleManagementException(ResponseCode.NO_MEMBER);
    }

    // save schedule
    Schedule schedule = scheduleRepository.save(param.toEntity());

    // save memberSchedule
    List<MemberSchedule> memberSchedules = new ArrayList<>(Collections.emptyList());
    participationMembers.forEach(member -> {
      MemberSchedule memberSchedule = MemberSchedule.builder()
                                                    .member(member)
                                                    .schedule(schedule)
                                                    .build();
      memberSchedules.add(memberSchedule);
    });
    memberScheduleRepository.saveAll(memberSchedules);

    return schedule.getId();
  }

  @Transactional(readOnly = false)
  public Long updateSchedule(UpdateScheduleParam param) {
    // schedule validation
    Long scheduleId = param.getScheduleId();
    Schedule schedule = scheduleRepository.findById(scheduleId)
                                          .orElseThrow(() -> new ScheduleManagementException(ResponseCode.NO_SCHEDULE));

    // member validation
    List<Long> participationIds = param.getParticipationIds();
    // 1. 일정을 수정하는 사람은, 해당 일정에 참여자이어야 한다.
    if (!participationIds.contains(param.getMemberId())) {
      throw new ScheduleManagementException(ResponseCode.NOT_PARTICIPATED_SCHEDULE);
    }
    // 2. 일정에 참여하는 사람은, 존재하는 유저이어야 한다.
    List<Member> participationMembers = memberRepository.findAllByIdIn(participationIds);
    if (!isParticipationsExistMember(participationMembers, participationIds)) {
      throw new ScheduleManagementException(ResponseCode.NO_MEMBER);
    }

    // member schedule update
    List<MemberSchedule> originalMemberSchedules = memberScheduleRepository.findAllByScheduleId(scheduleId);
    Set<MemberSchedule> newMemberSchedulesSet
        = participationMembers.stream().map(member -> MemberSchedule.builder()
                                                                    .member(member)
                                                                    .schedule(schedule)
                                                                    .build())
                              .collect(Collectors.toSet());

    Set<MemberSchedule> originalMemberSchedulesSet = new HashSet<>(originalMemberSchedules);

    // add
    Set<MemberSchedule> memberSchedulesToAdd = new HashSet<>(newMemberSchedulesSet);
    memberSchedulesToAdd.removeAll(originalMemberSchedulesSet);

    // delete
    Set<MemberSchedule> memberSchedulesToDelete = new HashSet<>(originalMemberSchedulesSet);
    memberSchedulesToDelete.removeAll(newMemberSchedulesSet);

    memberScheduleRepository.saveAll(memberSchedulesToAdd);
    memberScheduleRepository.deleteAll(memberSchedulesToDelete);

    // schedule update
    schedule.apply(param);
    return scheduleRepository.save(schedule).getId();
  }

  private Boolean isParticipationsExistMember(List<Member> participationMembers, List<Long> participationIds) {
    return participationMembers.size() == participationIds.size();
  }
}

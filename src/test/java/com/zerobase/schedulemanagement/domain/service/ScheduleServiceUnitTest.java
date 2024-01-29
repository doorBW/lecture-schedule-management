package com.zerobase.schedulemanagement.domain.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zerobase.schedulemanagement.domain.entity.Member;
import com.zerobase.schedulemanagement.domain.entity.MemberSchedule;
import com.zerobase.schedulemanagement.domain.entity.Schedule;
import com.zerobase.schedulemanagement.entry.dto.ResponseCode;
import com.zerobase.schedulemanagement.entry.dto.schdule.CreateScheduleParam;
import com.zerobase.schedulemanagement.infra.exception.ScheduleManagementException;
import com.zerobase.schedulemanagement.infra.persistence.MemberRepository;
import com.zerobase.schedulemanagement.infra.persistence.MemberScheduleRepository;
import com.zerobase.schedulemanagement.infra.persistence.ScheduleRepository;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceUnitTest {

  @Mock
  private ScheduleRepository scheduleRepository;

  @Mock
  private MemberScheduleRepository memberScheduleRepository;

  @Mock
  private MemberRepository memberRepository;

  @InjectMocks
  private ScheduleService scheduleService;


  @Test
  public void S_getSchedules_exist_schedules() {
    // given
    Long memberId = 1L;
    MemberSchedule mockMemberSchedule = mock(MemberSchedule.class);
    when(memberScheduleRepository.findAllByMemberId(memberId)).thenReturn(List.of(mockMemberSchedule));
    Schedule mockSchedule = mock(Schedule.class);
    when(mockMemberSchedule.getSchedule()).thenReturn(mockSchedule);
    when(mockSchedule.getId()).thenReturn(100L);

    when(scheduleRepository.findAllByIdIn(any())).thenReturn(List.of(mockSchedule));

    // when
    List<Schedule> schedules = scheduleService.getSchedules(memberId);

    // then
    Assertions.assertEquals(1, schedules.size());
    Assertions.assertEquals(100L, schedules.get(0).getId());
  }

  @Test
  public void S_getSchedules_non_exist_schedules() {
    // given
    Long memberId = 1L;
    when(memberScheduleRepository.findAllByMemberId(memberId)).thenReturn(Collections.emptyList());
    when(scheduleRepository.findAllByIdIn(any())).thenReturn(Collections.emptyList());

    // when
    List<Schedule> schedules = scheduleService.getSchedules(memberId);

    // then
    Assertions.assertEquals(0, schedules.size());
  }

  @Test
  public void S_createSchedule() {
    // given
    CreateScheduleParam param = CreateScheduleParam.builder()
                                                   .memberId(1L)
                                                   .title("test title")
                                                   .description("test desc")
                                                   .startAt(1000000L)
                                                   .endAt(1000000L)
                                                   .isDone(false)
                                                   .participationIds(List.of(1L, 2L, 3L))
                                                   .build();
    Member member1 = mock(Member.class);
    Member member2 = mock(Member.class);
    Member member3 = mock(Member.class);
    when(memberRepository.findAllByIdIn(any())).thenReturn(List.of(member1, member2, member3));
    Schedule mockSchedule = mock(Schedule.class);
    when(mockSchedule.getId()).thenReturn(1L);
    when(scheduleRepository.save(any())).thenReturn(mockSchedule);
    when(memberScheduleRepository.saveAll(any())).thenReturn(mock(List.class));

    // when
    Long savedScheduleId = scheduleService.createSchedule(param);

    // then
    Assertions.assertEquals(1L, savedScheduleId);
    verify(scheduleRepository, times(1)).save(any());
    verify(memberScheduleRepository, times(1)).saveAll(any());
  }

  @Test
  public void F_createSchedule_not_participated_schedule() {
    // given
    // when
    // then
  }

  @Test
  public void F_createSchedule_no_member() {
    // given
    CreateScheduleParam param = CreateScheduleParam.builder()
                                                   .memberId(1L)
                                                   .title("test title")
                                                   .description("test desc")
                                                   .startAt(1000000L)
                                                   .endAt(1000000L)
                                                   .isDone(false)
                                                   .participationIds(List.of(1L, 2L, 3L))
                                                   .build();
    Member member1 = mock(Member.class);
    Member member2 = mock(Member.class);
    when(memberRepository.findAllByIdIn(any())).thenReturn(List.of(member1, member2));

    // when
    ScheduleManagementException exception = Assertions.assertThrows(ScheduleManagementException.class,
                                                                    () -> scheduleService.createSchedule(param));

    // then
    Assertions.assertEquals(ResponseCode.NO_MEMBER, exception.getCode());
    verify(scheduleRepository, times(0)).save(any());
    verify(memberScheduleRepository, times(0)).saveAll(any());
  }
}
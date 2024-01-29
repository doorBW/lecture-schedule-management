package com.zerobase.schedulemanagement.domain.service;

import com.zerobase.schedulemanagement.domain.entity.Member;
import com.zerobase.schedulemanagement.domain.entity.MemberSchedule;
import com.zerobase.schedulemanagement.domain.entity.Schedule;
import com.zerobase.schedulemanagement.entry.dto.ResponseCode;
import com.zerobase.schedulemanagement.entry.dto.schdule.CreateScheduleParam;
import com.zerobase.schedulemanagement.infra.exception.ScheduleManagementException;
import com.zerobase.schedulemanagement.infra.persistence.MemberRepository;
import com.zerobase.schedulemanagement.infra.persistence.MemberScheduleRepository;
import com.zerobase.schedulemanagement.infra.persistence.ScheduleRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ScheduleServiceIntegrationTest {

  @Autowired
  private ScheduleRepository scheduleRepository;

  @Autowired
  private MemberScheduleRepository memberScheduleRepository;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private ScheduleService scheduleService;

  @BeforeEach
  public void init() {
    Member member1 = Member.builder()
                           .name("user1")
                           .build();
    Member member2 = Member.builder()
                           .name("user2")
                           .build();
    Member member3 = Member.builder()
                           .name("user3")
                           .build();
    memberRepository.saveAll(List.of(member1, member2, member3));
  }

  @Test
  public void S_getSchedules_exist_schedules() {
    // given
    Long memberId = 1L;
    Member member = memberRepository.findById(memberId).get();

    Schedule schedule = Schedule.builder()
                                .title("test title")
                                .description("test description")
                                .startAt(Instant.now().toEpochMilli())
                                .endAt(Instant.now().plus(5, ChronoUnit.DAYS).toEpochMilli())
                                .isDone(false)
                                .createdBy(member.getId())
                                .updatedBy(member.getId())
                                .build();

    scheduleRepository.save(schedule);

    MemberSchedule memberSchedule = MemberSchedule.builder()
                                                  .member(member)
                                                  .schedule(schedule)
                                                  .build();

    memberScheduleRepository.save(memberSchedule);

    // when
    List<Schedule> schedules = scheduleService.getSchedules(memberId);

    // then
    Assertions.assertEquals(1, schedules.size());
    Assertions.assertEquals(1L, schedules.get(0).getId());
    Assertions.assertEquals("test title", schedules.get(0).getTitle());
  }

  @Test
  public void S_getSchedules_non_exist_schedules() {
    // given
    Long memberId = 1L;
    Member member = memberRepository.findById(memberId).get();

    // when
    List<Schedule> schedules = scheduleService.getSchedules(member.getId());

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

    // when
    Long savedScheduleId = scheduleService.createSchedule(param);

    // then
    Assertions.assertEquals(1L, savedScheduleId);
    Schedule schedule = scheduleRepository.findById(savedScheduleId).get();
    Assertions.assertEquals("test title", schedule.getTitle());
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
                                                   .participationIds(List.of(1L, 2L, 3L, 4L))
                                                   .build();

    // when
    ScheduleManagementException exception = Assertions.assertThrows(ScheduleManagementException.class,
                                                                    () -> scheduleService.createSchedule(param));

    // then
    Assertions.assertEquals(ResponseCode.NO_MEMBER, exception.getCode());
  }
}
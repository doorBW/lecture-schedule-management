package com.zerobase.schedulemanagement.infra.persistence;

import com.zerobase.schedulemanagement.domain.entity.MemberSchedule;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberScheduleRepository extends JpaRepository<MemberSchedule, Long> {

  List<MemberSchedule> findAllByMemberId(Long memberId);

  List<MemberSchedule> findAllByScheduleId(Long scheduleId);

}

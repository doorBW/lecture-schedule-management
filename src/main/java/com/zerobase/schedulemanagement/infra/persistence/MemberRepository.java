package com.zerobase.schedulemanagement.infra.persistence;

import com.zerobase.schedulemanagement.domain.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

  public List<Member> findAllByIdIn(List<Long> ids);

}

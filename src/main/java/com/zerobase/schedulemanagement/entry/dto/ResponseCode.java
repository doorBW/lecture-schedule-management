package com.zerobase.schedulemanagement.entry.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {
  SUCCESS("0000", "요청에 성공하였습니다."),
  NO_SCHEDULE("0400", "일정이 존재하지 않습니다."),
  NOT_PARTICIPATED_SCHEDULE("0401", "일정에 참여되어 있지 않습니다."),
  NO_MEMBER("0402", "존재하지 않는 사용자입니다."),

  BAD_REQUEST("9400", "잘못된 요청입니다."),
  INTERNAL_SERVER_ERROR("9500", "서버 내부 에러입니다.");

  private String code;
  private String message;
}

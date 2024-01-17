package com.zerobase.schedulemanagement.infra.exception;

import com.zerobase.schedulemanagement.entry.dto.ResponseCode;
import lombok.Getter;

@Getter
public class ScheduleManagementException extends RuntimeException {

  private ResponseCode code;
  private String message;

  public ScheduleManagementException(ResponseCode responseCode) {
    this.code = responseCode;
    this.message = responseCode.getMessage();
  }

  public ScheduleManagementException(ResponseCode responseCode, String returnMessage) {
    this.code = responseCode;
    this.message = returnMessage;
  }

}

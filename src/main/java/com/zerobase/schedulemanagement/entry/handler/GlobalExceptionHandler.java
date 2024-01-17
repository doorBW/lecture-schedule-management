package com.zerobase.schedulemanagement.entry.handler;

import com.zerobase.schedulemanagement.entry.dto.ResponseCode;
import com.zerobase.schedulemanagement.entry.dto.ResponseDto;
import com.zerobase.schedulemanagement.infra.exception.ScheduleManagementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(ScheduleManagementException.class)
  public ResponseDto<?> handleScheduleManagementException(ScheduleManagementException ex) {
    return ResponseDto.of(ex.getCode(), ex.getMessage());
  }

  @ExceptionHandler(value = {
      HttpMessageNotReadableException.class,
      MethodArgumentNotValidException.class,
      HttpRequestMethodNotSupportedException.class,
      MissingServletRequestParameterException.class,
      MethodArgumentTypeMismatchException.class
  })
  public ResponseDto<?> handleException(Exception ex) {
    log.warn("[REQUEST EXCEPTION] : {}", ex.getMessage(), ex);
    return ResponseDto.of(ResponseCode.BAD_REQUEST);
  }

  @ExceptionHandler(value = {Exception.class})
  public ResponseDto<?> handleServerException(Exception ex) {
    log.error("[REQUEST EXCEPTION] : {}", ex.getMessage(), ex);
    return ResponseDto.of(ResponseCode.INTERNAL_SERVER_ERROR);
  }
}

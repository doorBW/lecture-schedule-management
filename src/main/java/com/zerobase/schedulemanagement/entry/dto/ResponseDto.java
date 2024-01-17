package com.zerobase.schedulemanagement.entry.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.ToString;

@Getter
@JsonInclude(Include.NON_NULL) // data null인 경우, response에 포함하지 않기 위함
@ToString
public class ResponseDto<T> {

  private String code;
  private String message;
  private T data;

  public static <T> ResponseDto of(T data) {
    ResponseDto<T> response = new ResponseDto<>();
    response.code = ResponseCode.SUCCESS.getCode();
    response.message = ResponseCode.SUCCESS.getMessage();
    response.data = data;
    return response;
  }

  public static <T> ResponseDto of(ResponseCode responseCode) {
    ResponseDto<T> response = new ResponseDto<>();
    response.code = responseCode.getCode();
    response.message = responseCode.getMessage();
    return response;
  }

  public static <T> ResponseDto of(ResponseCode responseCode, String responseMessage) {
    ResponseDto<T> response = new ResponseDto<>();
    response.code = responseCode.getCode();
    response.message = responseMessage;
    return response;
  }
}

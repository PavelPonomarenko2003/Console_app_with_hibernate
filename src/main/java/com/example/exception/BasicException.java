package com.example.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter // lombok annotation for getter
public abstract class BasicException extends RuntimeException {

  private final String errorCode;
  private final HttpStatus status;

  public BasicException(String message, String errorCode, HttpStatus status) {
    super(message);
    this.errorCode = errorCode;
    this.status = status;
  }

}

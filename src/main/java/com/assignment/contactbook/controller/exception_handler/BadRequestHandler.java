package com.assignment.contactbook.controller.exception_handler;

import com.assignment.contactbook.entity.response.SingleContactResponse;
import com.assignment.contactbook.exception.BadRequestException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by kousik on 29/03/18.
 */
@ControllerAdvice
public class BadRequestHandler {

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<SingleContactResponse> handleBadRequest(BadRequestException e,
      HttpServletResponse response) {

    SingleContactResponse response1 = new SingleContactResponse(e.getMessage(), null);
    return new ResponseEntity<>(response1, HttpStatus.BAD_REQUEST);
  }
}

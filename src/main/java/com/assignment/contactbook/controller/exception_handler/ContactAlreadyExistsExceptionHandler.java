package com.assignment.contactbook.controller.exception_handler;

import com.assignment.contactbook.entity.response.SingleContactResponse;
import com.assignment.contactbook.exception.ContactAlreadyExistsException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by kousik on 29/03/18.
 */
@ControllerAdvice
public class ContactAlreadyExistsExceptionHandler {

  @ExceptionHandler(ContactAlreadyExistsException.class)
  public ResponseEntity<SingleContactResponse> handleBadRequest(ContactAlreadyExistsException e,
      HttpServletResponse response) {

    return new ResponseEntity<>(new SingleContactResponse(e.getMessage(), null),
        HttpStatus.NOT_ACCEPTABLE);
  }

}

package com.assignment.contactbook.entity.response;

/**
 * Created by kousik on 29/03/18.
 */
public class BaseResponse {
  private String message;

  public BaseResponse(){}

  public BaseResponse(String m){
    this.message = m;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}

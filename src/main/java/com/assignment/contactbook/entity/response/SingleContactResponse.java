package com.assignment.contactbook.entity.response;

import com.assignment.contactbook.entity.Contact;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * Created by kousik on 29/03/18.
 */

@JsonNaming(value = SnakeCaseStrategy.class)
public class SingleContactResponse extends BaseResponse{
  private Contact result;

  public SingleContactResponse(){
  }

  public SingleContactResponse(String m, Contact r){
    super(m);
    this.result = r;
  }

  public Contact getResult() {
    return result;
  }

  public void setResult(Contact result) {
    this.result = result;
  }
}


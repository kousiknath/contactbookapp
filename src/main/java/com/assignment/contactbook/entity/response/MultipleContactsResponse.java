package com.assignment.contactbook.entity.response;

import com.assignment.contactbook.entity.Contact;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;

/**
 * Created by kousik on 29/03/18.
 */

@JsonNaming(value = SnakeCaseStrategy.class)
public class MultipleContactsResponse extends BaseResponse{
  private List<Contact> result;

  public MultipleContactsResponse(){
  }

  public MultipleContactsResponse(String m, List<Contact> r){
    super(m);
    this.result = r;
  }

  public List<Contact> getResult() {
    return result;
  }

  public void setResult(List<Contact> result) {
    this.result = result;
  }
}

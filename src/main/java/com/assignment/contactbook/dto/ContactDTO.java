package com.assignment.contactbook.dto;

/**
 * Created by kousik on 29/03/18.
 */
public class ContactDTO {

  private String name;
  private String email;

  public ContactDTO(String name, String email){
    this.name = name;
    this.email = email;
  }

  public ContactDTO(){}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}

package com.assignment.contactbook.exception;

/**
 * Created by kousik on 29/03/18.
 */
public class ContactAlreadyExistsException extends Exception {
  ContactAlreadyExistsException(){}

  public ContactAlreadyExistsException(String msg){
    super(msg);
  }

}

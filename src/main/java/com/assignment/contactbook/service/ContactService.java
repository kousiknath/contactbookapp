package com.assignment.contactbook.service;

import com.assignment.contactbook.entity.Contact;
import com.assignment.contactbook.exception.BadRequestException;
import java.util.List;

/**
 * Created by kousik on 28/03/18.
 */
public interface ContactService {
  Contact createContact(String name, String email) throws Exception;
  Contact updateContact(Contact existingContact, String newName, String newEmail)
      throws BadRequestException;
  void deleteContact(String email) throws BadRequestException;
  List<Contact> searchContact(String name, String email) throws BadRequestException;
}

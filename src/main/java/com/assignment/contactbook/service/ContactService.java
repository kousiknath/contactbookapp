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
  void deleteContact(Long id) throws BadRequestException;
  List<Contact> searchContact(String name, String email, int page, int size) throws BadRequestException;
  Contact searchExactByEmail(String email) throws BadRequestException;
  Contact searchById(Long id) throws BadRequestException;
}

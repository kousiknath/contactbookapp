package com.assignment.contactbook.service.impl;

import com.assignment.contactbook.exception.BadRequestException;
import com.assignment.contactbook.exception.ContactAlreadyExistsException;
import com.assignment.contactbook.utils.Utility;
import com.assignment.contactbook.entity.Contact;
import com.assignment.contactbook.repository.ContactRepository;
import com.assignment.contactbook.service.ContactService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by kousik on 28/03/18.
 */

@Service
public class ContactServiceImpl implements ContactService {

  @Autowired
  private ContactRepository contactRepository;

  @Autowired
  private Utility utility;

  @Override
  @Transactional
  public Contact createContact(String name, String email)
      throws ContactAlreadyExistsException, BadRequestException {
    if(!utility.isValidInput(name) || !utility.isValidInput(email))
      throw new BadRequestException("Name and email both should be valid");

    List<Contact> matchingContact = contactRepository.findByEmailRegex(email);
    if(matchingContact != null && matchingContact.size() > 0)
      throw new ContactAlreadyExistsException("Contact already exists");

    return contactRepository.save(new Contact(name, email));
  }

  @Override
  public Contact updateContact(Contact existingContact, String newName, String newEmail)
      throws BadRequestException {
    if(!utility.isValidInput(newName) && !utility.isValidInput(newEmail))
      throw new BadRequestException("Either name or email should be non null");

    if(existingContact == null)
      throw new BadRequestException("Got an invalid contact to update");

    if(utility.isValidInput(newName))
      existingContact.setName(newName);

    if(utility.isValidInput(newEmail))
      existingContact.setEmail(newEmail);

    return contactRepository.save(existingContact);
  }

  @Override
  @Transactional
  public void deleteContact(String email) throws BadRequestException {
    if(!utility.isValidInput(email))
      throw new BadRequestException("Unique email id is required for deleting entry.");

    List<Contact> foundContact = contactRepository.findByEmailRegex(email);
    if(foundContact.size() > 0)
      contactRepository.delete(foundContact.get(0));
  }

  @Override
  public List<Contact> searchContact(String name, String email) throws BadRequestException {
    if(!utility.isValidInput(name) && !utility.isValidInput(email))
      throw new BadRequestException("Either name or email should be non null");

    if(utility.isValidInput(name))
      return contactRepository.findByNameRegex(name);

    return contactRepository.findByEmailRegex(email);

  }
}

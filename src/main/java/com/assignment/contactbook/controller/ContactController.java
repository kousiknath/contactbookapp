package com.assignment.contactbook.controller;

import com.assignment.contactbook.entity.response.SingleContactResponse;
import com.assignment.contactbook.exception.BadRequestException;
import com.assignment.contactbook.entity.response.MultipleContactsResponse;
import com.assignment.contactbook.utils.Utility;
import com.assignment.contactbook.dto.ContactDTO;
import com.assignment.contactbook.entity.Contact;
import com.assignment.contactbook.service.ContactService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kousik on 29/03/18.
 */

@RestController
@RequestMapping(value = "/contacts")
public class ContactController {

  @Autowired
  private ContactService contactService;

  @Autowired
  private Utility utility;

  @PostMapping
  public ResponseEntity<SingleContactResponse> create(@RequestBody ContactDTO contactDTO) throws Exception {
    if(!utility.isValidInput(contactDTO.getName()) || !utility.isValidInput(contactDTO.getEmail()))
      throw new BadRequestException("Invalid name or email in request body");

    Contact createdContact = contactService.createContact(contactDTO.getName(), contactDTO.getEmail());
    SingleContactResponse response = new SingleContactResponse("Success", createdContact);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PatchMapping(value = "/{email}")
  public ResponseEntity<SingleContactResponse> update(@PathVariable(value = "email") String email, @RequestBody ContactDTO contactDTO)
      throws BadRequestException {
    if(!utility.isValidInput(email))
      throw new BadRequestException("Email of a contact can't be empty");

    List<Contact> matchingContacts = contactService.searchContact(null, email);
    if(matchingContacts == null || matchingContacts.size() == 0)
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    Contact toBeUpdated = matchingContacts.get(0);
    Contact updatedContact = contactService.updateContact(toBeUpdated, contactDTO.getName(), contactDTO.getEmail());
    SingleContactResponse response = new SingleContactResponse("Success", updatedContact);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @DeleteMapping(value = "/{email}")
  public ResponseEntity<SingleContactResponse> delete(@PathVariable(value = "email") String email)
      throws BadRequestException {
    if(!utility.isValidInput(email))
      throw new BadRequestException("Valid email required to delete contact");

    List<Contact> matchingContacts = contactService.searchContact(null, email);
    if(matchingContacts == null || matchingContacts.size() == 0)
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    contactService.deleteContact(email);
    SingleContactResponse response = new SingleContactResponse("Success", matchingContacts.get(0));

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping(value = "/search/name/{name}")
  public ResponseEntity<MultipleContactsResponse> searchByName(@PathVariable(value = "name") String name)
      throws BadRequestException {
    if(!utility.isValidInput(name))
      throw new BadRequestException("Invalid name to search");

    List<Contact> searchResult = contactService.searchContact(name, null);
    MultipleContactsResponse response = new MultipleContactsResponse("Success", searchResult);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping(value = "/search/email/{email}")
  public ResponseEntity<MultipleContactsResponse> searchByEmail(@PathVariable(value = "email") String email)
      throws BadRequestException {
    if(!utility.isValidInput(email))
      throw new BadRequestException("Invalid name to search");

    List<Contact> searchResult = contactService.searchContact(null, email);
    MultipleContactsResponse response = new MultipleContactsResponse("Success", searchResult);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}

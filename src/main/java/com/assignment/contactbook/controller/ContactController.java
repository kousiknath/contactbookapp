package com.assignment.contactbook.controller;

import com.assignment.contactbook.entity.response.SingleContactResponse;
import com.assignment.contactbook.exception.BadRequestException;
import com.assignment.contactbook.entity.response.MultipleContactsResponse;
import com.assignment.contactbook.exception.UnAuthrizedException;
import com.assignment.contactbook.utils.Utility;
import com.assignment.contactbook.dto.ContactDTO;
import com.assignment.contactbook.entity.Contact;
import com.assignment.contactbook.service.ContactService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  @Value("${app.token}")
  private String authToken;

  @PostMapping
  public ResponseEntity<SingleContactResponse> create(
      @RequestHeader(value = "token") String token,
      @RequestBody ContactDTO contactDTO) throws Exception {
    validateToken(token);

    if(!utility.isValidInput(contactDTO.getName()) || !utility.isValidInput(contactDTO.getEmail()))
      throw new BadRequestException("Invalid name or email in request body");

    Contact createdContact = contactService.createContact(contactDTO.getName(), contactDTO.getEmail());
    SingleContactResponse response = new SingleContactResponse("Success", createdContact);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PatchMapping(value = "/{id}")
  public ResponseEntity<SingleContactResponse> update(
      @RequestHeader(value = "token") String token,
      @PathVariable(value = "id") Long id,
      @RequestBody ContactDTO contactDTO) throws BadRequestException, UnAuthrizedException {
    validateToken(token);

    if(id == null || id <= 0)
      throw new BadRequestException("Valid id required to update contact");

    Contact matchingContact = contactService.searchById(id);
    if(matchingContact == null)
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    Contact updatedContact = contactService.updateContact(matchingContact, contactDTO.getName(), contactDTO.getEmail());
    SingleContactResponse response = new SingleContactResponse("Success", updatedContact);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity delete(
      @RequestHeader(value = "token") String token,
      @PathVariable(value = "id") Long id)
      throws BadRequestException, UnAuthrizedException {
    validateToken(token);

    if(id == null || id <= 0)
      throw new BadRequestException("Valid id required to delete contact");

    Contact matchingContact = contactService.searchById(id);
    if(matchingContact == null)
      return new ResponseEntity(HttpStatus.NOT_FOUND);

    contactService.deleteContact(id);

    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @GetMapping(value = "/search")
  public ResponseEntity<MultipleContactsResponse> search(
      @RequestHeader(value = "token") String token,
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "email", required = false) String email,
      @RequestParam(value = "page", required = false, defaultValue = "0") int page,
      @RequestParam(value = "size", required = false, defaultValue = "10") int size)
      throws BadRequestException, UnAuthrizedException {
    validateToken(token);

    if(!utility.isValidInput(email) && !utility.isValidInput(name)
        ) throw new BadRequestException("Either name or email should be valid");

    List<Contact> searchResult = contactService.searchContact(name, email, page, size);
    MultipleContactsResponse response = new MultipleContactsResponse("Success", searchResult);

    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  private void validateToken(String token) throws UnAuthrizedException {
    //Extremely simple basic authentication
    if(!utility.isValidInput(token) || !token.equals(authToken))
      throw new UnAuthrizedException("User is not allowed to access the api");
  }
}

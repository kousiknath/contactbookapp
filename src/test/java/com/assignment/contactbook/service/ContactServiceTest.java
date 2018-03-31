package com.assignment.contactbook.service;

import com.assignment.contactbook.entity.Contact;
import com.assignment.contactbook.exception.BadRequestException;
import com.assignment.contactbook.repository.ContactRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

/**
 * Created by kousik on 28/03/18.
 */

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class ContactServiceTest {

  @Autowired
  private ContactService contactService;

  @MockBean
  private ContactRepository contactRepository;

  @Test
  public void testCreateContact() throws Exception {
    String name = "contact_1";
    String email = "contact_1@test.com";

    Contact expectedResult = new Contact(name, email);
    given(contactRepository.save(any(Contact.class))).willReturn(expectedResult);

    Contact createdContact = contactService.createContact(name, email);

    assertThat(createdContact.getName()).isEqualTo(name);
    assertThat(createdContact.getEmail()).isEqualTo(email);
  }

  @Test(expected = BadRequestException.class)
  public void testCreateContactWithInvalidName() throws Exception {
    String name = "";
    String email = "contact_1@test.com";

    Contact expectedResult = new Contact(name, email);
    given(contactRepository.save(any(Contact.class))).willReturn(expectedResult);

    contactService.createContact(name, email);
  }

  @Test
  public void testUpdateContact() throws Exception {
    String name = "contact_1";
    String email = "contact_1@test.com";

    Contact c = new Contact(name, email);
    given(contactRepository.save(c)).willReturn(c);
    Contact createdContact = contactService.createContact(name, email);

    Contact c1 = new Contact("contact_2", email);
    given(contactRepository.save(c1)).willReturn(c1);
    Contact updatedContact = contactService.updateContact(createdContact, "contact_2", null);

    assertThat(updatedContact.getName()).isEqualTo("contact_2");
    assertThat(updatedContact.getEmail()).isEqualTo(createdContact.getEmail());
  }

  @Test
  public void testSearchContactByNameOrEmail() throws BadRequestException {
    String firstContactName = "first";
    String firstContactEmail = "first@test.com";
    Contact firstContact = new Contact(firstContactName, firstContactEmail);

    String secondContactName = "first_second";
    String secondContactEmail = "second@test.com";
    Contact secondContact = new Contact(secondContactName, secondContactEmail);

    PageImpl<Contact> pagedResult = new PageImpl<>(Arrays.asList(firstContact, secondContact));
    PageImpl<Contact> partialPagedResult = new PageImpl<>(Arrays.asList(secondContact));

    given(contactRepository.findByNameIgnoreCaseContaining(anyString(), any(PageRequest.class))).willReturn(pagedResult);
    given(contactRepository.findByEmailIgnoreCaseContaining(anyString(), any(PageRequest.class))).willReturn(partialPagedResult);

    List<Contact> contactsByName = contactService.searchContact("first", null, 0, 10);
    assertThat(contactsByName.size()).isEqualTo(2);

    for(Contact c : contactsByName)
      assertThat(c.getName().contains("first"));

    List<Contact> contactsByEmail = contactService.searchContact(null, "second", 0, 10);
    assertThat(contactsByEmail.size()).isEqualTo(1);
    assertThat(contactsByEmail.get(0).getEmail().contains("second"));
  }
}

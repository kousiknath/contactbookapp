package com.assignment.contactbook.repository;

import com.assignment.contactbook.entity.Contact;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by kousik on 28/03/18.
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DataMongoTest
public class ContactRepositoryTest {

  private final String contactName = "test";
  private final String contactEmail = "test@test.com";
  private Contact testContact;

  @Autowired
  private ContactRepository contactRepository;

  @Before
  public void setUp(){
    //Delete the test repository first.
    contactRepository.deleteAll();

    this.testContact = new Contact(contactName, contactEmail);
    this.testContact = contactRepository.save(this.testContact);
  }

  @Test
  public void testContactCreation(){
    assertThat(contactName).isEqualTo(testContact.getName());
    assertThat(contactEmail).isEqualTo(testContact.getEmail());
  }

  @Test
  public void testSearchContactWithFullName(){
    List<Contact> searchedContacts = contactRepository.findByNameIgnoreCaseContaining("test");

    assertThat(searchedContacts.size()).isEqualTo(1);
    assertThat(searchedContacts.get(0).getName()).isEqualTo(this.contactName);
  }

  @Test
  public void testSearchContactWithPartialName(){
    List<Contact> searchedContacts = contactRepository.findByNameIgnoreCaseContaining("tes");

    assertThat(searchedContacts.size()).isEqualTo(1);
    assertThat(searchedContacts.get(0).getName()).isEqualTo(this.contactName);
  }

  @Test
  public void testSearchContactCaseInsensitive(){
    List<Contact> searchedContacts = contactRepository.findByNameIgnoreCaseContaining("Test");

    assertThat(searchedContacts.size()).isEqualTo(1);
    assertThat(searchedContacts.get(0).getName()).isEqualTo(this.contactName);
  }

  @Test
  public void testSearchContactWithMultipleResult(){
    Contact anotherContact = new Contact("test1", "test1@test.com");
    contactRepository.save(anotherContact);

    List<Contact> searchedContacts = contactRepository.findByNameIgnoreCaseContaining("tes");

    assertThat(searchedContacts.size()).isEqualTo(2);
  }

  @Test
  public void testEditContact(){
    String newName = "test_new";
    List<Contact> searchedContacts = contactRepository.findByNameIgnoreCaseContaining("test");
    Contact existingContact = searchedContacts.get(0);

    existingContact.setName(newName);
    contactRepository.save(existingContact);

    List<Contact> searchedContactsAfterUpdation = contactRepository.findByNameIgnoreCaseContaining("test");

    assertThat(searchedContactsAfterUpdation.get(0).getId()).isEqualTo(existingContact.getId());
    assertThat(searchedContactsAfterUpdation.get(0).getName()).isEqualTo(newName);
  }

  @Test
  public void testDeleteContact(){
    List<Contact> searchedContacts = contactRepository.findByNameIgnoreCaseContaining("test");
    assertThat(searchedContacts.size()).isEqualTo(1);

    contactRepository.delete(searchedContacts.get(0));

    List<Contact> searchedContactsAfterDeletion = contactRepository.findByNameIgnoreCaseContaining("test");
    assertThat(searchedContactsAfterDeletion.size()).isEqualTo(0);
  }
}

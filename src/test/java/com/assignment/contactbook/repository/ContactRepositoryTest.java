package com.assignment.contactbook.repository;

import com.assignment.contactbook.entity.Contact;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by kousik on 28/03/18.
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= Replace.NONE)
public class ContactRepositoryTest {

  private final String contactName = "test";
  private final String contactEmail = "test@test.com";
  private Contact testContact;
  private PageRequest defaultPageRequest;

  @Autowired
  private ContactRepository contactRepository;

  @Before
  public void setUp(){
    //Delete the test repository first.
    contactRepository.deleteAll();

    this.testContact = new Contact(contactName, contactEmail);
    this.testContact = contactRepository.save(this.testContact);
    this.defaultPageRequest = new PageRequest(0, 10);
  }

  @Test
  public void testContactCreation(){
    assertThat(contactName).isEqualTo(testContact.getName());
    assertThat(contactEmail).isEqualTo(testContact.getEmail());
  }

  @Test
  public void testSearchContactWithFullName(){
    Page<Contact> searchedContacts = contactRepository.findByNameIgnoreCaseContaining("test", defaultPageRequest);
    List<Contact> contacts = searchedContacts.getContent();

    assertThat(contacts.size()).isEqualTo(1);
    assertThat(contacts.get(0).getName()).isEqualTo(this.contactName);
  }

  @Test
  public void testSearchContactWithPartialName(){
    Page<Contact> searchedContacts = contactRepository.findByNameIgnoreCaseContaining("tes", defaultPageRequest);

    assertThat(searchedContacts.getContent().size()).isEqualTo(1);
    assertThat(searchedContacts.getContent().get(0).getName()).isEqualTo(this.contactName);
  }

  @Test
  public void testSearchContactCaseInsensitive(){
    Page<Contact> searchedContacts = contactRepository.findByNameIgnoreCaseContaining("Test", defaultPageRequest);

    assertThat(searchedContacts.getContent().size()).isEqualTo(1);
    assertThat(searchedContacts.getContent().get(0).getName()).isEqualTo(this.contactName);
  }

  @Test
  public void testSearchContactWithMultipleResult(){
    Contact anotherContact = new Contact("test1", "test1@test.com");
    contactRepository.save(anotherContact);

    Page<Contact> searchedContacts = contactRepository.findByNameIgnoreCaseContaining("tes", defaultPageRequest);

    assertThat(searchedContacts.getContent().size()).isGreaterThan(1);
  }

  @Test
  public void testSearchContactWithMoreThan10Result(){
    for(int i = 0; i < 15; i++){
      String n = "test_new" + i;
      String e = n + "@test.com";

      contactRepository.save(new Contact(n, e));
    }

    Page<Contact> firstPageResult = contactRepository.findByNameIgnoreCaseContaining("tes", defaultPageRequest);
    assertThat(firstPageResult.getContent().size()).isEqualTo(defaultPageRequest.getPageSize());
  }

  @Test
  public void testEditContact(){
    String newName = "test_new";
    Contact existingContact = contactRepository.findByEmail(contactEmail);

    existingContact.setName(newName);
    contactRepository.save(existingContact);

    Contact searchedContactAfterUpdation = contactRepository.findByEmail(contactEmail);

    assertThat(searchedContactAfterUpdation.getId()).isEqualTo(existingContact.getId());
    assertThat(searchedContactAfterUpdation.getName()).isEqualTo(newName);
  }

  @Test
  public void testDeleteContact(){
    Contact searchedContact = contactRepository.findByEmail(contactEmail);
    contactRepository.delete(searchedContact);

    Contact searchedContactAfterDeletion = contactRepository.findByEmail(contactEmail);
    assertThat(searchedContactAfterDeletion).isNull();
  }
}

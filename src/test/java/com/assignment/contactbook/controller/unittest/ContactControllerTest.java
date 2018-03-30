package com.assignment.contactbook.controller.unittest;

import com.assignment.contactbook.dto.ContactDTO;
import com.assignment.contactbook.entity.Contact;
import com.assignment.contactbook.entity.response.MultipleContactsResponse;
import com.assignment.contactbook.entity.response.SingleContactResponse;
import com.assignment.contactbook.repository.ContactRepository;
import com.assignment.contactbook.service.ContactService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.BDDMockito.doReturn;

/**
 * Created by kousik on 29/03/18.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ContactControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @SpyBean
  private ContactService contactService;

  @MockBean
  private ContactRepository contactRepository;

  private JacksonTester<ContactDTO> jsonRequest;
  private JacksonTester<SingleContactResponse> jsonResponse;
  private JacksonTester<MultipleContactsResponse> multipleContactsJsonResponse;

  private String name;
  private String email;

  @Before
  public void setUp() {
    JacksonTester.initFields(this, new ObjectMapper());
    this.name = "test123";
    this.email = "test123@test.com";
  }

  @Test
  public void testCreateContact() throws Exception {
    Contact c = new Contact(name, email);
    doReturn(c).when(contactService).createContact(name, email);

    ContactDTO contactDTO = new ContactDTO(name, email);

    MockHttpServletResponse apiResponse = mockMvc.perform(
        post("/contacts").contentType(MediaType.APPLICATION_JSON)
            .content(jsonRequest.write(contactDTO).getJson())).andReturn().getResponse();

    SingleContactResponse expectedResponse = new SingleContactResponse("Success", c);

    assertThat(apiResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(apiResponse.getContentAsString())
        .isEqualTo(jsonResponse.write(expectedResponse).getJson());
  }

  @Test
  public void testUpdateContact() throws Exception {
    String newName = "test123345";

    Contact c = new Contact(name, email);
    Contact expectedUpdatedContact = new Contact(newName, email);

    doReturn(Arrays.asList(c)).when(contactService).searchContact(anyString(), anyString());
    doReturn(expectedUpdatedContact).when(contactService).updateContact(any(Contact.class), anyString(), anyString());

    MockHttpServletResponse apiResponse = mockMvc.perform(
        patch("/contacts/test123@test.com").contentType(MediaType.APPLICATION_JSON)
            .content(jsonRequest.write(new ContactDTO(newName, email)).getJson())).andReturn().getResponse();

    SingleContactResponse expectedResponse = new SingleContactResponse("Success", expectedUpdatedContact);
    assertThat(apiResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(apiResponse.getContentAsString())
        .isEqualTo(jsonResponse.write(expectedResponse).getJson());
  }

  @Test
  public void testSearchContactByName() throws Exception {
    Contact c = new Contact(name, email);
    doReturn(Arrays.asList(c)).when(contactRepository).findByNameRegex(name);
    doReturn(Arrays.asList(c)).when(contactRepository).findByEmailRegex(email);

    MockHttpServletResponse apiResponse = mockMvc.perform(
        get("/contacts/search?name=test123")).andReturn().getResponse();

    MultipleContactsResponse expectedResponse = new MultipleContactsResponse("Success", Arrays.asList(c));
    assertThat(apiResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(apiResponse.getContentAsString())
        .isEqualTo(multipleContactsJsonResponse.write(expectedResponse).getJson());
  }

}

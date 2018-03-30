package com.assignment.contactbook.controller.integrationtest;

/**
 * Created by kousik on 30/03/18.
 */
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.assignment.contactbook.dto.ContactDTO;
import com.assignment.contactbook.repository.ContactRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

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

  @Autowired
  private ContactRepository contactRepository;

  private JacksonTester<ContactDTO> jsonRequest;

  private String name;
  private String email;
  private MockHttpServletResponse apiResponse;

  @Before
  public void setUp() throws Exception {
    contactRepository.deleteAll();

    JacksonTester.initFields(this, new ObjectMapper());
    this.name = "test123";
    this.email = "test123@test.com";

    ContactDTO contactDTO = new ContactDTO(name, email);
    apiResponse = mockMvc.perform(
        post("/contacts").contentType(MediaType.APPLICATION_JSON)
            .content(jsonRequest.write(contactDTO).getJson())).andReturn().getResponse();
  }

  @Test
  public void testCreateContact() throws Exception {
    JSONObject jsonApiResponse = new JSONObject(apiResponse.getContentAsString());
    JSONObject result = new JSONObject(jsonApiResponse.get("result").toString());

    assertThat(apiResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(result.get("name")).isEqualTo(this.name);
    assertThat(result.get("email")).isEqualTo(this.email);
  }

  @Test
  public void testUpdateContact() throws Exception {
    String newName = "test123345";

    MockHttpServletResponse apiResponse = mockMvc.perform(
        patch("/contacts/test123@test.com").contentType(MediaType.APPLICATION_JSON)
            .content(jsonRequest.write(new ContactDTO(newName, email)).getJson())).andReturn().getResponse();

    JSONObject jsonApiResponse = new JSONObject(apiResponse.getContentAsString());
    JSONObject result = new JSONObject(jsonApiResponse.get("result").toString());

    assertThat(apiResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(result.get("name")).isEqualTo(newName);
    assertThat(result.get("email")).isEqualTo(this.email);
  }

  @Test
  public void testUpdateContactWhenContactDoesNotExist() throws Exception {
    String newName = "test123345";

    MockHttpServletResponse apiResponse = mockMvc.perform(
        patch("/contacts/test1234@test.com").contentType(MediaType.APPLICATION_JSON)
            .content(jsonRequest.write(new ContactDTO(newName, email)).getJson())).andReturn().getResponse();

    assertThat(apiResponse.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
  }

  @Test
  public void testSearchContactByName() throws Exception {
    MockHttpServletResponse apiResponse = mockMvc.perform(
        get("/contacts/search?name=test123")).andReturn().getResponse();

    JSONObject jsonApiResponse = new JSONObject(apiResponse.getContentAsString());
    JSONArray items = new JSONArray(jsonApiResponse.get("result").toString());
    JSONObject result = (JSONObject) items.get(0);

    assertThat(apiResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(result.get("name")).isEqualTo(name);
    assertThat(result.get("email")).isEqualTo(email);
  }

  @Test
  public void testSearchContactByEmail() throws Exception {
    MockHttpServletResponse apiResponse = mockMvc.perform(
        get("/contacts/search?email=test123")).andReturn().getResponse();

    JSONObject jsonApiResponse = new JSONObject(apiResponse.getContentAsString());
    JSONArray items = new JSONArray(jsonApiResponse.get("result").toString());
    JSONObject result = (JSONObject) items.get(0);

    assertThat(apiResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(result.get("name")).isEqualTo(name);
    assertThat(result.get("email")).isEqualTo(email);
  }

  @Test
  public void testDeleteContact() throws Exception {
    MockHttpServletResponse apiResponse = mockMvc.perform(
        delete("/contacts/test123@test.com")).andReturn().getResponse();

    assertThat(apiResponse.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
  }
}

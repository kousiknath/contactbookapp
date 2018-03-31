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
import org.springframework.beans.factory.annotation.Value;
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

  @Value("${app.token}")
  private String authToken;

  private JacksonTester<ContactDTO> jsonRequest;

  private String name;
  private String email;

  @Before
  public void setUp() throws Exception {
    contactRepository.deleteAll();

    JacksonTester.initFields(this, new ObjectMapper());
    this.name = "test123";
    this.email = "test123@test.com";
  }

  @Test
  public void testCreateContact() throws Exception {
    ContactDTO contactDTO = new ContactDTO(name, email);
    MockHttpServletResponse apiResponse = mockMvc.perform(
        post("/contacts").contentType(MediaType.APPLICATION_JSON)
            .header("token", authToken)
            .content(jsonRequest.write(contactDTO).getJson())).andReturn().getResponse();

    JSONObject jsonApiResponse = new JSONObject(apiResponse.getContentAsString());
    JSONObject result = new JSONObject(jsonApiResponse.get("result").toString());

    assertThat(apiResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(result.get("name")).isEqualTo(this.name);
    assertThat(result.get("email")).isEqualTo(this.email);
  }

  @Test
  public void testSearchContactByName() throws Exception {
    ContactDTO contactDTO = new ContactDTO(name, email);
    mockMvc.perform(
        post("/contacts").contentType(MediaType.APPLICATION_JSON)
            .header("token", authToken)
            .content(jsonRequest.write(contactDTO).getJson())).andReturn().getResponse();

    MockHttpServletResponse apiResponse = mockMvc.perform(
        get("/contacts/search?name=test").header("token", authToken))
        .andReturn().getResponse();

    JSONObject jsonApiResponse = new JSONObject(apiResponse.getContentAsString());
    JSONArray items = new JSONArray(jsonApiResponse.get("result").toString());
    JSONObject result = (JSONObject) items.get(0);

    assertThat(apiResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(result.get("name")).isEqualTo(name);
    assertThat(result.get("email")).isEqualTo(email);
  }

  @Test
  public void testSearchContactByEmail() throws Exception {
    ContactDTO contactDTO = new ContactDTO(name, email);
    mockMvc.perform(
        post("/contacts").contentType(MediaType.APPLICATION_JSON)
            .header("token", authToken)
            .content(jsonRequest.write(contactDTO).getJson())).andReturn().getResponse();

    MockHttpServletResponse apiResponse = mockMvc.perform(
        get("/contacts/search?email=test123@test.com").header("token", authToken))
        .andReturn().getResponse();

    JSONObject jsonApiResponse = new JSONObject(apiResponse.getContentAsString());
    JSONArray items = new JSONArray(jsonApiResponse.get("result").toString());
    JSONObject result = (JSONObject) items.get(0);

    assertThat(apiResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(result.get("name")).isEqualTo(name);
    assertThat(result.get("email")).isEqualTo(email);
  }

  @Test
  public void testUpdateContact() throws Exception {
    ContactDTO contactDTO = new ContactDTO(name, email);
    MockHttpServletResponse apiResponse = mockMvc.perform(
        post("/contacts").contentType(MediaType.APPLICATION_JSON)
            .header("token", authToken)
            .content(jsonRequest.write(contactDTO).getJson())).andReturn().getResponse();

    JSONObject existingContactApiResponse = new JSONObject(apiResponse.getContentAsString());
    JSONObject existingContactApiResponseResult = new JSONObject(existingContactApiResponse.get("result").toString());
    Long existingContactId = existingContactApiResponseResult.getLong("id");

    String newName = "test123345";

    MockHttpServletResponse apiResponse1 = mockMvc.perform(
        patch("/contacts/" + existingContactId).contentType(MediaType.APPLICATION_JSON)
            .header("token", authToken)
            .content(jsonRequest.write(new ContactDTO(newName, email)).getJson())).andReturn().getResponse();

    assertThat(apiResponse1.getStatus()).isEqualTo(HttpStatus.OK.value());

    JSONObject jsonApiResponse = new JSONObject(apiResponse1.getContentAsString());
    JSONObject result = new JSONObject(jsonApiResponse.get("result").toString());

    assertThat(result.get("name")).isEqualTo(newName);
    assertThat(result.get("email")).isEqualTo(this.email);
  }

  @Test
  public void testUpdateContactWhenContactDoesNotExist() throws Exception {
    ContactDTO contactDTO = new ContactDTO(name, email);
    MockHttpServletResponse apiResponse = mockMvc.perform(
        post("/contacts").contentType(MediaType.APPLICATION_JSON)
            .header("token", authToken)
            .content(jsonRequest.write(contactDTO).getJson())).andReturn().getResponse();

    JSONObject existingContactApiResponse = new JSONObject(apiResponse.getContentAsString());
    JSONObject existingContactApiResponseResult = new JSONObject(existingContactApiResponse.get("result").toString());
    Long existingContactId = existingContactApiResponseResult.getLong("id");

    String newName = "test123345";

    MockHttpServletResponse apiResponse1 = mockMvc.perform(
        patch("/contacts/" + (existingContactId + 100L)).contentType(MediaType.APPLICATION_JSON)
            .header("token", authToken)
            .content(jsonRequest.write(new ContactDTO(newName, email)).getJson())).andReturn().getResponse();

    assertThat(apiResponse1.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
  }

  @Test
  public void testDeleteContact() throws Exception {
    ContactDTO contactDTO = new ContactDTO(name, email);

    MockHttpServletResponse apiResponse = mockMvc.perform(
        post("/contacts").contentType(MediaType.APPLICATION_JSON)
            .header("token", authToken)
            .content(jsonRequest.write(contactDTO).getJson())).andReturn().getResponse();

    JSONObject existingContactApiResponse = new JSONObject(apiResponse.getContentAsString());
    JSONObject existingContactApiResponseResult = new JSONObject(existingContactApiResponse.get("result").toString());
    Long existingContactId = existingContactApiResponseResult.getLong("id");

    MockHttpServletResponse apiResponse1 = mockMvc.perform(
        delete("/contacts/" + existingContactId).header("token", authToken)).andReturn().getResponse();

    assertThat(apiResponse1.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
  }
}

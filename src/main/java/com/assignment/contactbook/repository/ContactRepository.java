package com.assignment.contactbook.repository;

import com.assignment.contactbook.entity.Contact;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * Created by kousik on 28/03/18.
 */
public interface ContactRepository extends MongoRepository<Contact, String> {

  @Query(value = "{'name': {$regex: ?0, $options: 'i'}}")
  List<Contact> findByNameRegex(String nameRegex);

  @Query(value = "{'email': {$regex: ?0, $options: 'i'}}")
  List<Contact> findByEmailRegex(String emailRegex);
}

package com.assignment.contactbook.repository;

import com.assignment.contactbook.entity.Contact;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by kousik on 28/03/18.
 */
public interface ContactRepository extends JpaRepository<Contact, String> {

  List<Contact> findByNameIgnoreCaseContaining(String nameRegex);
  List<Contact> findByEmailIgnoreCaseContaining(String emailRegex);
}

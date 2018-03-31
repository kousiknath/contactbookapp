package com.assignment.contactbook.repository;

import com.assignment.contactbook.entity.Contact;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by kousik on 28/03/18.
 */
public interface ContactRepository extends PagingAndSortingRepository<Contact, Long> {
  Contact findByEmail(String email);
  Page<Contact> findByNameIgnoreCaseContaining(String nameRegex, Pageable pageable);
  Page<Contact> findByEmailIgnoreCaseContaining(String emailRegex, Pageable pageable);
  Contact findById(Long id);
}

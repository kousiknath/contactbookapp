package com.assignment.contactbook.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.springframework.data.annotation.Id;

/**
 * Created by kousik on 28/03/18.
 */
@Entity
public class Contact {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;

  private String name;

  @Column(unique = true)
  private String email;

  public Contact(String name, String email){
    this.name = name;
    this.email = email;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String toString() {
    return String.format(
        "Contact Id: {0}, name: {1}, email: {2}",
        id, name, email
    );
  }

  @Override
  public int hashCode() {
    int hashCode = 0;

    if(name != null)
      hashCode += 31 * name.hashCode();

    if(email != null)
      hashCode += email.hashCode();

    return hashCode;
  }

  @Override
  public boolean equals(Object obj) {
    Contact another = (Contact) obj;

    if(another == null || this.getClass() != another.getClass())
      return false;

    return this.getName().equals(another.getName()) &&
        this.getEmail().equals(another.getEmail()) &&
        this.hashCode() == another.hashCode();
  }
}

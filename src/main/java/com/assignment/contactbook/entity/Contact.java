package com.assignment.contactbook.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by kousik on 28/03/18.
 */
@Entity
public class Contact {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String name;

  @Column(unique = true)
  private String email;

  public Contact(){

  }

  public Contact(String name, String email){
    this.name = name;
    this.email = email;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
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
    int hashCode = 17;

    hashCode = 31 * hashCode + (name != null ? name.hashCode() : 0);
    hashCode = 31 * hashCode + (email != null ? email.hashCode() : 0);
    return hashCode;
  }

  @Override
  public boolean equals(Object obj) {
    if(obj == this)
      return true;

    if(obj == null || this.getClass() != obj.getClass())
      return false;

    Contact another = (Contact) obj;

    return this.getName().equals(another.getName()) &&
        this.getEmail().equals(another.getEmail()) &&
        this.hashCode() == another.hashCode();
  }
}

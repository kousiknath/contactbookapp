package com.assignment.contactbook.utils;

import org.springframework.stereotype.Component;

/**
 * Created by kousik on 29/03/18.
 */
@Component
public class Utility {

  public boolean isValidInput(String input) {
    return (input != null && input.trim().length() > 0);
  }

}

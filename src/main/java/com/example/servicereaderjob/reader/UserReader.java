package com.example.servicereaderjob.reader;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import com.example.servicereaderjob.domain.User;

@Component
public class UserReader implements ItemReader<User> {

  @Override
  public User read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
    //TODO: Implementar leitura
    return null;
  }

}

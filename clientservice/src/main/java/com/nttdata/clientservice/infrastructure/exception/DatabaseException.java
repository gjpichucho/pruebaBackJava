package com.nttdata.clientservice.infrastructure.exception;


public class DatabaseException extends Exception {

  public DatabaseException(Throwable throwable) {
    super("Database error, check connection or syntax", throwable);
  }
}

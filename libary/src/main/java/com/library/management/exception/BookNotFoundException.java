package com.library.management.exception;

public class BookNotFoundException extends LibraryException {
    public BookNotFoundException(String bookId) {
        super("Book with ID " + bookId + " not found");
    }
}
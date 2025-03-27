package com.library.management.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.library.management.exception.BookNotFoundException;
import com.library.management.exception.ValidationException;
import com.library.management.model.Book;
import com.library.management.repository.BookRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LibraryServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private LibraryService libraryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddBookSuccess() {
        Book book = new Book("1", "Test Book", "Author", "Fiction", true);
        when(bookRepository.existsById("1")).thenReturn(false);
        when(bookRepository.save(book)).thenReturn(book);

        Book result = libraryService.addBook(book);
        assertEquals(book, result);
        verify(bookRepository).save(book);
    }

    @Test
    void testAddBookDuplicateId() {
        Book book = new Book("1", "Test Book", "Author", "Fiction", true);
        when(bookRepository.existsById("1")).thenReturn(true);

        assertThrows(ValidationException.class, () -> libraryService.addBook(book));
    }

    @Test
    void testAddBookNull() {
        assertThrows(ValidationException.class, () -> libraryService.addBook(null));
    }

    @Test
    void testGetAllBooks() {
        List<Book> books = Arrays.asList(
                new Book("1", "Book 1", "Author 1", "Fiction", true),
                new Book("2", "Book 2", "Author 2", "Non-fiction", false)
        );
        when(bookRepository.findAll()).thenReturn(books);

        List<String> result = libraryService.getAllBooks();
        assertEquals(2, result.size());
    }

    @Test
    void testSearchBooksInvalidTerm() {
        assertThrows(ValidationException.class, () -> libraryService.searchBooks(""));
    }

    @Test
    void testDeleteBookNotFound() {
        when(bookRepository.existsById("1")).thenReturn(false);
        assertThrows(BookNotFoundException.class, () -> libraryService.deleteBook("1"));
    }
}
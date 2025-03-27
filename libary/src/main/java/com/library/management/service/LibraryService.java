package com.library.management.service;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.library.management.exception.BookNotFoundException;
import com.library.management.exception.LibraryException;
import com.library.management.exception.ValidationException;
import com.library.management.model.Book;
import com.library.management.repository.BookRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibraryService {

    private final BookRepository bookRepository;

    public LibraryService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book addBook(Book book) {
        try {
            validateBook(book);
            if (bookRepository.existsById(book.getBookId())) {
                throw new ValidationException("Book with ID " + book.getBookId() + " already exists");
            }
            return bookRepository.save(book);
        } catch (DataAccessException e) {
            throw new LibraryException("Failed to add book due to database error: " + e.getMessage());
        }
    }

    public List<String> getAllBooks() {
        try {
            return bookRepository.findAll()
                    .stream()
                    .map(Book::toString)
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            throw new LibraryException("Failed to retrieve books: " + e.getMessage());
        }
    }

    public List<String> searchBooks(String searchTerm) {
        try {
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                throw new ValidationException("Search term cannot be empty");
            }
            String term = searchTerm.toLowerCase();
            return bookRepository.findAll()
                    .stream()
                    .filter(book -> book.getBookId().toLowerCase().contains(term) ||
                            book.getTitle().toLowerCase().contains(term))
                    .map(Book::toString)
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            throw new LibraryException("Failed to search books: " + e.getMessage());
        }
    }

    public Book updateBook(String bookId, Book updatedBook) {
        try {
            Book existingBook = findBookById(bookId);
            validateBook(updatedBook);
            updateBookFields(existingBook, updatedBook);
            return bookRepository.save(existingBook);
        } catch (DataAccessException e) {
            throw new LibraryException("Failed to update book: " + e.getMessage());
        }
    }

    public void deleteBook(String bookId) {
        try {
            if (!bookRepository.existsById(bookId)) {
                throw new BookNotFoundException(bookId);
            }
            bookRepository.deleteById(bookId);
        } catch (DataAccessException e) {
            throw new LibraryException("Failed to delete book: " + e.getMessage());
        }
    }

    private void validateBook(Book book) {
        if (book == null) {
            throw new ValidationException("Book cannot be null");
        }
        if (book.getBookId() == null || book.getBookId().trim().isEmpty()) {
            throw new ValidationException("Book ID cannot be null or empty");
        }
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new ValidationException("Title cannot be null or empty");
        }
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new ValidationException("Author cannot be null or empty");
        }
    }

    private Book findBookById(String bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
    }

    private void updateBookFields(Book existing, Book updated) {
        if (updated.getTitle() != null) existing.setTitle(updated.getTitle());
        if (updated.getAuthor() != null) existing.setAuthor(updated.getAuthor());
        if (updated.getGenre() != null) existing.setGenre(updated.getGenre());
        existing.setAvailable(updated.isAvailable());
    }
}
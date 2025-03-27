// src/main/java/com/library/management/controller/LibraryController.java
package com.library.management.controller;

import com.library.management.model.Book;
import com.library.management.service.LibraryService;
import jakarta.validation.Valid;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class LibraryController {

    private final LibraryService libraryService;
    private final ApplicationContext applicationContext;

    public LibraryController(LibraryService libraryService, ApplicationContext applicationContext) {
        this.libraryService = libraryService;
        this.applicationContext = applicationContext;
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {
        return ResponseEntity.ok(libraryService.addBook(book));
    }

    @GetMapping
    public ResponseEntity<List<String>> getAllBooks() {
        return ResponseEntity.ok(libraryService.getAllBooks());
    }

    @GetMapping("/search")
    public ResponseEntity<List<String>> searchBooks(@RequestParam("term") String term) {
        return ResponseEntity.ok(libraryService.searchBooks(term));
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<Book> updateBook(@PathVariable("bookId") String bookId, @Valid @RequestBody Book book) {
        return ResponseEntity.ok(libraryService.updateBook(bookId, book));
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable("bookId") String bookId) {
        libraryService.deleteBook(bookId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/shutdown")
    public ResponseEntity<String> shutdownSystem() {
        try {
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    SpringApplication.exit(applicationContext, () -> 0);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
            return ResponseEntity.ok("System shutting down...");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to shutdown system: " + e.getMessage());
        }
    }
}
package com.library.management.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.library.management.model.Book;
import com.library.management.service.LibraryService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class LibraryControllerTest {

    @Mock
    private LibraryService libraryService;

    @Mock
    private ApplicationContext applicationContext;

    @InjectMocks
    private LibraryController libraryController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(libraryController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testAddBookSuccess() throws Exception {
        Book book = new Book("1", "Test Book", "Author", "Fiction", true);
        when(libraryService.addBook(any(Book.class))).thenReturn(book);

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"bookId\":\"1\",\"title\":\"Test Book\",\"author\":\"Author\",\"genre\":\"Fiction\",\"isAvailable\":true}"))
                .andExpect(status().isOk());
    }

    @Test
    void testAddBookInvalidInput() throws Exception {
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"bookId\":\"\",\"title\":\"\",\"author\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testShutdownSystem() throws Exception {
        mockMvc.perform(post("/api/books/shutdown")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("System shutting down..."));
    }
}
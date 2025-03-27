package com.library.management.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Book {
    @Id
    @NotBlank(message = "Book ID is mandatory")
    private String bookId;

    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Author is mandatory")
    private String author;

    private String genre;
    private boolean isAvailable;

    

    

    @Override
    public String toString() {
        String status = isAvailable ? "Available" : "Checked Out";
        return String.format("ID: %s, Title: %s, Author: %s, Genre: %s, Status: %s",
                bookId, title, author, genre, status);
    }
}
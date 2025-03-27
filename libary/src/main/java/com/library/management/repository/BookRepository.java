package com.library.management.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.library.management.model.Book;

public interface BookRepository extends JpaRepository<Book, String> {
}


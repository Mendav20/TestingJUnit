package com.test.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.test.model.Book;

@Service("bookService")
public class BookServiceImpl implements BookService{

	@Override
	public List<Book> getAllBooks() {
		
		return Collections.emptyList();
	}

	@Override
	public Book addBook(Book book) {
		
		return book;
	}

	@Override
	public Book getBookByISBN(Long isbn) {
	
		return null;
	}

	@Override
	public void deleteBook(Long isbn) {
		
		
	}

}

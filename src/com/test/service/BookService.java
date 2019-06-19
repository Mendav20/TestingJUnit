package com.test.service;

import java.util.List;

import com.test.model.Book;

	
//@Transactional
public interface BookService {
	
	List<Book> getAllBooks();
	
	Book addBook (Book book);
	
	Book getBookByISBN(Long isbn);
	
	void deleteBook(Long isbn);

}

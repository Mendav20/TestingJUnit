package com.test.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.test.model.Book;
import com.test.service.BookService;

@Controller
public class MainController {
	
	@Autowired
	private BookService bookService;

	@RequestMapping(value = "/books", method = RequestMethod.GET)
	public ResponseEntity<List<Book>> getAllBooks() {
		
		List<Book> listBook = bookService.getAllBooks();		 
		return new ResponseEntity<>(listBook,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/books/{isbn}", method = RequestMethod.GET)
	public ResponseEntity<Book> getBook(@PathVariable("isbn") Long isbnCode){
		Book book = bookService.getBookByISBN(isbnCode);
		if(book == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else {
			return new ResponseEntity<>(book, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/books",method = RequestMethod.POST)
	public ResponseEntity<Book> addBook(@RequestBody Book book){
		book = bookService.addBook(book);
		return new ResponseEntity<>(book, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/books/{isbn}", method = RequestMethod.DELETE)
	public ResponseEntity<Book> deleteBook(@PathVariable("isbn") Long isbnCode) {
		Book book = bookService.getBookByISBN(isbnCode);
		bookService.deleteBook(isbnCode);
		return new ResponseEntity<>(book,HttpStatus.OK);		
	}
	
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public  String gethello(){
		return "redirect:/hel";
	}
	
	@RequestMapping(value = "/hel",method = RequestMethod.GET)
	public ModelAndView getworld() {
		return new ModelAndView("/hello");
	}
	

}

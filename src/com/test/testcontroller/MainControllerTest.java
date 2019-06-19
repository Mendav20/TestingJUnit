package com.test.testcontroller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.configuration.MvcConfiguration;
import com.test.controller.MainController;
import com.test.model.Book;
import com.test.service.BookService;


@SuppressWarnings("deprecation")
@RunWith(SpringRunner.class)
@WebMvcTest(MainController.class)
@ContextConfiguration(classes = MvcConfiguration.class)
public class MainControllerTest {
	
	@Before
	public void setup() {
		MainController controller = new MainController();
		this.mock = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Autowired
	private MockMvc mockMvc;
	private MockMvc mock;
	
	@MockBean
	private BookService bookService;

	
	@Autowired
	private ObjectMapper mapper;
	

	private final Book effectiveJavaBook = Book.builder().tittle("effective Java").author("Joshua Blonch")
			.categories("Java", "Programing").isbn(1234567890L).build();

	private final Book algorithmsBook = Book.builder().tittle("algorith").author("Pattrick").categories("Prolog")
			.isbn(1234567891L).build();

	private final Book prologBook = Book.builder().tittle("Learn prolog").author("Pattrick sand").categories("Prolog")
			.isbn(1234567892L).build();

	private final Book scalaBook = Book.builder().tittle("scala").author("Martin odern").categories("scala")
			.isbn(1234567893L).build();
	
	
	private static class Behavior{
		BookService bookService;
		
		public static Behavior set(BookService bookService) {
			Behavior behavior = new Behavior();
			behavior.bookService = bookService;
			return behavior;
		}
		public Behavior hasNoBooks() {
			when(bookService.getAllBooks()) // when: Esto básicamente solo llama al método probado. Por lo tanto, siempre se puede ver fácilmente lo que se prueba con una determinada prueba JUnit
			.thenReturn(Collections.emptyList());// then: en esta sección, se implementan las afirmaciones sobre los resultados del paso de ejecución
			when(bookService.getBookByISBN(anyLong())).thenReturn(null);
			return this;
		}
		
		public Behavior returnSame() {
			when(bookService.addBook(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
			return this;
		}
		public Behavior returnBooks(Book...books) {
			when(bookService.getAllBooks()).thenReturn(Arrays.asList(books));
			for(Book book : books) {
				when(bookService.getBookByISBN(book.getIsbnCode())).thenReturn(book);
				when(bookService.addBook(book)).thenReturn(book);
			}
			return this;
		}
	}
	
	
	@Test
	public void testhello()throws Exception{
		mockMvc
			.perform(get("/hel"))
			.andExpect(status().isOk())
			.andExpect(view().name("/hello"))
			.andExpect(forwardedUrl("/WEB-INF/views/hello.jsp"));
	}

	@Test 
	public void testGetAllBooks_NoBooks() throws Exception {
		Behavior.set(bookService).hasNoBooks();
		mockMvc
			.perform(get("/books"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(content().json("[]"));
		verify(bookService, times(1)).getAllBooks();
	}

	@Test
	public void getAllBooks_AlLeastOneBook()throws Exception{
		Behavior.set(bookService).returnBooks(effectiveJavaBook);
		String bookContent = mapper.writeValueAsString(effectiveJavaBook);
		mockMvc
			.perform(get("/books"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(content().json(String.format("[%s]", bookContent)));
		verify(bookService,times(1)).getAllBooks();
	}
	
	@Test
	public void getAllBooks_MoreThanOneBook()throws Exception{
		Behavior.set(bookService).returnBooks(effectiveJavaBook,scalaBook,prologBook,algorithmsBook);
		List<Book> books = Arrays.asList(effectiveJavaBook, scalaBook, prologBook, algorithmsBook);
		String booksContent = mapper.writeValueAsString(books);
		mockMvc
			.perform(get("/books"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(content().json(booksContent));
		verify(bookService, times(1)).getAllBooks();
	}
	
	@Test
	public void addBook_Positive() throws Exception{
		Behavior.set(bookService).returnSame();
		String bookContent = mapper.writeValueAsString(effectiveJavaBook);
		mockMvc
			.perform(post("/books") // se define el path por el cual se accede al controlador y el tipo de peticion
					.content(bookContent) // se asigna el contenido que se ha de enviar a la solicitud hecha por el controlador
					.contentType(MediaType.APPLICATION_JSON_UTF8)) // se deine el tipo de dato que se ha de enviar al controlador
			.andExpect(status().isCreated()) // aqui se hace la primer prueba en la que se determina si el .perform se creo correctamente
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)) // aqui se verifica que el contenido sea en formato json
			.andExpect(content().json(bookContent)); // se espera informacion en formato json
		verify(bookService, times(1)).addBook(Matchers.refEq(effectiveJavaBook));	 // se usa junto con times() para probar un determinado numero de veces y el metodo que queremos testear
	}
	
	@Test
	public void getBook_positive()throws Exception{
		Behavior.set(bookService).returnBooks(effectiveJavaBook);
		String bookContent = mapper.writeValueAsString(effectiveJavaBook);
		mockMvc
			.perform(get("/books/" + effectiveJavaBook.getIsbnCode()))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(content().json(bookContent));
		verify(bookService, times(1)).getBookByISBN(effectiveJavaBook.getIsbnCode());
	}
	
	@Test
	public void getBook_NoBookFound()throws Exception{
		Behavior.set(bookService).hasNoBooks();
		mockMvc
			.perform(get("/books/1234567899"))
			.andExpect(status().isNotFound());
		verify(bookService, times(1)).getBookByISBN(anyLong());
	}
	
	@Test
	public void deleteBook()throws Exception{
		mockMvc 
			.perform(delete("/books/1234567899"))
			.andExpect(status().isOk());
		verify(bookService, times(1)).deleteBook(anyLong());
	}
	
}

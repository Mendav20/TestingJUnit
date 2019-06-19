package com.test.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Book implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5492455099750047332L;

	private String tittle;
	private String author;
	private int publishedYear;
	private List<String> categories;
	private String publisher;
	private Long isbnCode;
	
	
	public String getTittle() {
		return tittle;
	}

	public void setTittle(String tittle) {
		this.tittle = tittle;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getPublishedYear() {
		return publishedYear;
	}

	public void setPublishedYear(int publishedYear) {
		this.publishedYear = publishedYear;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public Long getIsbnCode() {
		return isbnCode;
	}

	public void setIsbnCode(Long isbnCode) {
		this.isbnCode = isbnCode;
	}

	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder{
		
		private Book book = new Book();
		
		public Builder tittle(String tittle) {
			book.setTittle(tittle);
			return this;
		}
		public Builder author(String author) {
			book.setAuthor(author);
			return this;
		}
		
		public Builder isbn(Long isbnCode) {
			book.setIsbnCode(isbnCode);
			return this;
		}
		
		public Builder yearOfPublishing(int year) {
			book.setPublishedYear(year);
			return this;
		}
		
		public Builder categories(String...category) {
			book.setCategories(Arrays.asList(category));
			return this;
		}
		
		public Builder publisher(String publisher) {
			book.setPublisher(publisher);
			return this;
		}
		public Book build() {
			return this.book;
		}
	}
}

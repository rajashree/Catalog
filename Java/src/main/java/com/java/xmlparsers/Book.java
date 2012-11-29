package com.java.xmlparsers;

public class Book {

	private String title;
	
	private String author;
	
	private String subject;

	public Book(String subject) {
		this.subject = subject;
	}
	public Book(String title, String author, String subject) {
		this.title = title;
		this.author = author;
		this.subject = subject;
	}
	
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(" { Book Details --");
		sb.append("Title:" + getTitle());
		sb.append(", ");
		sb.append("Author:" + getAuthor());
		sb.append(", ");
		sb.append("Subject:" + getSubject());
		sb.append(". } \n");
		
		return sb.toString();
	}
}

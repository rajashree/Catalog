package com.java.xmlparsers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

//For jdk1.5 with built in xerces parser
//import com.sun.org.apache.xml.internal.serialize.OutputFormat;
//import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

//For JDK 1.3 or JDK 1.4  with xerces 2.7.1
import org.apache.xml.serialize.XMLSerializer;
import org.apache.xml.serialize.OutputFormat;


public class XMLCreatorExample {

	//No generics
	List myData;
	Document dom;

	public XMLCreatorExample() {
		//create a list to hold the data
		myData = new ArrayList();

		//initialize the list
		loadData();

		//Get a DOM object
		createDocument();
	}


	public void runExample(){
		System.out.println("Started .. ");
		createDOMTree();
		printToFile();
		System.out.println("Generated file successfully.");
	}

	/**
	 * Add a list of books to the list
	 * In a production system you might populate the list from a DB
	 */
	private void loadData(){
		myData.add(new Book("Head First Java", "Kathy Sierra .. etc","Java 1.5"));
		myData.add(new Book("Head First Design Patterns", "Kathy Sierra .. etc","Java Architect"));
	}

	/**
	 * Using JAXP in implementation independent manner create a document object
	 * using which we create a xml tree in memory
	 */
	private void createDocument() {

		//get an instance of factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
		//get an instance of builder
		DocumentBuilder db = dbf.newDocumentBuilder();

		//create an instance of DOM
		dom = db.newDocument();

		}catch(ParserConfigurationException pce) {
			//dump it
			System.out.println("Error while trying to instantiate DocumentBuilder " + pce);
			System.exit(1);
		}

	}

	/**
	 * The real workhorse which creates the XML structure
	 */
	private void createDOMTree(){

		//create the root element <Books>
		Element rootEle = dom.createElement("Books");
		dom.appendChild(rootEle);

		//No enhanced for
		Iterator it  = myData.iterator();
		while(it.hasNext()) {
			Book b = (Book)it.next();
			//For each Book object  create <Book> element and attach it to root
			Element bookEle = createBookElement(b);
			rootEle.appendChild(bookEle);
		}

	}

	/**
	 * Helper method which creates a XML element <Book>
	 * @param b The book for which we need to create an xml representation
	 * @return XML element snippet representing a book
	 */
	private Element createBookElement(Book b){

		Element bookEle = dom.createElement("Book");
		bookEle.setAttribute("Subject", b.getSubject());

		//create author element and author text node and attach it to bookElement
		Element authEle = dom.createElement("Author");
		Text authText = dom.createTextNode(b.getAuthor());
		authEle.appendChild(authText);
		bookEle.appendChild(authEle);

		//create title element and title text node and attach it to bookElement
		Element titleEle = dom.createElement("Title");
		Text titleText = dom.createTextNode(b.getTitle());
		titleEle.appendChild(titleText);
		bookEle.appendChild(titleEle);

		return bookEle;

	}

	/**
	 * This method uses Xerces specific classes
	 * prints the XML document to file.
     */
	private void printToFile(){

		try
		{
			//print
			OutputFormat format = new OutputFormat(dom);
			format.setIndenting(true);

			/*//to generate output to console use this serializer
			XMLSerializer serializer = new XMLSerializer(System.out, format);
*/

			//to generate a file output use fileoutputstream instead of system.out
			XMLSerializer serializer = new XMLSerializer(
			new FileOutputStream(new File("book.xml")), format);

			serializer.serialize(dom);

		} catch(IOException ie) {
		    ie.printStackTrace();
		}
	}


	public static void main(String args[]) {

		//create an instance
		XMLCreatorExample xce = new XMLCreatorExample();

		//run the example
		xce.runExample();
	}
}


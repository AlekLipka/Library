import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UnitTests {

	@Test
	public final void testAddBook() {
		Library library = new Library();
		library.addBook("Lord of The Rings The Two Towers", 2002, "J.R.R. Tolkien");
		library.addBook("Pan Tadeusz", 2004, "Adam Mickiewicz");
		library.addBook("Harry Potter and the Goblet of Fire", 2011, "J.K. Rowling");
		
		List<Library.Book> bookList = library.getBookList();
		
		assertEquals("Lord of The Rings The Two Towers" , bookList.get(0).getTitle());
		assertEquals(2002 , bookList.get(0).getYear());
		assertEquals("J.R.R. Tolkien" , bookList.get(0).getAuthor());
		
		assertEquals("Pan Tadeusz" , bookList.get(1).getTitle());
		assertEquals(2004 , bookList.get(1).getYear());
		assertEquals("Adam Mickiewicz" , bookList.get(1).getAuthor());
		
		assertEquals("Harry Potter and the Goblet of Fire" , bookList.get(2).getTitle());
		assertEquals(2011 , bookList.get(2).getYear());
		assertEquals("J.K. Rowling" , bookList.get(2).getAuthor());
		
		//Checking wheter IDs are different
		assertNotEquals(bookList.get(0).getID(), bookList.get(1).getID());
		assertNotEquals(bookList.get(1).getID(), bookList.get(2).getID());
		assertNotEquals(bookList.get(0).getID(), bookList.get(2).getID());
	}

	@Test
	public final void testDeleteBook() {
		Library library = new Library();
		library.addBook("Lord of The Rings The Two Towers", 2002, "J.R.R. Tolkien");
		library.addBook("Pan Tadeusz", 2004, "Adam Mickiewicz");
		library.addBook("Harry Potter and the Goblet of Fire", 2011, "J.K. Rowling");
		
		List<Library.Book> bookList = library.getBookList();
		UUID idToBeDeleted = bookList.get(1).getID();
	
		assertTrue(library.deleteBook(idToBeDeleted)); //Deleting book that exist
		assertFalse(bookList.stream().anyMatch(o -> o.getID().equals(idToBeDeleted))); //Checking if the book was deleted from library
		assertEquals(2, bookList.size());
		assertFalse(library.deleteBook(UUID.randomUUID())); //Trying to delete book that doesn't exist
	}

	@Test
	public final void testListAllBooks() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testSearch() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testLendBook() {
		Library library = new Library();
		library.addBook("Lord of The Rings The Two Towers", 2002, "J.R.R. Tolkien");
		library.addBook("Pan Tadeusz", 2004, "Adam Mickiewicz");
		library.addBook("Harry Potter and the Goblet of Fire", 2011, "J.K. Rowling");
		
		List<Library.Book> bookList = library.getBookList();
		
		UUID id = bookList.get(0).getID();
		library.lendBook(id, "Aleksander Wielki");
		
		UUID id1 = bookList.get(1).getID();
		assertEquals(0, library.lendBook(id1, "Kasia Mroz")); //lending book
		assertEquals(1, library.lendBook(id1, "Maciej Wrzenczek")); //book already lent
		assertEquals(-1, library.lendBook(UUID.randomUUID(), "Maciej Wrzenczek")); //book id wrong
		
		//Checking wheter lent books are available
		assertFalse(bookList.get(0).isAvailable());
		assertFalse(bookList.get(1).isAvailable());
		assertTrue(bookList.get(2).isAvailable());
		
		//Checking who was the book lent to
		assertEquals("Aleksander Wielki", bookList.get(0).getBorrower());
		assertEquals("Kasia Mroz", bookList.get(1).getBorrower());
		assertEquals(null, bookList.get(2).getBorrower());
	}
	
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	
	@Before
	public void setUpStreams() {
		System.setOut(new PrintStream(outContent));
	}
	@After
	public void cleanUpStreams(){
		System.setOut(null);
	}

	@Test
	public final void testViewBooksDetails() {
		Library library = new Library();
		library.addBook("Lord of The Rings The Two Towers", 2002, "J.R.R. Tolkien");
		library.addBook("Pan Tadeusz", 2004, "Adam Mickiewicz");
		library.addBook("Harry Potter and the Goblet of Fire", 2011, "J.K. Rowling");
		
		List<Library.Book> bookList = library.getBookList();
		
		UUID id = bookList.get(0).getID();
		library.lendBook(id, "Aleksander Wielki");
		
		//First way: Checking lent book via system.output
		String string = library.viewBooksDetails(id);
		System.out.print(string);		
		assertEquals("Lord of The Rings The Two Towers	2002	J.R.R. Tolkien	lent to: Aleksander Wielki", outContent.toString());
		
		//Second way: Checking available book via returned string
		UUID id1 = bookList.get(1).getID();
		String string1 = library.viewBooksDetails(id1);
		assertEquals("Pan Tadeusz	2004	Adam Mickiewicz	available", string1);
	}

}

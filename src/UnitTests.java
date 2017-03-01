import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UnitTests {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private Library library = new Library();
	private List<Book> bookList;
	
	@Before
	public void setUpStreams() {
		System.setOut(new PrintStream(outContent));
		library.addBook("Lord of The Rings The Two Towers", 2002, "J.R.R. Tolkien");
		library.addBook("Pan Tadeusz", 2004, "Adam Mickiewicz");
		library.addBook("Harry Potter and the Goblet of Fire", 2011, "J.K. Rowling");
		bookList = library.getBookList();
	}
	@After
	public void cleanUpStreams(){
		System.setOut(null);
	}
	
	@Test
	public final void testAddBook() {
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
		UUID idToBeDeleted = bookList.get(1).getID();
	
		assertTrue(library.deleteBook(idToBeDeleted)); //Deleting book that exist
		assertFalse(bookList.stream().anyMatch(o -> o.getID().equals(idToBeDeleted))); //Checking if the book was deleted from library
		assertEquals(2, bookList.size());
		assertFalse(library.deleteBook(UUID.randomUUID())); //Trying to delete book that doesn't exist
	}

	@Test
	public final void testListAllBooks() {
		library.addBook("Lord of The Rings The Two Towers", 2002, "J.R.R. Tolkien"); //adding the same book to library
		library.addBook("Lord of The Rings The Two Towers", 2002, "J.R.R. Tolkien"); //adding the same book to library
		
		UUID id = bookList.get(0).getID();
		library.lendBook(id, "Baba Jaga");
		
		String string[] = library.listAllBooks();
		
		assertEquals(5, bookList.size());
		assertEquals(3, string.length); //checking if the result is distinct
		
		for(String s : string)
			System.out.print(s + " ");		
		assertEquals("Lord of The Rings The Two Towers	2002	J.R.R. Tolkien	available copies: 2	lent copies: 1 Pan Tadeusz	2004	Adam Mickiewicz	available copies: 1 Harry Potter and the Goblet of Fire	2011	J.K. Rowling	available copies: 1 ", outContent.toString()); //checking amount of available and lent copies
	}

	@Test
	public final void testSearch() {	
		assertEquals("J.R.R. Tolkien", library.search("Lord of The Rings The Two Towers", 2002, "J.R.R. Tolkien").get(0).getAuthor()); //search by Title AND Year AND Author
		assertEquals("J.R.R. Tolkien", library.search("Lord of The Rings The Two Towers", 2002, null).get(0).getAuthor()); //search by Title AND Year
		assertEquals("J.R.R. Tolkien", library.search("Lord of The Rings The Two Towers", 0, null).get(0).getAuthor()); //search by Title
		assertEquals("J.R.R. Tolkien", library.search(null, 2002, null).get(0).getAuthor()); //search by Year
		assertEquals(2002, library.search(null, 0, "J.R.R. Tolkien").get(0).getYear()); //search by Author
		assertEquals("Lord of The Rings The Two Towers", library.search(null, 2002, "J.R.R. Tolkien").get(0).getTitle()); //search by Year AND Author
		assertEquals(2002, library.search("Lord of The Rings The Two Towers", 0, "J.R.R. Tolkien").get(0).getYear()); //search by Title AND Author
		
		library.addBook("Harry Potter and the Goblet of Fire", 2011, "J.K. Rowling");
		assertEquals(2, library.search(null, 0, "J.K. Rowling").size()); //checking if both positions were found
	}

	@Test
	public final void testLendBook() {
		UUID id = bookList.get(0).getID();
		library.lendBook(id, "Aleksander Wielki");
		
		UUID id1 = bookList.get(1).getID();
		assertEquals(0, library.lendBook(id1, "Kasia Mroz")); //lending book
		assertEquals(1, library.lendBook(id1, "Maciej Wrzenczek")); //book already lent
		assertEquals(-1, library.lendBook(UUID.randomUUID(), "Maciej Wrzenczek")); //book id wrong
		
		//Checking whether lent books are available
		assertFalse(bookList.get(0).isAvailable());
		assertFalse(bookList.get(1).isAvailable());
		assertTrue(bookList.get(2).isAvailable());
		
		//Checking who was the book lent to
		assertEquals("Aleksander Wielki", bookList.get(0).getBorrower());
		assertEquals("Kasia Mroz", bookList.get(1).getBorrower());
		assertEquals(null, bookList.get(2).getBorrower());
	}

	@Test
	public final void testViewBooksDetails() {
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

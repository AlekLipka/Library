import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Library {
	private ArrayList<Book> library;
	
	public Library(){
		library = new ArrayList<Book>();
	}
	
	public List<Book> getBookList(){ return library; }
	
	public UUID getUniqueID(){ 
		while(true){
			UUID id = UUID.randomUUID();
			//the chance of getting the same id using UUID is extremely low: 1 to hundreds of billions, but still can happen, so we need to check it
			if (!library.stream().anyMatch(o -> o.getID().equals(id)))
				return id;
		}
	}
	
	public int getCountOfAvailableCopies(String givenTitle){
		return library.stream().filter(o -> o.getTitle().equals(givenTitle)).filter(o -> o.isAvailable() == true).collect(Collectors.toList()).size();
	}
	public int getCountOfLentCopies(String givenTitle){
		return library.stream().filter(o -> o.getTitle().equals(givenTitle)).filter(o -> o.isAvailable() == false).collect(Collectors.toList()).size();
	}
	
	public void addBook(String newTitle, int newYear, String newAuthor){
		Book book = new Book(newTitle, newYear, newAuthor);		
		UUID uniqueID = getUniqueID();
		book.setID(uniqueID);
		library.add(book);
	}
	
	public boolean deleteBook(UUID id){
		if (library.stream().anyMatch(o -> o.getID().equals(id))){
			Book book = library.stream().filter(o -> o.getID().equals(id)).findFirst().get();
			
			//Checking whether the book is lent
			if (!book.isAvailable())
				return false;
			
			library.remove(book);
			return true;
		}
		else
			return false;		
	}
	
	public static <T> Predicate<T> distinctByKey(Function<? super T,Object> keyExtractor) {
	    Map<Object,Boolean> seen = new ConcurrentHashMap<>();
	    return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
	
	public String[] listAllBooks(){
		List<Book> distinctList = library.stream().filter(distinctByKey(o -> o.getTitle())).collect(Collectors.toList());
			
		String[] allBooks = new String[distinctList.size()];
		int i=0;
		
		for(Book element : distinctList){
			allBooks[i] = element.getTitle()+"\t"+element.getYear()+"\t"+element.getAuthor()+"\tavailable copies: " + getCountOfAvailableCopies(element.getTitle());
			if (getCountOfLentCopies(element.getTitle()) != 0)
				allBooks[i] += "\tlent copies: " + getCountOfLentCopies(element.getTitle());
			i++;
		}
		return allBooks;
	}
	
	public List<Book> search(String searchTitle, int searchYear, String searchAuthor){	
		List<Book> foundBooks = new ArrayList<Book>();
	
		if (searchTitle != null && searchYear != 0 && searchAuthor != null)
			foundBooks = library.stream().filter(o -> o.getTitle().equals(searchTitle)).filter(o -> o.getYear() == searchYear).filter(o -> o.getAuthor().equals(searchAuthor)).collect(Collectors.toList());
		else if (searchTitle == null && searchYear == 0)
			foundBooks = library.stream().filter(o -> o.getAuthor().equals(searchAuthor)).collect(Collectors.toList());
		else if (searchTitle == null && searchAuthor == null)
			foundBooks = library.stream().filter(o -> o.getYear() == searchYear).collect(Collectors.toList());
		else if (searchYear == 0 && searchAuthor == null)
			foundBooks = library.stream().filter(o -> o.getTitle() == searchTitle).collect(Collectors.toList());
		else if (searchTitle == null)
			foundBooks = library.stream().filter(o -> o.getYear() == searchYear).filter(o -> o.getAuthor().equals(searchAuthor)).collect(Collectors.toList());
		else if (searchYear == 0)
			foundBooks = library.stream().filter(o -> o.getTitle() == searchTitle).filter(o -> o.getAuthor().equals(searchAuthor)).collect(Collectors.toList());
		else if (searchAuthor == null)
			foundBooks = library.stream().filter(o -> o.getTitle() == searchTitle).filter(o -> o.getYear() == searchYear).collect(Collectors.toList());
		
		return foundBooks;
	}
	
	public int lendBook(UUID id, String person){
		if (library.stream().anyMatch(o -> o.getID().equals(id))){
			Book book = library.stream().filter(o -> o.getID().equals(id)).findFirst().get();
			if (book.isAvailable()){
				book.lendBook(person);
				return 0;
			}
			else
				return 1;			
		}
		else
			return -1;
	}
	
	public String viewBooksDetails(UUID id){
		if (library.stream().anyMatch(o -> o.getID().equals(id))){
			Book book = library.stream().filter(o -> o.getID().equals(id)).findFirst().get();
			String bookDetails = book.getTitle() + "\t" + book.getYear() + "\t" + book.getAuthor() + "\t";
			if (book.isAvailable())
				bookDetails += "available";
			else
				bookDetails += "lent to: " + book.getBorrower();
			return bookDetails;
		}
		else
			return null;
	}
}

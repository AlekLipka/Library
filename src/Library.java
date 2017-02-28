import java.util.*;

public class Library {
	private ArrayList<Book> library;
	
	public Library(){
		library = new ArrayList<Book>();
	}
	
	public List<Book> getBookList(){ return library; }
	
	class Book{
		private UUID id;
		private String title;
		private int year;
		private String author;
		private Availibility availbility; 
					
		public Book(String newTitle, int newYear, String newAuthor){
			title = newTitle;
			year = newYear;
			author = newAuthor;
			availbility = new Availibility();
		}
		
		public void setUniqueID(){ 
			while(true){
				id = UUID.randomUUID();
				//the chance of getting the same id using UUID is extremely low: 1 to hundreds of billions, but still can happen, so we need to check it
				if (!library.stream().anyMatch(o -> o.getID().equals(id)))
					break;
			}
		}
		public UUID getID(){ return id; }
		public String getTitle(){ return title; }
		public int getYear(){ return year; }
		public String getAuthor(){ return author; }
		
		public boolean isAvailable() { 
			Book book = library.stream().filter(o -> o.getID().equals(id)).findFirst().get();
			if (book.availbility.getIsLent())
				return false;
			else
				return true;
		}
		
		public String getBorrower() { 
			Book book = library.stream().filter(o -> o.getID().equals(id)).findFirst().get();
			if (book.availbility.getIsLent())
				return book.availbility.getBorrowerA();
			else
				return null;
		}
		
		private void lendBook(String person){
			availbility.lentBook(person);
		}
		
		class Availibility{
			private boolean isLent;
			private String person;
			
			private Availibility(){
				isLent = false;
				person = null;
			}
			
			private void lentBook(String name){
				isLent = true;
				person = name;
			}
			
			private boolean getIsLent() { return isLent; }
			private String getBorrowerA() { return person; }
		}
	}
	
	public void addBook(String newTitle, int newYear, String newAuthor){
		Book book = new Book(newTitle, newYear, newAuthor);		
		book.setUniqueID();
		library.add(book);
	}
	
	public boolean deleteBook(UUID id){
		if (library.stream().anyMatch(o -> o.getID().equals(id))){
			Book book = library.stream().filter(o -> o.getID().equals(id)).findFirst().get();
			
			//Checking wheter the book is lent
			if (book.availbility.isLent)
				return false;
			
			library.remove(book);
			return true;
		}
		else
			return false;		
	}
	
	public String[] listAllBooks(){
		//for ()
		//ArrayList<Book> lib = library.stream().allMatch(o -> o.getTitle().equals(newTitle));
		//toDo distinct search
		String[] allBooks = new String[library.size()];
		int i=0;
		for(Book element : library){
			allBooks[i] = element.getTitle()+"\t"+element.getYear()+"\t"+element.getAuthor()+"\tavailable copies: ";// + element.getCountOfCopies();
			i++;
		}
		return allBooks;
	}
	public String search(){
		//toDo
		//library.stream().filter(o -> o.getTitle().equals(newTile)).filter(o -> o.getYear() == newYear).filter(o -> o.getAuthor().equals(newAuthor));
		return null;
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

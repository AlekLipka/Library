import java.util.UUID;

public class Book{
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
		
		public void setID(UUID newId){ id = newId; }
		public UUID getID(){ return id; }
		public String getTitle(){ return title; }
		public int getYear(){ return year; }
		public String getAuthor(){ return author; }
		
		public boolean isAvailable() { 
			if (availbility.getIsLent())
				return false;
			else
				return true;
		}
		
		public String getBorrower() { 
			if (availbility.getIsLent())
				return availbility.getBorrowerA();
			else
				return null;
		}
		
		public void lendBook(String person){
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

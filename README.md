# Library
Library application written in Java 1.8

The application has no GUI and the only way to check it's usability is via unit tests.

There are the following tests written for the Library class:
- testAddBook() - checks wheter the function <i>Library.addBook(Title, Year, Author)</i> works correctly and generates unique ID for each book.
- testDeleteBook() - checks wheter the function <i>Library.deleteBook(Id)</i> deletes the book given by id and if it doesn't crash when the book woth the given id doesn't exist.
- testListAllBooks()
- testSearch()
- testLendBook() - tests wheter the function <i>Library.lendBook(Id, "name surname")</i> lends a book correctly to the given person and wheter its availbility is changed afterwards
- testViewBooksDetails() - tests wheter the function <i>Library.viewBooksDetails(Id)</i> shows all of the info about the book correctly - also if its available or lent to someone

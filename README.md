Bookstore web application 

Based on Java 17, Spring Boot.
Stores data in MySQL database from db4free.net (temporary database, that will be up and run approximately till the end of September 2024).
Tests written with Junit5 and Mockito.

Bookstore.com PDF fail contains description of main components, risks and setup guide. 

Endpoints: 
http://localhost:8080/v1/books (view books with pagination (50 books in one page));
http://localhost:8080/v1/admin/add   (admin-only page for adding books);
http://localhost:8080/v2/books (view books with price in json format to pass it to e-commerce);
http://localhost:8080/v2/books/{name} (PUT method to update the price of the book, body of the book must be provided and name provided in the api should be the same as in the provided body in json format.
Example: { "name": "Name", "price": 19.99}

Docker image can be accessed after login in docker hub, by next commands: 
docker pull alijajeniceka/bookstore:latest 
docker run -d -p 8080:8080 alijajeniceka/bookstore:latest


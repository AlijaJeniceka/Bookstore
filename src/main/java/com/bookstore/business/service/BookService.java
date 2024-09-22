package com.bookstore.business.service;

import com.bookstore.model.BookDTO;

import java.util.List;

public interface BookService {

    /**
     * Method to save book
     *
     * @param bookToSave
     * @return
     */
    BookDTO saveBook(BookDTO bookToSave);

    /**
     * Method to find all books with pagination
     *
     * @param page
     * @return
     */
    List<BookDTO> findAllBookList(int page);

    /**
     * Method to find list of the books without pagination
     *
     * @return
     */
    List<BookDTO> findAllBookListWithoutPagination();

    /**
     * Methof to update book by name
     *
     * @param updatedBook
     * @param name
     * @return
     * @throws Exception
     */
    BookDTO updateBook(BookDTO updatedBook, String name) throws Exception;
}

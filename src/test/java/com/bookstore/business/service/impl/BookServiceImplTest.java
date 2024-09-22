package com.bookstore.business.service.impl;

import com.bookstore.business.mapper.BookMapper;
import com.bookstore.business.repository.BookRepository;
import com.bookstore.business.repository.model.Book;
import com.bookstore.exception.BookControllerException;
import com.bookstore.model.BookDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private BookDTO bookDTO;
    private Book book;

    @BeforeEach
    void setUp() {
        bookDTO = new BookDTO();
        bookDTO.setName("Test Book");
        bookDTO.setPrice(19.99);

        book = new Book();
        book.setName("Test Book");
        book.setPrice(19.99);
        book.setDateAdded(new Date());
    }

    @Test
    void test_saveBook_success() {
        //given
        when(bookRepository.findByName(bookDTO.getName())).thenReturn(Optional.empty());
        when(bookMapper.bookDTOToBook(bookDTO)).thenReturn(book);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.bookToBookDTO(book)).thenReturn(bookDTO);
        //when
        BookDTO savedBook = bookService.saveBook(bookDTO);
        //then
        assertNotNull(savedBook);
        assertEquals(bookDTO.getName(), savedBook.getName());
        verify(bookRepository).save(book);
    }

    @Test
    void test_saveBook_throwException_bookAlreadyExists() {
        //given-when
        when(bookRepository.findByName(bookDTO.getName())).thenReturn(Optional.of(book));
        BookControllerException exception = assertThrows(BookControllerException.class, () -> {
            bookService.saveBook(bookDTO);
        });
        //then
        assertEquals(HttpStatus.CONFLICT, exception.getErrorCode());
        assertEquals("Book with same name already exists", exception.getMessage());
    }

    @Test
    void test_findAllBookList_success() {
        //given
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Arrays.asList(book)));
        when(bookMapper.bookToBookDTO(book)).thenReturn(bookDTO);
        //when
        List<BookDTO> books = bookService.findAllBookList(0);
        //then
        assertNotNull(books);
        assertEquals(1, books.size());
        assertEquals("Test Book", books.get(0).getName());
    }

    @Test
    void test_findAllBookListWithoutPagination_success() {
        //given
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book));
        when(bookMapper.bookToBookDTO(book)).thenReturn(bookDTO);
        //when
        List<BookDTO> books = bookService.findAllBookListWithoutPagination();
        //then
        assertNotNull(books);
        assertEquals(1, books.size());
    }

    @Test
    void test_updateBook_success() throws Exception {
        //given
        when(bookRepository.findByName("Test Book")).thenReturn(Optional.of(book));
        when(bookMapper.bookToBookDTO(book)).thenReturn(bookDTO);

        bookDTO.setPrice(24.99);
        //when
        BookDTO updatedBook = bookService.updateBook(bookDTO, "Test Book");
        //then
        assertNotNull(updatedBook);
        assertEquals(24.99, updatedBook.getPrice());
        verify(bookRepository).save(book);
    }

    @Test
    void test_updateBook_throwException_nameDoesNotMatch() {
        //given-when
        bookDTO.setName("Different Book");
        BookControllerException exception = assertThrows(BookControllerException.class, () -> {
            bookService.updateBook(bookDTO, "Test Book");
        });
        //then
        assertEquals(HttpStatus.NOT_ACCEPTABLE, exception.getErrorCode());
        assertEquals("Book is not possible to update", exception.getMessage());
    }

    @Test
    void test_updateBook_throwException_bookNotFound() {
        //given-when
        when(bookRepository.findByName("Test Book")).thenReturn(Optional.empty());
        BookControllerException exception = assertThrows(BookControllerException.class, () -> {
            bookService.updateBook(bookDTO, "Test Book");
        });
        //then
        assertEquals(HttpStatus.NOT_FOUND, exception.getErrorCode());
        assertEquals("Book with this name is not found Test Book", exception.getMessage());
    }
}
package com.bookstore.business.service.impl;

import com.bookstore.business.service.BookService;
import com.bookstore.model.BookDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.util.Arrays;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EcommerceSyncServiceImplTest {

    @Mock
    private BookService bookService;
    @Mock
    private Logger log;
    @InjectMocks
    private EcommerceSyncServiceImpl ecommerceSyncService;

    private BookDTO newBook;
    private BookDTO existingBook;

    @BeforeEach
    void setUp() {
        newBook = new BookDTO();
        newBook.setName("New Book");
        newBook.setPrice(29.99);

        existingBook = new BookDTO();
        existingBook.setName("Existing Book");
        existingBook.setPrice(19.99);
    }

    @Test
    void test_booksSynchronization_success() {
        //given
        when(bookService.findAllBookListWithoutPagination()).thenReturn(Arrays.asList(newBook, existingBook));
        //when
        ecommerceSyncService.booksSynchronization();
        //then
        verify(bookService).findAllBookListWithoutPagination();
        verify(log, never()).info(anyString());
    }
}
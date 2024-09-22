package com.bookstore.web.controller;

import com.bookstore.business.service.BookService;
import com.bookstore.exception.BookControllerException;
import com.bookstore.model.BookDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;
    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private BookController bookController;

    private MockMvc mockMvc;

    private BookDTO bookDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController)
                .setControllerAdvice(new BookControllerException()) // Exception handler
                .build();

        bookDTO = new BookDTO("Test Book", 10.99, new Date());
    }

    @Test
    void test_getBooks() throws Exception {
        List<BookDTO> bookList = Arrays.asList(bookDTO);

        when(bookService.findAllBookListWithoutPagination()).thenReturn(bookList);

        mockMvc.perform(get("/v2/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(bookList.size()))
                .andExpect(jsonPath("$[0].name").value("Test Book"))
                .andExpect(jsonPath("$[0].price").value(10.99));

        verify(bookService, times(1)).findAllBookListWithoutPagination();
    }

    @Test
    void test_updateBookByName() throws Exception {
        when(bookService.updateBook(any(BookDTO.class), eq("Test Book"))).thenReturn(bookDTO);

        mockMvc.perform(put("/v2/books/Test Book")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"Test Book\", \"price\": 20.99 }"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.name").value("Test Book"))
                .andExpect(jsonPath("$.price").value(20.99));

        verify(bookService, times(1)).updateBook(any(BookDTO.class), eq("Test Book"));
    }

}
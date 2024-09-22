package com.bookstore.web.controller;

import com.bookstore.business.service.BookService;
import com.bookstore.model.BookDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookstoreControllerTest {

    @Mock
    private BookService bookService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private BookstoreController bookstoreController;

    private MockMvc mockMvc;

    private BookDTO bookDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookstoreController).build();
        bookDTO = new BookDTO("Test Book", 10.99, new Date());
    }

    @Test
    void test_findAllBooks_success() throws Exception {
        List<BookDTO> bookList = Collections.singletonList(bookDTO);

        when(bookService.findAllBookList(0)).thenReturn(bookList);

        mockMvc.perform(get("/v1/books").param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("currentPage", 0));

        verify(bookService, times(1)).findAllBookList(0);
    }

    @Test
    void test_showAddForm() throws Exception {
        mockMvc.perform(get("/v1/admin/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-book"));

        verifyNoInteractions(bookService);
    }

    @Test
    void test_addBook_ShouldSaveBook_AndReturnAddBookPage() throws Exception {
        mockMvc.perform(post("/v1/admin/add").param("name", "New Book"))
                .andExpect(status().isCreated())
                .andExpect(view().name("add-book"));

        verify(bookService, times(1)).saveBook(any(BookDTO.class));
    }

    @Test
    void test_showLoginForm() throws Exception {
        mockMvc.perform(get("/v1/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));

        verifyNoInteractions(bookService);
    }

    @Test
    void test_adminLogin() throws Exception {
        String username = "admin";
        String password = "password";

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(username, password);

        when(authenticationManager.authenticate(token)).thenReturn(token);

        mockMvc.perform(post("/v1/admin")
                .param("username", username)
                .param("password", password))
                .andExpect(redirectedUrl("v1/admin/add"));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}
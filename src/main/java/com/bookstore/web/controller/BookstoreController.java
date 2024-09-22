package com.bookstore.web.controller;

import com.bookstore.business.service.BookService;
import com.bookstore.model.BookDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("v1")
public class BookstoreController {

    private final BookService bookService;
    private final AuthenticationManager authenticationManager;

    @GetMapping("/books")
    @ResponseStatus(HttpStatus.OK)
    public String findAllBooks(@RequestParam(defaultValue = "0") int page, Model model) {
        log.info("Retrieve list of the books.");
        List<BookDTO> books = bookService.findAllBookList(page);
        model.addAttribute("books", books);
        model.addAttribute("currentPage", page);
        return "index";
    }

    @GetMapping("/admin/add")
    public String showAddForm() {
        log.info("Add form showing");
        return "add-book";
    }

    @PostMapping("/admin/add")
    @ResponseStatus(HttpStatus.CREATED)
    private String addBook(@RequestParam("name") String name) {
        log.info("Create new book by passing: {}.", name);
        BookDTO bookToSave = new BookDTO(name, 0, new Date());
        bookService.saveBook(bookToSave);
        log.debug("New book is created: {}.", bookToSave);
        return "add-book";
    }

    @GetMapping("/admin")
    public String showLoginForm() {
        log.info("Login form showing");
        return "login";
    }

    @PostMapping("/admin")
    public String adminLogin(@RequestParam("username") String username,
                             @RequestParam("password") String password) {
        try {
            log.info("Login attempt for user: " + username);
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(username, password);
            authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(token);
            return "redirect:v1/admin/add";
        } catch (AuthenticationException e) {
            log.error("Login attempt failed. ");
            return "redirect:v1/admin/add?error";
        }
    }
}

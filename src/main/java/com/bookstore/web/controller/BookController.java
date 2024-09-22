package com.bookstore.web.controller;

import com.bookstore.business.service.BookService;
import com.bookstore.exception.BookControllerException;
import com.bookstore.model.BookDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/books")
public class BookController {

    private final BookService bookService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<BookDTO>> getBooks() {
        log.info("Getting list of the books in Json format.");
        List<BookDTO> books = bookService.findAllBookListWithoutPagination();
        return ResponseEntity.ok(books);
    }

    @PutMapping("/{name}")
    public ResponseEntity<BookDTO> updateBookByName(@PathVariable String name, @RequestBody BookDTO updatedBook,
                                                    BindingResult bindingResult) throws Exception {
        log.info("Update existing book with name: {} and new body: {}", name, updatedBook);
        if (bindingResult.hasErrors()) {
            log.error("Book is not updated: error {}", bindingResult);
            throw new BookControllerException(HttpStatus.BAD_REQUEST, "Bad request to update book");
        }

        bookService.updateBook(updatedBook, name);
        log.debug("Book with name {} is updated: {}", name, updatedBook);
        return new ResponseEntity<>(updatedBook, HttpStatus.ACCEPTED);
    }

}

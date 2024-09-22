package com.bookstore.business.service.impl;

import com.bookstore.business.mapper.BookMapper;
import com.bookstore.business.repository.BookRepository;
import com.bookstore.business.repository.model.Book;
import com.bookstore.business.service.BookService;
import com.bookstore.exception.BookControllerException;
import com.bookstore.model.BookDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @CacheEvict(cacheNames = "bookList", allEntries = true)
    @Override
    public BookDTO saveBook(BookDTO bookToSave) {
        log.info("Started method to save the book with name: " + bookToSave.getName());
        boolean sameBookExists = bookRepository.findByName(bookToSave.getName()).isPresent();

        if (sameBookExists) {
            log.error("Book with same name already exists. Conflict exception. ");
            throw new BookControllerException(HttpStatus.CONFLICT, "Book with same name already exists");
        }

        Book bookDAO = bookMapper.bookDTOToBook(bookToSave);
        Book bookSaved = bookRepository.save(bookDAO);
        log.info("New book saved: {}", bookSaved);
        return bookMapper.bookToBookDTO(bookSaved);
    }


    @Cacheable(value = "bookList")
    @Scheduled(fixedDelay = 300000)
    @Override
    public List<BookDTO> findAllBookList(int page) {
        Pageable paging = PageRequest.of(page, 50, Sort.by("dateAdded").descending());
        Page<Book> bookList = bookRepository.findAll(paging);
        log.info("Method findAllBookList with paging. Size: " + bookList.stream().toList().size());

        return bookList
                .stream()
                .map(bookMapper::bookToBookDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDTO> findAllBookListWithoutPagination() {
        List<Book> bookList = bookRepository.findAll();
        log.info("Get book list. Size is : {}", bookList.size());

        return bookList
                .stream()
                .map(bookMapper::bookToBookDTO)
                .collect(Collectors.toList());
    }


    @Override
    public BookDTO updateBook(BookDTO updatedBook, String name) throws Exception {
        if (!updatedBook.getName().equalsIgnoreCase(name.trim())) {
            log.error("Book with this name is not possible to update ");
            throw new BookControllerException(HttpStatus.NOT_ACCEPTABLE, "Book is not possible to update");
        }

        Book optionalBook = bookRepository.findByName(name)
                .orElseThrow(() -> new BookControllerException(HttpStatus.NOT_FOUND, "Book with this name is not found " + name));

        optionalBook.setPrice(updatedBook.getPrice());
        optionalBook.setDateAdded(new Date());
        bookRepository.save(optionalBook);

        log.info("Book is updated: {}", optionalBook);
        return bookMapper.bookToBookDTO(optionalBook);
    }
}

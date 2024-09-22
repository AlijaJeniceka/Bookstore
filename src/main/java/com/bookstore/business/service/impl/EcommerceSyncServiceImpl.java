package com.bookstore.business.service.impl;

import com.bookstore.business.service.BookService;
import com.bookstore.business.service.EcommerceSyncService;
import com.bookstore.model.BookDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EcommerceSyncServiceImpl implements EcommerceSyncService {

    private final BookService bookService;

    @Scheduled(cron = "0 0 0 * * ?", zone = "Europe/Helsinki")
    @Override
    public void booksSynchronization() {
        List<BookDTO> books = bookService.findAllBookListWithoutPagination();
        books.forEach(book -> {
            try {
                syncBookWithECommerce(book);
            } catch (Exception e) {
                log.info("Error synchronizing book: " + book.getName() + " - " + e.getMessage());
            }
        });
    }

    private void syncBookWithECommerce(BookDTO book) {
        if (isNewBook(book)) {
            createBookInEcommerce(book);
        } else {
            updateBookInEcommerce(book);
        }
    }

    private boolean isNewBook(BookDTO book) {
        // Implement logic to check if book exists in e-commerce
        return false;
    }

    private void createBookInEcommerce(BookDTO book) {
        // Implement API call to create product
    }

    private void updateBookInEcommerce(BookDTO book) {
        // Implement API call to update product price
    }
}

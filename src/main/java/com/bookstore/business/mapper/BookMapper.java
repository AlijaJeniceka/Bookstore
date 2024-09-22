package com.bookstore.business.mapper;

import com.bookstore.business.repository.model.Book;
import com.bookstore.model.BookDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDTO bookToBookDTO(Book book);

    Book bookDTOToBook(BookDTO bookDTO);
}

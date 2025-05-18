package br.com.bookstore.mapper;

import br.com.bookstore.model.dtos.Book;
import br.com.bookstore.model.dtos.BookDetail;
import br.com.bookstore.model.entity.BookEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {BaseLabelMapper.class})
public interface BookMapper {

    Book toDto(BookEntity bookEntity);

    BookDetail toDetailDto(BookEntity bookEntity);

    List<Book> toDto(List<BookEntity> lstBookEntity);
}

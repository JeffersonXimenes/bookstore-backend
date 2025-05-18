package br.com.bookstore.service;

import br.com.bookstore.exception.ResourceNotFoundException;
import br.com.bookstore.mapper.BookMapper;
import br.com.bookstore.model.dtos.BaseLabel;
import br.com.bookstore.model.dtos.Book;
import br.com.bookstore.model.dtos.BookDetail;
import br.com.bookstore.model.entity.BookEntity;
import br.com.bookstore.repository.BookRepository;
import br.com.bookstore.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper mapper;

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ListOperations<String, String> listOperations;

    @InjectMocks
    private BookService bookService;

    @Test
    void shouldReturnAllBooks() {
        Pageable pageable = PageRequest.of(0, 10);
        BookEntity entity = new BookEntity();
        Book dto = new Book();

        List<BookEntity> entities = List.of(entity);
        Page<BookEntity> page = new PageImpl<>(entities, pageable, entities.size());

        when(bookRepository.findAll(pageable)).thenReturn(page);
        when(mapper.toDto(ArgumentMatchers.<BookEntity>any())).thenReturn(dto);

        List<Book> result = bookService.getAllBooks(0, 10);

        assertEquals(1, result.size());
        verify(bookRepository).findAll(pageable);
        verify(mapper).toDto(ArgumentMatchers.<BookEntity>any());
    }

    @Test
    void shouldReturnBookDetailById() {
        Long id = 1L;
        BookEntity entity = new BookEntity();
        BookDetail detail = new BookDetail();

        when(bookRepository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDetailDto(entity)).thenReturn(detail);
        when(redisTemplate.opsForList()).thenReturn(listOperations);

        BookDetail result = bookService.getAllBookById(id);

        assertEquals(detail, result);

        verify(bookRepository).findById(id);
        verify(mapper).toDetailDto(entity);
        verify(redisTemplate, times(3)).opsForList();
        verify(listOperations).remove("recentlyViewedBooks", 1, id.toString());
        verify(listOperations).leftPush("recentlyViewedBooks", id.toString());
        verify(listOperations).trim("recentlyViewedBooks", 0, 9);
    }

    @Test
    void shouldThrowWhenBookNotFound() {
        Long id = 999L;

        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> bookService.getAllBookById(id));

        assertTrue(exception.getMessage().contains("No item was found with the [ 999 ] entered."));
    }

    @Test
    void shouldReturnBooksByGenre() {
        String genre = "Romance";
        Pageable pageable = PageRequest.of(0, 10);

        List<BookEntity> entities = List.of(new BookEntity());

        Page<BookEntity> page = new PageImpl<>(entities, pageable, entities.size());

        when(bookRepository.findByMainGenreDescription(genre, pageable)).thenReturn(page);

        List<Book> dtos = List.of(new Book());
        when(mapper.toDto(ArgumentMatchers.<BookEntity>any())).thenReturn(dtos.get(0));

        List<Book> result = bookService.getBooksByGenre(genre, 0, 10);

        assertEquals(1, result.size());

        verify(bookRepository).findByMainGenreDescription(genre, pageable);
        verify(mapper).toDto(ArgumentMatchers.<BookEntity>any());
    }

    @Test
    void shouldReturnBooksByAuthor() {
        String author = "George";
        Pageable pageable = PageRequest.of(0, 10);

        BookEntity entity = new BookEntity();
        Book dto = new Book();

        List<BookEntity> entities = List.of(entity);
        List<Book> dtos = List.of(dto);

        Page<BookEntity> page = new PageImpl<>(entities, pageable, entities.size());

        when(bookRepository.findByAuthorLike(author, pageable)).thenReturn(page);
        when(mapper.toDto(entity)).thenReturn(dto);

        List<Book> result = bookService.getBooksByAuthor(author, 0, 10);

        assertEquals(1, result.size());
        verify(bookRepository).findByAuthorLike(author, pageable);
    }

    @Test
    void shouldCountAllBooks() {
        when(bookRepository.count()).thenReturn(42L);
        assertEquals(42L, bookService.countBooks());
    }

    @Test
    void shouldCountBooksByGenre() {
        when(bookRepository.countByMainGenreDescriptionIgnoreCase("Sci-Fi")).thenReturn(5L);
        assertEquals(5L, bookService.countBooksByGenre("Sci-Fi"));
    }

    @Test
    void shouldCountBooksByAuthor() {
        when(bookRepository.countByAuthorContainingIgnoreCase("Asimov")).thenReturn(3L);
        assertEquals(3L, bookService.countBooksByAuthor("Asimov"));
    }

    @Test
    void shouldReturnRecentlyViewedBooksFromRedis() {
        List<String> idsStr = List.of("1", "2");
        List<Long> ids = List.of(1L, 2L);
        List<BookEntity> entities = List.of(new BookEntity(), new BookEntity());
        List<Book> dtos = List.of(new Book(), new Book());

        when(redisTemplate.opsForList()).thenReturn(listOperations);
        when(listOperations.range("recentlyViewedBooks", 0, -1)).thenReturn(idsStr);
        when(bookRepository.findAllById(ids)).thenReturn(entities);

        when(mapper.toDto(ArgumentMatchers.<BookEntity>any()))
                .thenReturn(dtos.get(0))
                .thenReturn(dtos.get(1));

        List<Book> result = bookService.getRecentlyViewedBooks();

        assertEquals(2, result.size());
        verify(redisTemplate.opsForList()).range("recentlyViewedBooks", 0, -1);
        verify(bookRepository).findAllById(ids);
        verify(mapper, times(2)).toDto(ArgumentMatchers.<BookEntity>any());
    }

    @Test
    void shouldReturnEmptyListWhenRedisIsEmpty() {
        when(redisTemplate.opsForList()).thenReturn(listOperations);
        when(listOperations.range("recentlyViewedBooks", 0, -1)).thenReturn(List.of());

        List<Book> result = bookService.getRecentlyViewedBooks();

        assertTrue(result.isEmpty());
        verify(redisTemplate.opsForList()).range("recentlyViewedBooks", 0, -1);
        verifyNoInteractions(bookRepository);
        verifyNoInteractions(mapper);
    }
}
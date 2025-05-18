package br.com.bookstore.service;

import br.com.bookstore.exception.ResourceNotFoundException;
import br.com.bookstore.mapper.BookMapper;
import br.com.bookstore.model.dtos.Book;
import br.com.bookstore.model.dtos.BookDetail;
import br.com.bookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private static final String MSG_BOOK_NOT_FOUND = "No item was found with the [ %s ] entered.";

    private static final String RECENTLY_VIEWED_KEY = "recentlyViewedBooks";

    private static final int MAX_RECENTLY_VIEWED = 10;

    private final BookRepository bookRepository;

    private final BookMapper mapper;

    private final StringRedisTemplate redisTemplate;

    @Cacheable(value = "books", key = "#page + '-' + #size")
    public List<Book> getAllBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookRepository.findAll(pageable)
                .map(mapper::toDto)
                .toList();
    }

    @Cacheable(value = "booksById", key = "#id")
    public BookDetail getAllBookById(Long id) {
        BookDetail bookDetail = bookRepository.findById(id)
                .map(mapper::toDetailDto)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(MSG_BOOK_NOT_FOUND, id)));

        redisTemplate.opsForList().remove(RECENTLY_VIEWED_KEY, 1, id.toString());
        redisTemplate.opsForList().leftPush(RECENTLY_VIEWED_KEY, id.toString());
        redisTemplate.opsForList().trim(RECENTLY_VIEWED_KEY, 0, MAX_RECENTLY_VIEWED - 1);

        return bookDetail;
    }

    @Cacheable(value = "booksByGenre", key = "#genre + '-' + #page + '-' + #size")
    public List<Book> getBooksByGenre(String genre, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookRepository.findByMainGenreDescription(genre, pageable)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Cacheable(value = "booksByAuthor", key = "#author + '-' + #page + '-' + #size")
    public List<Book> getBooksByAuthor(String author, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookRepository.findByAuthorLike(author, pageable)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Cacheable(value = "booksCount")
    public long countBooks() {
        return bookRepository.count();
    }

    @Cacheable(value = "booksByGenreCount", key = "#genre")
    public long countBooksByGenre(String genre) {
        return bookRepository.countByMainGenreDescriptionIgnoreCase(genre);
    }

    @Cacheable(value = "booksByAuthorCount", key = "#author")
    public long countBooksByAuthor(String author) {
        return bookRepository.countByAuthorContainingIgnoreCase(author);
    }

    public List<Book> getRecentlyViewedBooks() {
        List<String> idStrings = redisTemplate.opsForList().range(RECENTLY_VIEWED_KEY, 0, -1);
        if (idStrings == null || idStrings.isEmpty()) return List.of();

        List<Long> ids = idStrings.stream().map(Long::valueOf).toList();

        return bookRepository.findAllById(ids).stream()
                .map(mapper::toDto)
                .toList();
    }
}

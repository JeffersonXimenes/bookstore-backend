package br.com.bookstore.controller;

import br.com.bookstore.exception.error.ResourceDetails;
import br.com.bookstore.model.dtos.Book;
import br.com.bookstore.model.dtos.BookDetail;
import br.com.bookstore.service.BookService;
import br.com.bookstore.utils.PaginationHeaderUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@Tag(name = "Books", description = "Services responsible for listing information about all books, with pagination options. It is also possible to retrieve a book by ID to obtain more detailed information or search for a specific genre or author.")
public class BookController {

    private final BookService bookService;

    @Operation(
            summary = "List all books",
            description = "List all books with pagination",
            responses = {@ApiResponse(description = "List of books", responseCode = "200")
            })
    @GetMapping
    private ResponseEntity<List<Book>> books(@Parameter(description = "Page where resources will be listed", name = "page", example = "0")
                                                 @RequestParam(defaultValue = "0")
                                                 @Min(value = 0, message = "Number must be at least 0!") int page,
                                             @Parameter(description = "Maximum number of resources to be listed", name = "size", example = "10")
                                                 @RequestParam(defaultValue = "10")
                                                 @Min(value = 1, message = "Number must be at least 1!")
                                                 @Max(value = 50, message = "Maximum limit of 50") int size) {

        List<Book> books = bookService.getAllBooks(page, size);
        Pageable pageable = PageRequest.of(page, size);
        long total = bookService.countBooks();

        Page<Book> pageResult = new PageImpl<>(books, pageable, total);
        return ResponseEntity.ok()
                .headers(PaginationHeaderUtil.createHeaders(pageResult))
                .body(books);
    }

    @Operation(
            summary = "List book by id",
            description = "List a book by id with detailed information about it",
            responses = {@ApiResponse(description = "Detailed book", responseCode = "200"),
                         @ApiResponse(description = "Book not found", responseCode = "404")
            })
    @GetMapping("/{id}")
    private ResponseEntity<BookDetail> booksById(@Parameter(description = "Book ID to be detailed", name = "id", example = "74")
                                                     @PathVariable Long id) {

        BookDetail response = bookService.getAllBookById(id);

        return ResponseEntity.ok().body(response);
    }

    @Operation(
            summary = "List by genre",
            description = "List all books by genre",
            responses = {@ApiResponse(description = "List of books by genre", responseCode = "200"),
                         @ApiResponse(description = "Genre not found", responseCode = "404", content = @Content(schema = @Schema(implementation = ResourceDetails.class)))

            })
    @GetMapping("/genre/{genre}")
    private ResponseEntity<List<Book>> booksByGenre(@Parameter(description = "Page where resources will be listed", name = "page", example = "0")
                                                       @RequestParam(defaultValue = "0")
                                                       @Min(value = 0, message = "Number must be at least 0!") int page,
                                                   @Parameter(description = "Maximum number of resources to be listed", name = "size", example = "10")
                                                       @RequestParam(defaultValue = "10")
                                                       @Min(value = 1, message = "Number must be at least 1!")
                                                       @Max(value = 50, message = "Maximum limit of 50") int size,
                                                   @Parameter(description = "Genre to look for", name = "genre", example = "Romance")
                                                       @PathVariable String genre) {

        List<Book> books = bookService.getBooksByGenre(genre, page, size);
        long total = bookService.countBooksByGenre(genre);
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> pageResult = new PageImpl<>(books, pageable, total);

        return ResponseEntity.ok()
                .headers(PaginationHeaderUtil.createHeaders(pageResult))
                .body(books);
    }

    @Operation(
            summary = "List by author",
            description = "List all books by author",
            responses = {@ApiResponse(description = "List of books by author", responseCode = "200"),
                         @ApiResponse(description = "Author not found", responseCode = "404", content = @Content(schema = @Schema(implementation = ResourceDetails.class)))
            })
    @GetMapping("/author/{author}")
    private ResponseEntity<List<Book>> booksByAuthor(@Parameter(description = "Page where resources will be listed", name = "page", example = "0")
                                                        @RequestParam(defaultValue = "0")
                                                        @Min(value = 0, message = "Number must be at least 0!") int page,
                                                    @Parameter(description = "Maximum number of resources to be listed", name = "size", example = "10")
                                                        @RequestParam(defaultValue = "10")
                                                        @Min(value = 1, message = "Number must be at least 1!")
                                                        @Max(value = 50, message = "Maximum limit of 50") int size,
                                                    @Parameter(description = "Author's name", name = "author", example = "George Orwell")
                                                        @PathVariable String author) {

        List<Book> books = bookService.getBooksByAuthor(author, page, size);
        long total = bookService.countBooksByAuthor(author);
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> pageResult = new PageImpl<>(books, pageable, total);

        return ResponseEntity.ok()
                .headers(PaginationHeaderUtil.createHeaders(pageResult))
                .body(books);
    }

    @Operation(
            summary = "List of recently viewed",
            description = "List of books that have been recently viewed on book detail",
            responses = {@ApiResponse(description = "Book List", responseCode = "200")
            })
    @GetMapping("/recently-viewed")
    public ResponseEntity<List<Book>> getRecentlyViewedBooks() {
        List<Book> books = bookService.getRecentlyViewedBooks();
        return ResponseEntity.ok(books);
    }
}

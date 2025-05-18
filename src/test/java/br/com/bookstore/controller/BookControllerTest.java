package br.com.bookstore.controller;

import br.com.bookstore.exception.ResourceNotFoundException;
import br.com.bookstore.model.dtos.BaseLabel;
import br.com.bookstore.model.dtos.Book;
import br.com.bookstore.model.dtos.BookDetail;
import br.com.bookstore.service.BookService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookControllerTest {

    private static final String AUTH_HEADER = "Authorization";
    private static final String MOCK_TOKEN = "Bearer mock.jwt.token";
    private static final String EMAIL = "user@test.com";
    private static final String NAME = "Test Book";
    private static final String PASSWORD = "AbTp9!fok";
    private static String token;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @BeforeAll
    void registerUserOnce() throws Exception {
        try {
            mockMvc.perform(post("/auth/register")
                            .header("email", EMAIL)
                            .header("name", NAME)
                            .header("password", PASSWORD))
                    .andExpect(status().isCreated());
        } catch (Exception ignored) {

        }

        MvcResult result = mockMvc.perform(get("/auth/login")
                        .header("email", EMAIL)
                        .header("password", PASSWORD))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode json = objectMapper.readTree(response);
        token = json.get("token").asText();
    }

    @Test
    void shouldReturnListOfBooks() throws Exception {
        List<Book> mockBooks = List.of(
                new Book(1L, "1984", "George Orwell", new BigDecimal("29.99")),
                new Book(2L, "Animal Farm", "George Orwell", new BigDecimal("19.99"))
        );

        when(bookService.getAllBooks(0, 10)).thenReturn(mockBooks);
        when(bookService.countBooks()).thenReturn((long) mockBooks.size());

        mockMvc.perform(get("/books")
                .param("page", "0")
                .param("size", "10")
                .header(AUTH_HEADER, "Bearer " + token)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(mockBooks.size()))
            .andExpect(jsonPath("$[0].title").value("1984"))
            .andExpect(jsonPath("$[1].author").value("George Orwell"));
    }

    @Test
    void shouldReturnBookById() throws Exception {
        List<BaseLabel> types = List.of(new BaseLabel("Kindle"));
        List<BaseLabel> mainGenres = List.of(new BaseLabel("Dystopia"));
        List<BaseLabel> subGenres = List.of(new BaseLabel("Political Fiction"));

        BookDetail detail = new BookDetail(
                1L,
                "1984",
                "George Orwell",
                new BigDecimal("39.90"),
                types,
                mainGenres,
                subGenres
        );

        when(bookService.getAllBookById(1L)).thenReturn(detail);

        mockMvc.perform(get("/books/1")
                        .header(AUTH_HEADER, "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").value("1984"))
            .andExpect(jsonPath("$.author").value("George Orwell"))
            .andExpect(jsonPath("$.price").value(39.90))
            .andExpect(jsonPath("$.types[0].description").value("Kindle"))
            .andExpect(jsonPath("$.mainGenres[0].description").value("Dystopia"))
            .andExpect(jsonPath("$.subGenres[0].description").value("Political Fiction"));
    }

    @Test
    void shouldReturnNotFoundWhenBookDoesNotExist() throws Exception {
        Long id = 555L;

        when(bookService.getAllBookById(id))
                .thenThrow(new ResourceNotFoundException(String.format("No item was found with the [ %s ] entered.", id)));

        mockMvc.perform(get("/books/" + id)
                        .header(AUTH_HEADER, "Bearer " + token)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.title").value("Not Found"))
                .andExpect(jsonPath("$.detail[0]").value("No item was found with the [ 555 ] entered."))
                .andExpect(jsonPath("$.type").value("br.com.bookstore.exception.ResourceNotFoundException"));
    }

    @Test
    void shouldReturnBooksByGenre() throws Exception {
        List<Book> books = List.of(
                new Book(1L, "1984", "George Orwell", new BigDecimal("29.99")),
                new Book(2L, "Brave New World", "Aldous Huxley", new BigDecimal("25.00"))
        );

        when(bookService.getBooksByGenre("Dystopia", 0, 10)).thenReturn(books);
        when(bookService.countBooksByGenre("Dystopia")).thenReturn(2L);

        mockMvc.perform(get("/books/genre/Dystopia?page=0&size=10")
                        .header(AUTH_HEADER, "Bearer " + token)
                )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].title", is("1984")));
    }

    @Test
    void shouldReturnBooksByAuthor() throws Exception {
        List<Book> books = List.of(
                new Book(1L, "1984", "George Orwell", new BigDecimal("29.99"))
        );

        when(bookService.getBooksByAuthor("George Orwell", 0, 10)).thenReturn(books);
        when(bookService.countBooksByAuthor("George Orwell")).thenReturn(1L);

        mockMvc.perform(get("/books/author/George Orwell?page=0&size=10")
                        .header(AUTH_HEADER, "Bearer " + token)
                )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].author", is("George Orwell")));
    }

    @Test
    void shouldReturnRecentlyViewedBooks() throws Exception {
        List<Book> books = List.of(new Book(), new Book());

        when(bookService.getRecentlyViewedBooks()).thenReturn(books);

        mockMvc.perform(get("/books/recently-viewed")
                        .header(AUTH_HEADER, "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(bookService).getRecentlyViewedBooks();
    }

    @Test
    void shouldReturnEmptyListWhenNoRecentlyViewedBooks() throws Exception {
        when(bookService.getRecentlyViewedBooks()).thenReturn(List.of());

        mockMvc.perform(get("/books/recently-viewed")
                        .header(AUTH_HEADER, "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(bookService).getRecentlyViewedBooks();
    }
}

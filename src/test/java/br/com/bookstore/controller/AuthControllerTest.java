package br.com.bookstore.controller;

import br.com.bookstore.service.BookService;
import br.com.bookstore.utils.Constants;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthControllerTest {

    private static final String AUTH_HEADER = "Authorization";
    private static final String EMAIL = "user@test.com";
    private static final String NAME = "Test Book";
    private static final String PASSWORD = "AbTp9!fok";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @BeforeAll
    void registerUserOnce() throws Exception {
        MvcResult loginAttempt = mockMvc.perform(get("/auth/login")
                        .header("email", EMAIL)
                        .header("password", PASSWORD))
                .andReturn();

        int status = loginAttempt.getResponse().getStatus();

        if (status == 401) {
            mockMvc.perform(post("/auth/register")
                            .header("email", EMAIL)
                            .header("name", NAME)
                            .header("password", PASSWORD))
                    .andExpect(status().isCreated());
        }
    }

    @Test
    void shouldReturnBadRequestWhenRegisteringWithExistingEmail() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .header("email", EMAIL)
                        .header("name", NAME)
                        .header("password", PASSWORD))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value(Constants.TITLE_BUSINESS_VIOLATED))
                .andExpect(jsonPath("$.detail[0]").value(Constants.USER_ALREADY_REGISTERED));
    }

    @Test
    void shouldReturnUnauthorizedWhenUserNotFound() throws Exception {
        mockMvc.perform(get("/auth/login")
                        .header("email", "nonexistent@test.com")
                        .header("password", "wrongpass"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnUnauthorizedWithInvalidJWT() throws Exception {
        mockMvc.perform(get("/books")
                        .header(AUTH_HEADER, "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqZWZmZXJzb24ueGltZW5lczExQG91dGxvb2suY29tIiwiaWF0IjoxNzQ3NDQ3NjU3LCJleHAiOjE3NDc0NTEyNTd9.Gsqhf-Ihuop2sWQXwLQE4rKpVNIFkqsjnoI6fxWSFUI"))
                .andExpect(status().isUnauthorized());
    }
}

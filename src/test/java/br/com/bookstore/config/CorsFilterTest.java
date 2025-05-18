package br.com.bookstore.config;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CorsFilterTest {

    @Test
    void shouldReturn200ForOptionsRequest() throws Exception {
        CorsFilter corsFilter = new CorsFilter();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("OPTIONS");

        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain chain = mock(FilterChain.class);

        corsFilter.doFilter(request, response, chain);

        assertEquals(200, response.getStatus());
        assertEquals("*", response.getHeader("Access-Control-Allow-Origin"));
        assertEquals("true", response.getHeader("Access-Control-Allow-Credentials"));
        assertNotNull(response.getHeader("Access-Control-Allow-Methods"));
        assertEquals("3600", response.getHeader("Access-Control-Max-Age"));
        assertTrue(response.getHeader("Access-Control-Allow-Headers").contains("Authorization"));

        verify(chain, never()).doFilter(any(ServletRequest.class), any(ServletResponse.class));
    }
}
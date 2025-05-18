package br.com.bookstore.utils;

import br.com.bookstore.enums.HeadersEnum;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;

public class PaginationHeaderUtil {

    public static <T> HttpHeaders createHeaders(Page<T> page) {
        HttpHeaders headers = new HttpHeaders();

        headers.add(HeadersEnum.X_TOTAL_PAGES.getValue(), String.valueOf(page.getTotalPages() - 1));
        headers.add(HeadersEnum.X_TOTAL_ELEMENTS.getValue(), String.valueOf(page.getTotalElements()));
        headers.add(HeadersEnum.X_CURRENT_PAGE.getValue(), String.valueOf(page.getNumber()));
        headers.add(HeadersEnum.X_PAGE_SIZE.getValue(), String.valueOf(page.getSize()));

        return headers;
    }
}

package br.com.bookstore.exception.error;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public record ResourceDetails(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT-3") Date timestamp,
        int status,
        String title,
        List<String> detail,
        String type) {

    public ResourceDetails(Date timestamp, int status, String title, List<String> detail, String type) {
        this.timestamp = timestamp;
        this.status = status;
        this.title = title;
        this.detail = detail;
        this.type = type;
    }
}
package br.com.bookstore.model.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Schema(description = "Represents a detailed book object")
public class BookDetail {

    @Schema(description = "Unique Book ID", example = "1")
    private Long id;

    @Schema(description = "Book title", example = "1984")
    private String title;

    @Schema(description = "Author of the book", example = "George Orwell")
    private String author;

    @Schema(description = "Book value", example = "39.90")
    private BigDecimal price;

    @Schema(description = "Specifies the type of book. It can be: hardcover, kindle or paperback")
    private List<BaseLabel> types;

    @Schema(description = "Specifies which main genres this book belongs to")
    private List<BaseLabel> mainGenres;

    @Schema(description = "Specifies which subgenres this book belongs to")
    private List<BaseLabel> subGenres;
}

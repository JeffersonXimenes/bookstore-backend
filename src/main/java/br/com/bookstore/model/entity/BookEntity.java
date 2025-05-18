package br.com.bookstore.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "tb_book")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "integer")
    private Long id;

    private String title;

    private String author;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tb_controls_type",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "type_id")
    )
    private List<TypeEntity> types;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tb_controls_main_genre",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "main_genre_id")
    )
    private List<MainGenreEntity> mainGenres;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tb_controls_sub_genre",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "sub_genre_id")
    )
    private List<SubGenreEntity> subGenres;
}
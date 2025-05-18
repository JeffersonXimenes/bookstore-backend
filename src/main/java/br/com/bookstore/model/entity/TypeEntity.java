package br.com.bookstore.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "tb_type")
public class TypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "integer")
    private Long id;

    private String description;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "types")
    private List<BookEntity> books;
}
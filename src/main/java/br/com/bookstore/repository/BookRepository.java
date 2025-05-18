package br.com.bookstore.repository;

import br.com.bookstore.model.entity.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

    @Query("SELECT book FROM BookEntity book JOIN book.mainGenres mainGenre WHERE mainGenre.description = :genre")
    Page<BookEntity> findByMainGenreDescription(@Param("genre") String genre, Pageable pageable);

    @Query("SELECT book FROM BookEntity book WHERE LOWER(book.author) LIKE LOWER(CONCAT('%', :author, '%'))")
    Page<BookEntity> findByAuthorLike(@Param("author") String author, Pageable pageable);

    @Query("SELECT COUNT(book) FROM BookEntity book JOIN book.mainGenres mg WHERE LOWER(mg.description) = LOWER(:genre)")
    long countByMainGenreDescriptionIgnoreCase(String genre);

    long countByAuthorContainingIgnoreCase(String author);
}

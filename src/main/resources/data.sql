INSERT INTO tb_book (author, title, price) VALUES
    ('George Orwell', '1984', 39.90),
    ('Harper Lee', 'To Kill a Mockingbird', 45.00),
    ('F. Scott Fitzgerald', 'The Great Gatsby', 29.90),
    ('J.K. Rowling', 'Harry Potter and the Sorcerer''s Stone', 59.99),
    ('J.R.R. Tolkien', 'The Lord of the Rings', 79.90),
    ('Jane Austen', 'Pride and Prejudice', 35.50),
    ('Dan Brown', 'The Da Vinci Code', 42.00),
    ('Gabriel García Márquez', 'One Hundred Years of Solitude', 51.25),
    ('Mary Shelley', 'Frankenstein', 28.00),
    ('Leo Tolstoy', 'War and Peace', 62.00),
    ('Ernest Hemingway', 'The Old Man and the Sea', 32.10),
    ('Aldous Huxley', 'Brave New World', 38.75),
    ('Ray Bradbury', 'Fahrenheit 451', 31.90),
    ('Markus Zusak', 'The Book Thief', 44.80),
    ('Antoine de Saint-Exupéry', 'The Little Prince', 26.90),
    ('Stephen King', 'The Shining', 49.90),
    ('Stephen King', 'It', 55.00),
    ('Margaret Atwood', 'The Handmaid''s Tale', 43.50),
    ('Herman Melville', 'Moby-Dick', 47.20),
    ('Emily Brontë', 'Wuthering Heights', 36.40),
    ('Charlotte Brontë', 'Jane Eyre', 34.70),
    ('Paulo Coelho', 'The Alchemist', 39.95),
    ('John Green', 'The Fault in Our Stars', 33.30),
    ('Kazuo Ishiguro', 'Never Let Me Go', 37.80),
    ('Arthur Conan Doyle', 'The Adventures of Sherlock Holmes', 29.90),
    ('Douglas Adams', 'The Hitchhiker''s Guide to the Galaxy', 41.50),
    ('Khaled Hosseini', 'The Kite Runner', 46.00),
    ('Oscar Wilde', 'The Picture of Dorian Gray', 28.50),
    ('George R.R. Martin', 'A Game of Thrones', 64.90),
    ('Suzanne Collins', 'The Hunger Games', 40.25),
    ('Yann Martel', 'Life of Pi', 37.10),
    ('Bram Stoker', 'Dracula', 30.80),
    ('C.S. Lewis', 'The Lion, the Witch and the Wardrobe', 35.60),
    ('Virginia Woolf', 'Mrs. Dalloway', 33.30),
    ('Albert Camus', 'The Stranger', 31.70),
    ('Victor Hugo', 'Les Misérables', 59.99),
    ('Jojo Moyes', 'Me Before You', 38.40),
    ('Colleen Hoover', 'It Ends With Us', 42.90),
    ('Lev Tolstoy', 'Anna Karenina', 53.00),
    ('Ian McEwan', 'Atonement', 39.20),
    ('Dostoevsky', 'Crime and Punishment', 48.50),
    ('J.D. Salinger', 'The Catcher in the Rye', 34.10),
    ('Neil Gaiman', 'American Gods', 46.75),
    ('Patrick Rothfuss', 'The Name of the Wind', 50.30),
    ('Erich Maria Remarque', 'All Quiet on the Western Front', 36.60),
    ('José Saramago', 'Blindness', 45.80),
    ('Malcolm Gladwell', 'Outliers', 43.20),
    ('Sun Tzu', 'The Art of War', 27.90),
    ('Niccolò Machiavelli', 'The Prince', 29.10),
    ('H.G. Wells', 'The Time Machine', 33.00),
    ('E.M. Forster', 'A Passage to India', 37.00);

INSERT INTO tb_type (description) VALUES
    ('Kindle Edition'),
    ('Capa Dura'),
    ('Capa Comum');

INSERT INTO tb_main_genre (description) VALUES
    ('Ficção'),
    ('Não Ficção'),
    ('Romance'),
    ('Fantasia'),
    ('Mistério'),
    ('Suspense'),
    ('Terror'),
    ('Biografia'),
    ('História'),
    ('Autoajuda'),
    ('Ciência'),
    ('Ficção Científica'),
    ('Literatura Clássica'),
    ('Drama'),
    ('Aventura'),
    ('Religião'),
    ('Negócios'),
    ('Filosofia'),
    ('Humor'),
    ('Poesia');

INSERT INTO tb_sub_genre (description) VALUES
    ('Distopia'),
    ('Romance Histórico'),
    ('Fantasia Épica'),
    ('Mistério Policial'),
    ('Suspense Psicológico'),
    ('Terror Sobrenatural'),
    ('Biografia Autorizada'),
    ('Drama Familiar'),
    ('Ficção Filosófica'),
    ('Ficção Científica Clássica'),
    ('Realismo Mágico'),
    ('Aventura Fantástica'),
    ('Romance Contemporâneo'),
    ('Crítica Social'),
    ('Pós-apocalíptico'),
    ('Satírico'),
    ('Jornada do Herói'),
    ('Política'),
    ('Literatura Existencialista'),
    ('Autoajuda Espiritual');

INSERT INTO tb_role (description) VALUES
    ('USER');

INSERT INTO tb_controls_type (book_id, type_id) VALUES
    (1, 1),
    (2, 2),
    (3, 3),
    (4, 1),
    (5, 2),
    (6, 3),
    (7, 1),
    (8, 2),
    (9, 3),
    (10, 1),
    (11, 2),
    (12, 3),
    (13, 1),
    (14, 2),
    (15, 3),
    (16, 1),
    (17, 2),
    (18, 3),
    (19, 1),
    (20, 2),
    (21, 3),
    (22, 1),
    (23, 2),
    (24, 3),
    (25, 1),
    (26, 2),
    (27, 3),
    (28, 1),
    (29, 2),
    (30, 3),
    (31, 1),
    (32, 2),
    (33, 3),
    (34, 1),
    (35, 2),
    (36, 3),
    (37, 1),
    (38, 2),
    (39, 3),
    (40, 1),
    (41, 2),
    (42, 3),
    (43, 1),
    (44, 2),
    (45, 3),
    (46, 1),
    (47, 2),
    (48, 3),
    (49, 1),
    (50, 2),
    (51, 3);

INSERT INTO tb_controls_main_genre (book_id, main_genre_id) VALUES
    (1, 12),
    (2, 3),
    (3, 13),
    (4, 4),
    (5, 4),
    (6, 3),
    (7, 5),
    (8, 3),
    (9, 7),
    (10, 13),
    (11, 13),
    (12, 12),
    (13, 12),
    (14, 3),
    (15, 3),
    (16, 7),
    (17, 7),
    (18, 6),
    (19, 13),
    (20, 3),
    (21, 3),
    (22, 10),
    (23, 3),
    (24, 14),
    (25, 5),
    (26, 12),
    (27, 3),
    (28, 13),
    (29, 4),
    (30, 15),
    (31, 4),
    (32, 7),
    (33, 4),
    (34, 14),
    (35, 13),
    (36, 13),
    (37, 3),
    (38, 3),
    (39, 3),
    (40, 14),
    (41, 13),
    (42, 14),
    (43, 4),
    (44, 4),
    (45, 9),
    (46, 14),
    (47, 17),
    (48, 18),
    (49, 18),
    (50, 12);

INSERT INTO tb_controls_sub_genre (book_id, sub_genre_id) VALUES
    (1, 1),
    (2, 2),
    (3, 14),
    (4, 3),
    (5, 3),
    (6, 2),
    (7, 4),
    (8, 11),
    (9, 6),
    (10, 2),
    (11, 8),
    (12, 1),
    (13, 10),
    (14, 14),
    (15, 12),
    (16, 6),
    (17, 5),
    (18, 1),
    (19, 9),
    (20, 8),
    (21, 13),
    (22, 20),
    (23, 13),
    (24, 9),
    (25, 4),
    (26, 16),
    (27, 8),
    (28, 14),
    (29, 3),
    (30, 15),
    (31, 12),
    (32, 6),
    (33, 12),
    (34, 8),
    (35, 19),
    (36, 2),
    (37, 13),
    (38, 13),
    (39, 2),
    (40, 8),
    (41, 19),
    (42, 8),
    (43, 3),
    (44, 3),
    (45, 2),
    (46, 14),
    (47, 17),
    (48, 18),
    (49, 18),
    (50, 10),
    (51, 17);

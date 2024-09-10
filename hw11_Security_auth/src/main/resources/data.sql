insert into authors(full_name)
values ('Author_1'),
       ('Author_2'),
       ('Author_3');

insert into genres(name)
values ('Genre_1'),
       ('Genre_2'),
       ('Genre_3');

insert into books(title, author_id, genre_id)
values ('BookTitle_1', 1, 1),
       ('BookTitle_2', 2, 2),
       ('BookTitle_3', 3, 3);

insert into comments(text, BOOK_ID)
values ('Comment_1_1', 1),
       ('Comment_1_2', 1),
       ('Comment_2_1', 2),
       ('Comment_2_2', 2),
       ('Comment_3_1', 3),
       ('Comment_3_2', 3);

insert into users(username, password, role)
values ('admin', '$2a$10$4cykf3l2aqNSxGkvNuudjOf5du2GWpWcZB4Q1EMVpkKR3Y67RVv9q', 'ROLE_ADMIN');
create table users
(
    id       bigint auto_increment
        primary key,
    username varchar(255) not null,
    password varchar(255) not null
);
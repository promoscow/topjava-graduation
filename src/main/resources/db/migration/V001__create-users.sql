create sequence users_seq start with 1 increment by 1;

create table users
(
    id       bigint auto_increment sequence users_seq
        primary key,
    username varchar(255) not null,
    password varchar(255) not null
);
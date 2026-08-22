create sequence tags_seq start with 1 increment by 1;

create table tags
(
    id   bigint auto_increment sequence tags_seq
        primary key,
    name varchar(255) not null,
    constraint ux_tags_name unique (name)
);

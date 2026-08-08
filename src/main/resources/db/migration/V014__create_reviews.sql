create sequence reviews_seq start with 1 increment by 1;

create table reviews
(
    id            bigint auto_increment sequence reviews_seq
        primary key,
    rating        int           not null,
    text          varchar(1000),
    date          date          not null,
    user_id       long          not null references users (id),
    restaurant_id long          not null references restaurants (id),
    constraint ux_user_restaurant unique (user_id, restaurant_id),
    constraint ck_reviews_rating check (rating between 1 and 5)
);

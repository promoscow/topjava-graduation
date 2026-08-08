insert into reviews(id, rating, text, date, user_id, restaurant_id)
values (1, 5, 'Отличный обед, буду ходить ещё', current_date(), 2, 1),
       (2, 4, 'Вкусно, но порции небольшие', current_date(), 3, 1),
       (3, 3, 'Средненько, ждать пришлось долго', current_date(), 4, 2),
       (4, 5, 'Лучший гусь в городе', current_date(), 5, 3),
       (5, 5, null, current_date(), 2, 2),
       (6, 4, null, current_date(), 3, 3),
       (7, 2, null, current_date(), 4, 1);

alter sequence reviews_seq restart with 8;

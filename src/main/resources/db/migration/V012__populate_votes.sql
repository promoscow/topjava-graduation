insert into votes(id, date, user_id, restaurant_id)
values (1, current_date(), 1, 1),
       (2, current_date(), 2, 1),
       (3, current_date(), 3, 3),
       (4, current_date(), 4, 3),
       (5, current_date(), 5, 3);

alter sequence votes_seq restart with 6;
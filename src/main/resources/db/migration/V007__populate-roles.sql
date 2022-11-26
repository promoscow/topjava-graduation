insert into roles(id, name)
values (1, 'USER'),
       (2, 'ADMIN');

alter sequence roles_seq restart with 3;
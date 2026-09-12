insert into restaurants (id, name)
values (1, 'Три поросёнка'),
       (2, 'Большая ложка'),
       (3, 'Сытый гусь');

alter table restaurants alter column id restart with 4;

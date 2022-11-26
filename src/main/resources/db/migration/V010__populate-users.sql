insert into users (id, username, password)
values (1, 'admin', '$2a$10$cwJeYeeNKCylkRabGeWkPeYXxle.U0hjk4EOtIzp25PL.ZKESEb8C'), -- admin
       (2, 'chiller2', '$2a$10$OI9DCWuEuC5wAL4LIhPMm.7/NFDymEj/GDgJcUNJ5/JGxxq4vbYHq'), -- 123
       (3, 'bestHorse', '$2a$10$CIW8AxSbRAjvoNIxe/KHTOd6moW0vrXSUe.OTJ2e4Nsbt7lMR/Fkq'), -- qwer1234
       (4, 'veryBestEmployee777', '$2a$10$1OUrBITwsT32lw061W0G0edVtn.Q66X2KiKIyFa9kYLtBry8KSsSC'), -- aqswde
       (5, 'pooh', '$2a$10$rMV7CEDtJbZXBrm.To83Vu5.FIGfi0zbAmdodhbAs.To3wLhkGUBS'); -- 12345

alter sequence users_seq restart with 6;
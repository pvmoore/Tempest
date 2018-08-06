-- person table

create table person(name varchar(50),
                    age integer,
                    birthdate date,
                    bornexactly timestamp,
                    borntime time);

insert into person values ('peter',31,'1811/07/13','1811/07/13 03:15:00','03:15:00.3000');
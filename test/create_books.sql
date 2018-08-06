-- creates books table

create table books(ref int,
                   title text,
                   author text,
                   published date,
                   pages int);

insert into books values (1,'The Fellowship of the Ring','JRR Tolkien','2000/11/19',1000);
insert into books values (2,'The Two Towers','JRR Tolkien','1999/01/01',1000);
insert into books values (3,'The Return of the King','JRR Tolkien','1983/07/6',1000);
create table test (ref autonumber not null primary  key unique, chars char(5),title varchar(255) default 'he'',llo',  num  int)
insert into test values (12,'yelp!','yellow',30)
insert test (title,num) values ('blah',20)
insert test (ref,title,num) values (12,'blah00',20)
insert test (ref,title,num) values (12,'blah00',20)
insert test (num,title) values (40,'blah00''s BAG')
insert test (num,title) values (5,'some text')
insert test (ref,num,title) values (1,80,'wonderful')
select num from test group by num order by num
select count(*) from test
select count(ref) from test
select count(num) from test
select count(distinct num) from test
select interval '3 10:03:02.100' day to second * 2 from test
select interval '10' year - interval '5-2' year(1) to month(1) from test
select interval '10' year + 2 from test
select interval '10' day - interval '5 2' day(1) to hour(1) from test
select interval '10' day + 2 from test
select interval '10' hour + interval '37' minute from test
select interval '7-5' year to month * 3 from test  
select interval '7-5' year to month / 2 from test  
select mod(10,3) from test
select timestamp '2003/10/9 10:11:12.00' from test
select date '2003/01/02' from test
select time '30:40:50.3' from test
select interval '3-10' year to month from test
select interval '-10-3' year to month from test
select interval '4 10:11:12.000001' day to second from test
select interval '-4 10:11:12.000001' day to second from test
select (3+7*10) from test
select num from test where num>(22+1-1)
select title from test where num=21 or num=35
select title,num from test where (num=21 or num=40)
SELECT 'tom''s WHERE clause',10,'hello',title || '--' || title FROM test t where ((title='yop' or (title='yel' || 'low')) AND num=21) order  by title
select num,interval '3' year from test
select (2+3) / (1+2) from test
select (2.0+3) / (1+2) from test
select (2.0 + 3.0) / (1.0 + 2.0) from test
select ((2.0 + 3.0) / (1.0 + 2.0)) from test
SELECT 'tom''s WHERE clause' from test
select * from test order by ref
select abs(10-21),'hello' as hello from test
select t.* from test t
select * from test order by ref desc
select num,ref,num + ref as result from test
select num from test
select num from test group by num
select num,title from test
select num,title from test group by num,title
select random(),num from test
select * from test order by (random())
select * from test order by random()
select * from test order by random(1)
select * from test order by ref desc,num,title
select * from test order by ref desc,num,title desc
select num,title from test order by num
select * from test order by num desc
select title from test where ref<=20
select t.num from test t
select 1.1+2 as mynum from test
select 'hello' || 'there' from test
SELECT (3+2*3),(2+3)/(1+2),((10)),((4+3)/2)*3,(10*(1+3)),num-2,(1+3-2*4) from test
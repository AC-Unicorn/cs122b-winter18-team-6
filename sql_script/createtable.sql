create table movies(
id varchar(10) not null,
title varchar(100) not null,
year int not null,
director varchar(100) not null,
PRIMARY KEY (id)

);


create table stars(
id varchar(10) not null,
name varchar(100) not null,
birthYear int,
primary key (id)
);

create table stars_in_movies(
starId varchar(10) not null,
movieId varchar(10) not null,
foreign key (starId) references stars(id),
foreign key (movieId) references movies(id)
);


create table genres(
id int not null auto_increment,
name varchar(32) not null,
primary key(id)
);

 
create table genres_in_movies(
genreId int not null,
movieId varchar(10) not null, 
foreign key (genreId) references genres(id),
foreign key (movieId) references movies(id)
);




create table creditcards( 
id varchar(20) not null,
firstName varchar(50) not null,
lastName varchar(50) not null,
expiration date not null,
primary key(id)
); 

create table ratings(
movieId varchar(10) not null,
rating float not null,
numVotes int not null,
foreign key(movieId) references movies(id) 
);

create table customers(
id int not null auto_increment,
firstName varchar(50) not null,
lastName varchar(50) not null,
ccId varchar(20) not null,
address varchar(200) not null,
email varchar(50) not null,
password varchar(20) not null,
foreign key(ccId) references creditcards(id),
primary key(id)
);

create table sales(
id int not null auto_increment,
customerId int not null,
movieId varchar(10) not null,
saleDate date not null,
foreign key(customerId) references customers(id),
foreign key(movieId) references movies(id),
primary key(id)
);

create table employees(
email varchar(50) primary key,
password varchar(20) not null,
fullname varchar(100)
);

INSERT INTO employees VALUES('classta@email.edu','classta','TA CS122B');
INSERT INTO employees VALUES('a','a','a');

drop procedure add_movie;

DELIMITER $$ 

CREATE PROCEDURE add_movie (in mTitle varchar(100),in year int,in director varchar(100),in sname varchar(100),in sbyear int,in gname varchar(32))

 
BEGIN

   declare ct int;
   declare ct1 int;
   declare ct2 int;
   declare movieId varchar(10);
   declare sid  varchar(10);
   declare gid  int;
   set ct = (SELECT count(*) FROM movies where title=mTitle);
   set ct1 = (SELECT count(*) FROM stars where name=sname);
   set ct2 = (SELECT count(*) FROM genres where name=gname);
   if ct=0 then
   
	 
     set movieId = concat('tt',substring(uuid_short(),11,7));
     set sid = substring(uuid_short(),8,10);
     set gid = (SELECT max(id)+1 from genres);
     insert into movies value(movieId,mTitle,year,director);
	 
     if ct1 = 0 then
     insert into stars value(sid,sname,sbyear);
     insert into stars_in_movies value(sid,movieId);
     else
     set sid = (select id from stars where name=sname limit 1);  
	 insert into stars_in_movies value(sid,movieId);
     end if;
     
     if ct2 = 0 then
     insert into genres value(gid,gname);
     insert into genres_in_movies value(gid,movieId);
     else
     set gid = (select id from genres where name=gname limit 1);
     insert into genres_in_movies value(gid,movieId);
     end if;
     
	 SELECT CONCAT("insert success") as answer;

   else
	 SELECT CONCAT("insert fail due to duplication record") as answer;
   end if;
   
END
$$

DELIMITER ; 




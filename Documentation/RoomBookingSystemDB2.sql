create database RoomBookingSystemDB2;
Use RoomBookingSystemDB2;

create table user(
id int(10) not null,
name varchar(50) not null,
phone varchar(15) not null,
email varchar(40) not null,
type varchar(15) not null,
password varchar(30) not null,
primary key (id)
);

insert into user(id,name,phone,email,type,password) values(1000,"tagore","+15142699000","tagore@gmail.com","admin","Tagore@1993");
insert into user(id,name,phone,email,type,password) values(2000,"tanya","+15142568000","tanya@gmail.com","admin","Tanya@1994");
insert into user(id,name,phone,email,type,password) values(3000,"arshdeep","+15148906000","arshdeep@gmail.com","admin","Arshdeep@1995");
insert into user(id,name,phone,email,type,password) values(9001,"akhil","+15147896000","akhil@gmail.com","staff","Akhil@9001");
insert into user(id,name,phone,email,type,password) values(9002,"mahesh","+15147896111","mahesh@gmail.com","staff","Mahesh@9002");
insert into user(id,name,phone,email,type,password) values(9003,"aditya","+15147896222","aditya@gmail.com","staff","Aditya@9003");

create table equipment(
id int(10) not null,
name varchar(50) not null,
type varchar(15) not null,
room_id int(10),
booking_details_id int(10),
primary key (id),
foreign key(room_id) references room(id),
foreign key(booking_details_id) references booking_details(id)
);

drop table equipment;

insert into equipment(id,name,type,room_id,booking_details_id) values (1,"android studio","software",2001,5001);
insert into equipment(id,name,type,room_id) values (2,"mysql work bench","software",2002);
insert into equipment(id,name,type,room_id,booking_details_id) values (3,"docker","software",2001,5001);
insert into equipment(id,name,type,room_id,booking_details_id) values (101,"laptops","hardware",2001,5001);
insert into equipment(id,name,type,room_id) values (102,"projectors","hardware",2002);
insert into equipment(id,name,type,room_id,booking_details_id) values (103,"printers","hardware",2002,5002);



create table room(
id int(10) not null,
capacity int(10) not null,
primary key (id)
);

drop table room;

insert into room(id,capacity)values (2001,20);
insert into room(id,capacity)values (2002,30);
insert into room(id,capacity)values (2003,10);
insert into room(id,capacity)values (2004,50);

drop table booking_details;

create table booking_details(
id int(10) not null,
purpose varchar(20) not null,
startdate date not null,
enddate date not null,
starttime time not null,
endtime time not null,
duration int(10) not null,
requested_equipment varchar(100) not null,
booking_status varchar(15) not null,
roomid int(10) not null,
userid int(10) not null,
primary key (id),
foreign key(roomid) references room(id),
foreign key(userid) references user(id)
 );
 
insert into booking_details(id,purpose,startdate,enddate,starttime,endtime,duration,requested_equipment,booking_status,roomid,userid) 
Values (5001,"exam","2020-05-16","2020-05-17","18:00:00","06:00:00",12,"Chairs","Confirmed",2001,9001);
insert into booking_details(id,purpose,startdate,enddate,starttime,endtime,duration,requested_equipment,booking_status,roomid,userid) 
Values (5002,"meeting","2020-05-17","2020-05-18","12:00:00","06:00:00",18,"Fans","Waiting",2002,9002);



select * from equipment e 
inner join booking_details bkdt on e.booking_details_id = bkdt.id
inner join room r on e.room_id = r.id where bkdt.id=5001;

select *, GROUP_CONCAT(name SEPARATOR ',') as equipments from (select e.id as eq_id, bkdt.id as bkd_id, e.type, e.name from equipment as e 
inner join booking_details bkdt on e.booking_details_id = bkdt.id
inner join room as r on e.room_id = r.id where bkdt.id=5001) as T group by type;
 
 
 
 
 
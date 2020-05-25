create database RoomBookingSystemDB;
Use RoomBookingSystemDB;

create table admin(
ad_id int(10) not null,
ad_password varchar(30) not null,
primary key(ad_id)
);
insert into admin(ad_id,ad_password) values(1000,"Tagore@1993");
insert into admin(ad_id,ad_password) values(2000,"Tanya@1994");
insert into admin(ad_id,ad_password) values(3000,"Arshdeep@1995");
 

create table user(
us_id int(10) not null,
us_name varchar(60) not null,
us_phone varchar(20) not null,
us_email varchar(30) not null,
us_password varchar(30) not null,
primary key (us_id)
);
insert into user(us_id,us_name,us_phone,us_email,us_password) values(9001,"akhil","+15147896000","akhil@gmail.com","Akhil@9001");
insert into user(us_id,us_name,us_phone,us_email,us_password) values(9002,"mahesh","+15147896111","mahesh@gmail.com","Mahesh@9002");
insert into user(us_id,us_name,us_phone,us_email,us_password) values(9003,"aditya","+15147896222","aditya@gmail.com","Aditya@9003");



create table S_Equipment(
sf_id int(10) not null,
sf_name varchar(50) not null,
ad_id int(10) not null,
primary key (sf_id),
foreign key (ad_id) references admin(ad_id) 
);
insert into S_Equipment(sf_id,sf_name,ad_id) values (1,"android studio,eclipse,sublime text",1000);
insert into S_Equipment(sf_id,sf_name,ad_id) values (2,"mysql work bench,nodejs",2000);
insert into S_Equipment(sf_id,sf_name,ad_id) values (3,"docker,netbeans",3000);

 


create table H_Equipment(
hd_id int(10) not null,
hd_name varchar(50) not null,
ad_id int(10) not null,
primary key (hd_id),
foreign key (ad_id) references admin(ad_id) 
);
insert into H_Equipment(hd_id,hd_name,ad_id) values (101,"laptops",1000);
insert into H_Equipment(hd_id,hd_name,ad_id) values (102,"projectors",2000);
insert into H_Equipment(hd_id,hd_name,ad_id) values (103,"printers",3000);


create table R_Equipment(
re_eq_id int(10) not null,
re_eq_name varchar(50) not null,
ad_id int(10) not null,
primary key (re_eq_id),
foreign key (ad_id) references admin(ad_id) 
);
insert into R_Equipment(re_eq_id,re_eq_name,ad_id) values (201,"fans",1000);
insert into R_Equipment(re_eq_id,re_eq_name,ad_id) values (202,"chairs",2000);
insert into R_Equipment(re_eq_id,re_eq_name,ad_id) values (203,"air fresheners",3000);




create table room(
r_id int(10) not null,
r_capacity int(10) not null,
sf_id int(10) not null,
hd_id int(10) not null,
ad_id int(10) not null,
primary key (r_id),
foreign key (sf_id) references  S_Equipment(sf_id),
foreign key (hd_id) references  H_Equipment(hd_id),
foreign key (ad_id) references admin(ad_id)
);
insert into room(r_id,r_capacity,sf_id,hd_id,ad_id)values (2001,20,1,101,1000);
insert into room(r_id,r_capacity,sf_id,hd_id,ad_id)values (2002,10,2,102,2000);
insert into room(r_id,r_capacity,sf_id,hd_id,ad_id)values (2003,30,3,103,3000);

create table if not exists R_Booking(
r_b_id int(10) not null,
re_eq_id int(10),
r_b_purpose varchar(20) not null,
r_b_startdate date not null,
r_b_enddate date not null,
r_b_starttime time not null,
r_b_endtime time not null,
r_b_duration int(10) not null,
r_id int(10) not null,
us_id int(10) not null,
primary key (r_b_id),
foreign key (re_eq_id) references  R_Equipment(re_eq_id),
foreign key(r_id) references room(r_id),
foreign key(us_id) references user(us_id)
 );
insert into R_Booking(r_b_id,re_eq_id,r_b_purpose,r_b_startdate,r_b_enddate,r_b_starttime,r_b_endtime,r_b_duration,r_id,us_id) 
Values (5001,202,"exam","2020-05-16","2020-05-17","18:00:00","06:00:00",12,2001,9001);
insert into R_Booking(r_b_id,re_eq_id,r_b_purpose,r_b_startdate,r_b_enddate,r_b_starttime,r_b_endtime,r_b_duration,r_id,us_id) 
Values (5002,201,"meeting","2020-05-17","2020-05-18","12:00:00","06:00:00",18,2002,9002);
insert into R_Booking(r_b_id,re_eq_id,r_b_purpose,r_b_startdate,r_b_enddate,r_b_starttime,r_b_endtime,r_b_duration,r_id,us_id) 
Values (5003,203,"exam","2020-05-18","2020-05-18","12:00:00","15:00:00",3,2003,9003);


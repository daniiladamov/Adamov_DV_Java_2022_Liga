create table users (id int8 generated by default as identity, name varchar(255), primary key (id));
create table tasks (id int8 generated by default as identity, task_date date, description varchar(255),
                    status varchar(255) default 'EMPTY', title varchar(255), user_id int8, primary key (id));
alter table if exists tasks add constraint FK6s1ob9k4ihi75xbxe2w0ylsdh foreign key (user_id) references users;

insert into users (name) values ('Ваня'),('Петя'),('Валера');
insert into tasks (title,description,user_id,task_date,status) values ('ДЗ','Cделать прям много чего',
                                                                      1,'2022-06-22','READY'),
 ('домашка','написать много кода',2,'2022-06-22','READY'),
('домашка','написать хоть сколько-либо кода',3,'2022-06-22','NEW'),
('домашка','написать что не смог написать',2,'2022-06-22','IN_PROGRESS');

insert into tasks (title,description,user_id,task_date) values('домашка','написать хоть что-то',3,'2022-07-22');

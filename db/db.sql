CREATE DATABASE fullstack;

CREATE TABLE TASKS(
id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
task_text varchar(150) not null,
dateTime timestamp not null
)
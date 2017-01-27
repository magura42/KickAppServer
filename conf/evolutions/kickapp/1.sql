# --- !Ups

CREATE TABLE Person(
  id INT PRIMARY KEY NOT NULL,
  email varchar(255) NOT NULL,
  password varchar(255) NOT NULL,
  firstname varchar(255) NOT NULL,
  lastname varchar(255) NOT NULL,
  login varchar(255) NOT NULL,
  isAdmin boolean NOT NULL
);

# --- !Downs

DROP TABLE Person;
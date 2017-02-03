# --- !Ups

CREATE TABLE person(
  id SERIAL PRIMARY KEY,
  email varchar(255),
  password varchar(255) NOT NULL,
  firstname varchar(255) NOT NULL,
  lastname varchar(255) NOT NULL,
  login varchar(255) NOT NULL,
  isadmin boolean NOT NULL
);

INSERT INTO person (firstname", "lastname", "email", "login", "isadmin", "password") VALUES ('Manfred2', 'Harrer', 'tne@gmx.li', 'mharrer', true, 'parzival');


# --- !Downs

DROP TABLE person;
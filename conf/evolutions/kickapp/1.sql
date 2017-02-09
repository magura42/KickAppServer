# --- !Ups

CREATE TABLE person(
  personid SERIAL PRIMARY KEY,
  email VARCHAR(255),
  password VARCHAR(255) NOT NULL,
  firstname VARCHAR(255) NOT NULL,
  lastname VARCHAR(255) NOT NULL,
  login VARCHAR(255) NOT NULL,
  isadmin BOOLEAN NOT NULL
);

INSERT INTO person ("firstname", "lastname", "email", "login", "isadmin", "password") VALUES ('Manfred', 'Harrer', 'tne@gmx.li', 'mharrer', true, 'mharrer');

CREATE TABLE club(
  clubid SERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  street VARCHAR(50) NOT NULL,
  zipcode VARCHAR(10) NOT NULL,
  city VARCHAR(50) NOT NULL,
  logo bytea,
  contact VARCHAR(50),
  email VARCHAR(50),
  telefon VARCHAR(50),
  web VARCHAR(50)
);

INSERT INTO club ("name", "street", "zipcode", "city", "telefon") VALUES ('SV Lochhausen', 'Bienenheimstraße 7', '81249', 'München', '089/8635131');

CREATE TABLE team(
  teamid SERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  foto BYTEA,
  fromyear INT NOT NULL,
  toyear INT NOT NULL,
  clubid INT REFERENCES club(clubid)
);

INSERT INTO team ("name", "fromyear", "toyear", "clubid") VALUES ('F1', 2008, 2008, 1);

# --- !Downs

DROP TABLE person;

DROP TABLE team;

DROP TABLE club;


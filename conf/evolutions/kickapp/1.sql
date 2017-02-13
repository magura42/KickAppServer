# --- !Ups


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

CREATE TABLE person(
  personid SERIAL PRIMARY KEY,
  firstname VARCHAR(255) NOT NULL,
  lastname VARCHAR(255) NOT NULL,
  street VARCHAR(50) NOT NULL,
  zipcode VARCHAR(10) NOT NULL,
  city VARCHAR(50) NOT NULL,
  telephone VARCHAR(50),
  email VARCHAR(50),
  birthday DATE,
  login VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(10) NOT NULL,
  teamid INT REFERENCES team(teamid),
  passnumber INT
);

INSERT INTO person ("firstname", "lastname", "email", "street", "zipcode", "city", "login", "role", "password") VALUES ('Manfred', 'Harrer', 'tne@gmx.li', 'Toni-Berger-Str. 15', '81249', 'München', 'mharrer', 'coach', 'mharrer');

INSERT INTO person ("firstname", "lastname", "email", "street", "zipcode", "city", "login", "role", "password", "teamid", "passnumber") VALUES ('Ludwig', 'Harrer', 'tne@gmx.li', 'Toni-Berger-Str. 15', '81249', 'München', 'lharrer', 'player', 'lharrer', 1, 3567);

INSERT INTO person ("firstname", "lastname", "email", "street", "zipcode", "city", "login", "role", "password") VALUES ('Andrea', 'Harrer', 'tne@gmx.li', 'Toni-Berger-Str. 15', '81249', 'München', 'aharrer', 'player', 'aharrer');

CREATE TABLE parenthood(
  parenthoodid SERIAL PRIMARY KEY,
  parentid INT REFERENCES person(personid),
  childid INT REFERENCES person(personid)
);

INSERT INTO parenthood ("parentid", "childid") VALUES (1, 2);
INSERT INTO parenthood ("parentid", "childid") VALUES (3, 2);

# --- !Downs

DROP TABLE parenthood;

DROP TABLE person;

DROP TABLE team;

DROP TABLE club;


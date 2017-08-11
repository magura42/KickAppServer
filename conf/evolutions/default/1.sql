# --- !Ups

CREATE TYPE role AS ENUM ('player', 'coach', 'parent');
CREATE TYPE personstatus AS ENUM ('active', 'inactive');

CREATE TABLE club (
  clubid  SERIAL PRIMARY KEY,
  name    VARCHAR(50) NOT NULL,
  street  VARCHAR(50) NOT NULL,
  zipcode VARCHAR(10) NOT NULL,
  city    VARCHAR(50) NOT NULL,
  logo    BYTEA,
  contact VARCHAR(50),
  email   VARCHAR(50),
  telefon VARCHAR(50),
  web     VARCHAR(50)
);

CREATE TABLE team (
  teamid   SERIAL PRIMARY KEY,
  name     VARCHAR(50) NOT NULL,
  foto     BYTEA,
  fromyear INT         NOT NULL,
  toyear   INT         NOT NULL,
  clubid   INT REFERENCES club (clubid)
);

CREATE TABLE person (
  personid   SERIAL PRIMARY KEY,
  firstname  VARCHAR(255) NOT NULL,
  lastname   VARCHAR(255) NOT NULL,
  street     VARCHAR(50)  NOT NULL,
  zipcode    VARCHAR(10)  NOT NULL,
  city       VARCHAR(50)  NOT NULL,
  telephone  VARCHAR(50),
  email      VARCHAR(50),
  birthday   DATE,
  login      VARCHAR(255) NOT NULL,
  password   VARCHAR(255) NOT NULL,
  role       VARCHAR(10) NOT NULL,
  teamid     INT REFERENCES team (teamid),
  passnumber INT,
  coached    INT REFERENCES team (teamid)
);

CREATE TABLE parenthood (
  parenthoodid SERIAL PRIMARY KEY,
  parentid     INT REFERENCES person (personid),
  childid      INT REFERENCES person (personid)
);

CREATE TABLE exercise (
  exerciseid   SERIAL PRIMARY KEY,
  name         VARCHAR(100)  NOT NULL,
  exercisetype VARCHAR(25)   NOT NULL,
  setup        VARCHAR(200)  NOT NULL,
  execution    VARCHAR(2000) NOT NULL,
  variants     VARCHAR(500),
  graphic      BYTEA,
  note         VARCHAR(500)
);

CREATE TABLE training (
  trainingid      SERIAL PRIMARY KEY,
  street          VARCHAR(50) NOT NULL,
  zipcode         VARCHAR(10) NOT NULL,
  city            VARCHAR(50) NOT NULL,
  date            DATE        NOT NULL,
  begintime       TIME        NOT NULL,
  endtime         TIME        NOT NULL,
  gettogethertime TIME        NOT NULL
);

CREATE TABLE trainingelement (
  trainingelementid SERIAL PRIMARY KEY,
  trainingid        INT REFERENCES training (trainingid),
  exerciseid        INT REFERENCES exercise (exerciseid)
);

CREATE TABLE trainingparticipant (
  trainingparticipantid SERIAL PRIMARY KEY,
  participantid         INT REFERENCES person (personid),
  trainingid            INT REFERENCES training (trainingid),
  role                  VARCHAR(10) NOT NULL
);

CREATE TABLE tournament (
  tournamentid    SERIAL PRIMARY KEY,
  name            VARCHAR(50) NOT NULL,
  street          VARCHAR(50) NOT NULL,
  zipcode         VARCHAR(10) NOT NULL,
  city            VARCHAR(50) NOT NULL,
  date            DATE        NOT NULL,
  begintime       TIME        NOT NULL,
  endtime         TIME        NOT NULL,
  gettogethertime TIME        NOT NULL,
  contact         VARCHAR(50),
  email           VARCHAR(50),
  telefon         VARCHAR(50),
  web             VARCHAR(50)
);

# --- !Downs

DROP TABLE tournament;

DROP TABLE trainingparticipant;

DROP TABLE trainingelement;

DROP TABLE parenthood;

DROP TABLE person;

DROP TABLE team;

DROP TABLE club;

DROP TABLE exercise;

DROP TABLE training;

DROP TYPE role;

DROP TYPE personstatus;
# --- !Ups

CREATE TYPE personstatus AS ENUM ('active', 'inactive');

CREATE TABLE club (
  clubid  SERIAL PRIMARY KEY,
  name    VARCHAR(50) NOT NULL,
  street  VARCHAR(50) NOT NULL,
  zipcode VARCHAR(10) NOT NULL,
  city    VARCHAR(50) NOT NULL,
  logo    TEXT,
  contact VARCHAR(50),
  email   VARCHAR(50),
  telefon VARCHAR(50),
  web     VARCHAR(50)
);

CREATE TABLE team (
  teamid   SERIAL PRIMARY KEY,
  name     VARCHAR(50) NOT NULL,
  foto     TEXT,
  fromyear INT         NOT NULL,
  toyear   INT         NOT NULL,
  clubid   INT REFERENCES club (clubid),
  info     VARCHAR(2000)
);

CREATE TABLE person (
  personid   SERIAL PRIMARY KEY,
  firstname  VARCHAR(255)                 NOT NULL,
  lastname   VARCHAR(255)                 NOT NULL,
  street     VARCHAR(50)                  NOT NULL,
  zipcode    VARCHAR(10)                  NOT NULL,
  city       VARCHAR(50)                  NOT NULL,
  telephone  VARCHAR(50),
  email      VARCHAR(50),
  birthday   DATE,
  foto       TEXT,
  login      VARCHAR(255)                 NOT NULL,
  password   VARCHAR(255)                 NOT NULL,
  role       VARCHAR(10)                  NOT NULL,
  teamid     INT REFERENCES team (teamid) NOT NULL,
  passnumber INT
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
  teamtype     VARCHAR(25)   NOT NULL,
  setup        VARCHAR(500)  NOT NULL,
  execution    VARCHAR(2000) NOT NULL,
  variants     VARCHAR(500),
  graphic      TEXT,
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
  gettogethertime TIME        NOT NULL,
  teamid          INT REFERENCES team (teamid)
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
  role                  VARCHAR(10) NOT NULL,
  participantstatus     VARCHAR(5)  NOT NULL
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
  web             VARCHAR(50),
  teamid          INT REFERENCES team (teamid)
);

CREATE TABLE tournamentparticipant (
  tournamentparticipantid SERIAL PRIMARY KEY,
  participantid           INT REFERENCES person (personid),
  tournamentid            INT REFERENCES tournament (tournamentid),
  role                    VARCHAR(10) NOT NULL,
  participantstatus       VARCHAR(5)  NOT NULL
);

CREATE TABLE teamevent (
  teameventid     SERIAL PRIMARY KEY,
  name            VARCHAR(50) NOT NULL,
  street          VARCHAR(50) NOT NULL,
  zipcode         VARCHAR(10) NOT NULL,
  city            VARCHAR(50) NOT NULL,
  date            DATE        NOT NULL,
  begintime       TIME        NOT NULL,
  endtime         TIME        NOT NULL,
  gettogethertime TIME        NOT NULL,
  teamid          INT REFERENCES team (teamid)
);


CREATE TABLE teameventparticipant (
  teameventparticipantid SERIAL PRIMARY KEY,
  participantid          INT REFERENCES person (personid),
  teameventid            INT REFERENCES teamevent (teameventid),
  role                   VARCHAR(10) NOT NULL,
  participantstatus      VARCHAR(5)  NOT NULL
);

CREATE TABLE match (
  matchid         SERIAL PRIMARY KEY,
  street          VARCHAR(50) NOT NULL,
  zipcode         VARCHAR(10) NOT NULL,
  city            VARCHAR(50) NOT NULL,
  date            DATE        NOT NULL,
  begintime       TIME        NOT NULL,
  endtime         TIME        NOT NULL,
  gettogethertime TIME        NOT NULL,
  matchtype       VARCHAR(25) NOT NULL,
  teamid          INT REFERENCES team (teamid)
);

CREATE TABLE matchparticipant (
  matchparticipantid SERIAL PRIMARY KEY,
  participantid      INT REFERENCES person (personid),
  matchid            INT REFERENCES match (matchid),
  role               VARCHAR(10) NOT NULL,
  participantstatus  VARCHAR(5)  NOT NULL
);

# --- !Downs

DROP TABLE IF EXISTS matchparticipant;

DROP TABLE IF EXISTS match;

DROP TABLE IF EXISTS teameventparticipant;

DROP TABLE IF EXISTS teamevent;

DROP TABLE IF EXISTS tournamentparticipant;

DROP TABLE IF EXISTS tournament;

DROP TABLE IF EXISTS trainingparticipant;

DROP TABLE IF EXISTS trainingelement;

DROP TABLE IF EXISTS parenthood;

DROP TABLE IF EXISTS person;

DROP TABLE IF EXISTS exercise;

DROP TABLE IF EXISTS training;

DROP TABLE IF EXISTS team;

DROP TABLE IF EXISTS club;

DROP TYPE IF EXISTS personstatus;

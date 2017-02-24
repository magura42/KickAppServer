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

INSERT INTO club ("name", "street", "zipcode", "city", "telefon")
VALUES ('SV Lochhausen', 'Bienenheimstraße 7', '81249', 'München', '089/8635131');

CREATE TABLE team (
  teamid   SERIAL PRIMARY KEY,
  name     VARCHAR(50) NOT NULL,
  foto     BYTEA,
  fromyear INT         NOT NULL,
  toyear   INT         NOT NULL,
  clubid   INT REFERENCES club (clubid)
);

INSERT INTO team ("name", "fromyear", "toyear", "clubid") VALUES ('F1', 2008, 2008, 1);

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
  role       role         NOT NULL,
  teamid     INT REFERENCES team (teamid),
  passnumber INT,
  coached    INT REFERENCES team (teamid)
);

INSERT INTO person ("firstname", "lastname", "email", "street", "zipcode", "city", "login", "role", "password", "coached")
VALUES ('Manfred', 'Harrer', 'tne@gmx.li', 'Toni-Berger-Str. 15', '81249', 'München', 'mharrer', 'coach', 'mharrer', 1);

INSERT INTO person ("firstname", "lastname", "email", "street", "zipcode", "city", "login", "role", "password", "teamid", "passnumber", "birthday")
VALUES ('Ludwig', 'Harrer', 'tne@gmx.li', 'Toni-Berger-Str. 15', '81249', 'München', 'lharrer', 'player', 'lharrer', 1,
                  3567, '2008-12-22');

INSERT INTO person ("firstname", "lastname", "email", "street", "zipcode", "city", "login", "role", "password")
VALUES ('Andrea', 'Harrer', 'tne@gmx.li', 'Toni-Berger-Str. 15', '81249', 'München', 'aharrer', 'parent', 'aharrer');

CREATE TABLE parenthood (
  parenthoodid SERIAL PRIMARY KEY,
  parentid     INT REFERENCES person (personid),
  childid      INT REFERENCES person (personid)
);

INSERT INTO parenthood ("parentid", "childid") VALUES (1, 2);
INSERT INTO parenthood ("parentid", "childid") VALUES (3, 2);

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

INSERT INTO exercise ("name", "setup", "exercisetype", "execution", "variants") VALUES ('Übergabe Kreis',
                                                                                        '6-10 Spieler bilden einen Kreis und ein Spieler hat einen Ball',
                                                                                        'warmup',
                                                                                        'Der Spieler läuft mit dem Ball zu einem anderen Spieler und übergibt den Ball (=> Positionswechsel).',
                                                                                        'Steigern mit mehreren Bällen.');

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


INSERT INTO training ("street", "zipcode", "city", "date", "begintime", "endtime", "gettogethertime")
VALUES ('Bienenheimstr. 5', '81249', 'München',
        '2017-04-04', '18:00:00', '19:30:00',
        '17:45:00');

CREATE TABLE trainingelement (
  trainingelementid SERIAL PRIMARY KEY,
  trainingid        INT REFERENCES training (trainingid),
  exerciseid        INT REFERENCES exercise (exerciseid)
);

INSERT INTO trainingelement ("trainingid", "exerciseid") VALUES (1, 1);

CREATE TABLE trainingparticipant (
  trainingparticipantid SERIAL PRIMARY KEY,
  participantid         INT REFERENCES person (personid),
  trainingid            INT REFERENCES training (trainingid),
  role                  VARCHAR(10) NOT NULL
);

INSERT INTO trainingparticipant ("participantid", "trainingid", "role") VALUES (1, 1, 'coach');
INSERT INTO trainingparticipant ("participantid", "trainingid", "role") VALUES (2, 1, 'player');


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

INSERT INTO tournament ("name", "street", "zipcode", "city", "date", "begintime", "endtime", "gettogethertime", "contact")
VALUES ('Sommerturnier TSV', 'Grünwalderstr. 15', '80000', 'München', '2017-06-12', '9:00:00', '13:00:00', '8:30:00', 'Werner Lorant');

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
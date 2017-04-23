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

INSERT INTO club ("name", "street", "zipcode", "city", "telefon")
VALUES ('SV Lochhausen', 'Bienenheimstraße 7', '81249', 'München', '089/8635131');

CREATE TABLE team (
  teamid   SERIAL PRIMARY KEY,
  name     VARCHAR(50) NOT NULL,
  foto     TEXT,
  fromyear INT         NOT NULL,
  toyear   INT         NOT NULL,
  clubid   INT REFERENCES club (clubid),
  info     VARCHAR(2000)
);

INSERT INTO team ("name", "fromyear", "toyear", "clubid") VALUES ('F1', 2008, 2008, 1);

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

INSERT INTO person ("firstname", "lastname", "email", "street", "zipcode", "city", "login", "role", "password", "teamid")
VALUES ('Manfred', 'Harrer', 'tne@gmx.li', 'Toni-Berger-Str. 15', '81249', 'München', 'mharrer', 'coach', 'mharrer', 1);

INSERT INTO person ("firstname", "lastname", "email", "street", "zipcode", "city", "login", "role", "password", "teamid", "telephone")
VALUES
  ('Franz', 'Beckenbauer', 'tne@gmx.li', 'Toni-Berger-Str. 15', '81249', 'München', 'mharrer', 'coach', 'mharrer', 1,
            '089/1238976234');

INSERT INTO person ("firstname", "lastname", "email", "street", "zipcode", "city", "login", "role", "password", "teamid", "passnumber", "birthday")
VALUES ('Ludwig', 'Harrer', 'tne@gmx.li', 'Toni-Berger-Str. 15', '81249', 'München', 'lharrer', 'player', 'lharrer', 1,
                  3567, '2008-12-22');

INSERT INTO person ("firstname", "lastname", "email", "street", "zipcode", "city", "login", "role", "password", "teamid", "passnumber", "birthday")
VALUES ('Manuel', 'Neuer', 'tne@gmx.li', 'Toni-Berger-Str. 15', '81249', 'München', 'lharrer', 'player', 'lharrer', 1,
                  3567, '2008-12-22');

INSERT INTO person ("firstname", "lastname", "email", "street", "zipcode", "city", "login", "role", "password", "teamid", "passnumber", "birthday")
VALUES ('Mats', 'Hummels', 'tne@gmx.li', 'Toni-Berger-Str. 15', '81249', 'München', 'lharrer', 'player', 'lharrer', 1,
                3567, '2008-12-22');

INSERT INTO person ("firstname", "lastname", "email", "street", "zipcode", "city", "login", "role", "password", "teamid")
VALUES ('Andrea', 'Harrer', 'tne@gmx.li', 'Toni-Berger-Str. 15', '81249', 'München', 'aharrer', 'parent', 'aharrer', 1);

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
  gettogethertime TIME        NOT NULL,
  teamid          INT REFERENCES team (teamid)
);

INSERT INTO training ("street", "zipcode", "city", "date", "begintime", "endtime", "gettogethertime", "teamid")
VALUES ('Bienenheimstr. 5', '81249', 'München',
        '2017-04-04', '18:00:00', '19:30:00',
        '17:45:00', 1);

INSERT INTO training ("street", "zipcode", "city", "date", "begintime", "endtime", "gettogethertime", "teamid")
VALUES ('Bienenheimstr. 5', '81249', 'München',
        '2017-04-11', '18:00:00', '19:30:00',
        '17:45:00', 1);

INSERT INTO training ("street", "zipcode", "city", "date", "begintime", "endtime", "gettogethertime", "teamid")
VALUES ('Bienenheimstr. 5', '81249', 'München',
        '2017-04-18', '18:00:00', '19:30:00',
        '17:45:00', 1);

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
  role                  VARCHAR(10) NOT NULL,
  participantstatus     VARCHAR(5)  NOT NULL
);

INSERT INTO trainingparticipant ("participantid", "trainingid", "role", "participantstatus")
VALUES (1, 1, 'coach', 'yes');
INSERT INTO trainingparticipant ("participantid", "trainingid", "role", "participantstatus")
VALUES (3, 1, 'player', 'yes');
INSERT INTO trainingparticipant ("participantid", "trainingid", "role", "participantstatus")
VALUES (4, 1, 'player', 'yes');
INSERT INTO trainingparticipant ("participantid", "trainingid", "role", "participantstatus")
VALUES (5, 1, 'player', 'no');

INSERT INTO trainingparticipant ("participantid", "trainingid", "role", "participantstatus")
VALUES (3, 2, 'player', 'maybe');
INSERT INTO trainingparticipant ("participantid", "trainingid", "role", "participantstatus")
VALUES (4, 2, 'player', 'maybe');
INSERT INTO trainingparticipant ("participantid", "trainingid", "role", "participantstatus")
VALUES (5, 2, 'player', 'no');

INSERT INTO trainingparticipant ("participantid", "trainingid", "role", "participantstatus")
VALUES (3, 3, 'player', 'no');
INSERT INTO trainingparticipant ("participantid", "trainingid", "role", "participantstatus")
VALUES (4, 3, 'player', 'yes');
INSERT INTO trainingparticipant ("participantid", "trainingid", "role", "participantstatus")
VALUES (5, 3, 'player', 'yes');

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


INSERT INTO tournament ("name", "street", "zipcode", "city", "date", "begintime", "endtime", "gettogethertime", "contact", "teamid")
VALUES ('Sommerturnier TSV', 'Grünwalderstr. 15a', '80000', 'München', '2017-06-12', '9:00:00', '13:00:00', '8:30:00',
        'Werner Lorant', 1);

CREATE TABLE tournamentparticipant (
  tournamentparticipantid SERIAL PRIMARY KEY,
  participantid           INT REFERENCES person (personid),
  tournamentid            INT REFERENCES tournament (tournamentid),
  role                    VARCHAR(10) NOT NULL,
  participantstatus       VARCHAR(5)  NOT NULL
);

INSERT INTO tournamentparticipant ("participantid", "tournamentid", "role", "participantstatus")
VALUES (1, 1, 'coach', 'yes');
INSERT INTO tournamentparticipant ("participantid", "tournamentid", "role", "participantstatus")
VALUES (2, 1, 'player', 'yes');


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

INSERT INTO teamevent ("name", "street", "zipcode", "city", "date", "begintime", "endtime", "gettogethertime", "teamid")
VALUES ('Abschlussfeier', 'Bienenheimstr. 15', '80000', 'München', '2017-07-22', '17:00:00', '19:00:00', '17:00:00', 1);

INSERT INTO teamevent ("name", "street", "zipcode", "city", "date", "begintime", "endtime", "gettogethertime", "teamid")
VALUES
  ('Weihnachtsfeier', 'Bienenheimstr. 15', '80000', 'München', '2017-12-18', '14:00:00', '17:00:00', '14:00:00', 1);

CREATE TABLE teameventparticipant (
  teameventparticipantid SERIAL PRIMARY KEY,
  participantid          INT REFERENCES person (personid),
  teameventid            INT REFERENCES teamevent (teameventid),
  role                   VARCHAR(10) NOT NULL,
  participantstatus      VARCHAR(5)  NOT NULL
);

INSERT INTO teameventparticipant ("participantid", "teameventid", "role", "participantstatus")
VALUES (1, 1, 'coach', 'yes');
INSERT INTO teameventparticipant ("participantid", "teameventid", "role", "participantstatus")
VALUES (2, 1, 'coach', 'yes');
INSERT INTO teameventparticipant ("participantid", "teameventid", "role", "participantstatus")
VALUES (3, 1, 'player', 'yes');
INSERT INTO teameventparticipant ("participantid", "teameventid", "role", "participantstatus")
VALUES (4, 1, 'player', 'maybe');
INSERT INTO teameventparticipant ("participantid", "teameventid", "role", "participantstatus")
VALUES (5, 1, 'player', 'no');

INSERT INTO teameventparticipant ("participantid", "teameventid", "role", "participantstatus")
VALUES (1, 2, 'coach', 'yes');
INSERT INTO teameventparticipant ("participantid", "teameventid", "role", "participantstatus")
VALUES (2, 2, 'coach', 'maybe');
INSERT INTO teameventparticipant ("participantid", "teameventid", "role", "participantstatus")
VALUES (3, 2, 'player', 'yes');
INSERT INTO teameventparticipant ("participantid", "teameventid", "role", "participantstatus")
VALUES (4, 2, 'player', 'maybe');
INSERT INTO teameventparticipant ("participantid", "teameventid", "role", "participantstatus")
VALUES (5, 2, 'player', 'yes');


# --- !Downs

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
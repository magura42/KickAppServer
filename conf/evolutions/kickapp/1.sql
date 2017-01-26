# Users SCHEMA

# --- !Ups

CREATE TABLE User(
    id bigint(20) NOT NULL AUTO_INCREMENT,
    firstname varchar(255) NOT NULL,
    lastname varchar(255) NOT NULL,
    email varchar(255) NOT NULL,
    PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE User;
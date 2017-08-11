KickApp Server
-----------------------

## Description

## Prerequisites
### SBT
http://www.scala-sbt.org/0.13/docs/Installing-sbt-on-Linux.html
### PostgreSQL
sudo apt-get install postgresql postgresql-contrib



## PostgreSQL
### Shell 
sudo -u postgres psql
### DB anlegen:
* sudo -i -u postgres
* Benutzer kickapp anlegen: createuser --interactive -P
* DB anlegen: createdb kickapp
* Port: 5432
* Account: kickapp/kickapp
* DB: kickapp
* Commands:
    * show dbs: \l
    * connect to db: \c kickapp
    * show relations: \d
    * show roles: \du

## Start application

sbt run

Debug: sbt -jvm-debug 5005


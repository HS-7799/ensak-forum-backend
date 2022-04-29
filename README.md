# ensak-forum-backend
backend side of ensak forum enterprise

## setup

Create a database with : 
```
create database forum
```

and run :
```
INSERT INTO roles(name) VALUES('ROLE_ADMIN');
INSERT INTO roles(name) VALUES('ROLE_ETUDIANT');
INSERT INTO roles(name) VALUES('ROLE_ENTREPRISE');
```

## Run

run the following:
```
mvn spring-boot:run
```

## packaging

run the following:
```
mvn install
```

now your local developpement server is running on 8080

you need to insert a user manuallay to be the administrator or you can send a  POST request to : /api/auth/signup

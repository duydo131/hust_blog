# Springboot Demo

## How To Run
### 1. Build Dọcker
```
docker-compose build
```
### 2. Run db, create database
```
docker-compose up -d db
docker exec -it db_spring bash
mysql -u root -p
create database demo_spring_boot;
```

### 3. Run zookeeper, kafka, api from docker-compose
```
docker-compose up
```

Server will run at localhost and listen on port 8080

# bookstore_myBackend

This project is the backend of online bookstore project. That's the main project of SE2321 and SE3353. 

## how to use

1. Because this project use kafka, you should first start kafka environment on your machine. 
Ref: https://kafka.apache.org/quickstart 

2. in Intellij IDE, start MyBackendApplication

3. the port and datasource configuration are in application.properties, you can change them to deploy on your own 
port and database. I used MySQL database.

4. my config: redis on 6379, backend on 8443, mysql on 3306

5. In order to use redis in windows, I use docker and map the redis port 6379 to localhost. 
```
docker run -d -p 6379:6379 redis
```




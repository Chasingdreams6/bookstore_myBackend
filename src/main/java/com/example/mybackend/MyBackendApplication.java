package com.example.mybackend;

import com.example.mybackend.utility.OrderEndpoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.websocket.server.ServerEndpointConfig;

@SpringBootApplication
public class MyBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyBackendApplication.class, args);
    }

}

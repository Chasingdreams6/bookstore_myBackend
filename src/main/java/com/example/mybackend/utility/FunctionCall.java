package com.example.mybackend.utility;


import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class FunctionCall {

    @Configuration
    public static class ApplicationConfig {
        @Bean
        public RestTemplate restTemplate(RestTemplateBuilder builder) {
            return builder.build();
        }
    }

    @Autowired
    private RestTemplate restTemplate;

    public Integer mul(String s) {
        String backend = "http://localhost:8089/mul";
        URI uri = UriComponentsBuilder.fromUriString(backend).buildAndExpand().toUri();
        RequestEntity<String> request = RequestEntity.post(uri).
                body(s);
        ResponseEntity<Integer> response = restTemplate.exchange(request, Integer.class);
        return response.getBody();
    }
}

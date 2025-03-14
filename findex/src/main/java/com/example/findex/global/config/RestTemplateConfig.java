package com.example.findex.global.config;

import java.time.Duration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplateBuilder()
        .connectTimeout(Duration.ofSeconds(3))
        .readTimeout(Duration.ofSeconds(3))
        .build();
  }
}

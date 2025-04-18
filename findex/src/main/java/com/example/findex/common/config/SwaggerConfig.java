package com.example.findex.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI findexAPI() {
    return new OpenAPI()
        .components(new Components())
        .info(info());
  }

  private Info info() {
    return new Info()
        .title("Findex API 문서")
        .description("Findex 프로젝트의 Swagger API 문서입니다.")
        .version("v0.0.1");
  }
}
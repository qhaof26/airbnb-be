package com.project.airbnb.configurations.RestTemplate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplateConfig restTemplateConfig(){
        return new RestTemplateConfig();
    }
}

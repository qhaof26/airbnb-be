package com.project.airbnb.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Cho phép tất cả các endpoint
                .allowedOrigins("http://127.0.0.1:5500")  // Cho phép từ domain này
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Các phương thức HTTP được phép
                .allowedHeaders("*")  // Cho phép tất cả header
                .allowCredentials(true);  // Cho phép gửi cookies hoặc headers xác thực
    }
}

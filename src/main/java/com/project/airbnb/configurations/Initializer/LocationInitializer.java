package com.project.airbnb.configurations.Initializer;

import com.project.airbnb.services.Location.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class LocationInitializer {
    private final LocationService provinceService;

    @Bean
    public ApplicationRunner applicationRunner(){
        return args -> {
            if(provinceService.getProvinces().isEmpty()){
                provinceService.fetchAndSaveProvinces();
                provinceService.fetchAndSaveDistricts();
                provinceService.fetchAndSaveWards();
                log.warn("Inside method init");
            }
        };
    }
}

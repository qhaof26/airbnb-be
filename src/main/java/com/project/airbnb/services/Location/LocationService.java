package com.project.airbnb.services.Location;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.airbnb.dtos.response.Location.DistrictDTO;
import com.project.airbnb.dtos.response.Location.ProvinceDTO;
import com.project.airbnb.dtos.response.Location.WardDTO;
import com.project.airbnb.exceptions.AppException;
import com.project.airbnb.exceptions.ErrorCode;
import com.project.airbnb.models.Location.District;
import com.project.airbnb.models.Location.Province;
import com.project.airbnb.models.Location.Ward;
import com.project.airbnb.repositories.DistrictRepository;
import com.project.airbnb.repositories.ProvinceRepository;
import com.project.airbnb.repositories.WardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationService implements ILocationService {
    private final RestTemplate restTemplate;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;

    @NonFinal
    @Value("${url.api-province}")
    private String apiProvince;

    @NonFinal
    @Value("${url.api-district}")
    private String apiDistrict;

    @NonFinal
    @Value("${url.api-ward}")
    private String apiWard;

    @Override
    @Transactional
    public void fetchAndSaveProvinces() {
        String response = restTemplate.getForObject(apiProvince, String.class);
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            ProvinceDTO[] provinceResponses = objectMapper.readValue(response, ProvinceDTO[].class);
            for (ProvinceDTO dto : provinceResponses) {
                Province province = new Province();
                province.setId(dto.getCode());
                province.setName(dto.getName());
                provinceRepository.save(province);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    @Transactional
    public void fetchAndSaveDistricts() {
        try {
            if(provinceRepository.count() > 0){
                log.warn("In method save district");
                ObjectMapper objectMapper = new ObjectMapper();
                String responseDistrict = restTemplate.getForObject(apiDistrict, String.class);
                DistrictDTO[] districtResponses = objectMapper.readValue(responseDistrict, DistrictDTO[].class);
                for (DistrictDTO dto : districtResponses) {
                    District district = new District();
                    district.setId(dto.getCode());
                    district.setName(dto.getName());
                    Province province = provinceRepository.findById(dto.getProvince_code()).orElseThrow(() -> new AppException(ErrorCode.PROVINCE_NOT_EXISTED));
                    district.setProvince(province);
                    districtRepository.save(district);
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void fetchAndSaveWards() {
        try {
            if(districtRepository.count() > 0){
                log.warn("In method save ward");
                ObjectMapper objectMapper = new ObjectMapper();
                String responseWard = restTemplate.getForObject(apiWard, String.class);
                WardDTO[] wardResponses = objectMapper.readValue(responseWard, WardDTO[].class);
                for (WardDTO dto : wardResponses) {
                    Ward ward = new Ward();
                    ward.setId(dto.getCode());
                    ward.setName(dto.getName());
                    District district = districtRepository.findById(dto.getDistrict_code()).orElseThrow(() -> new AppException(ErrorCode.DISTRICT_NOT_EXISTED));
                    ward.setDistrict(district);
                    wardRepository.save(ward);
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Province> getProvinces() {
        return provinceRepository.findAll();
    }

}

package com.project.airbnb.services.Location;

import com.project.airbnb.models.Province;

import java.util.List;

public interface ILocationService {
    void fetchAndSaveProvinces();
    void fetchAndSaveDistricts();
    void fetchAndSaveWards();
    List<Province> getProvinces();
}

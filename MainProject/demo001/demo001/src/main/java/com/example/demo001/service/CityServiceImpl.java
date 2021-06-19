package com.example.demo001.service;


import com.example.demo001.domain.Transport.City;
import com.example.demo001.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService{

    @Autowired
    private CityRepository cityRepository;

    @Override
    public City FindCityByName(String name){
        List<City> allCities = cityRepository.findAll();
        for (City allCity : allCities) {
            if (allCity.getCityName().equals(name))
                return allCity;
        }
        return null;
    }

}

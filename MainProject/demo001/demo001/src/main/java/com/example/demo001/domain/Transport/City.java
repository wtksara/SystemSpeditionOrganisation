package com.example.demo001.domain.Transport;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class City {

    @Id
    @GeneratedValue
    private Integer cityId;

    @Column
    private String cityName;

}

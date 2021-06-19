package com.example.demo001.domain.Transport;

import lombok.Data;
import javax.persistence.*;

/**
 * Representation of city
 * - may be used as source or destination of delivery.
 */
@Entity
@Data
public class City {

    /** City's autogenerated unique identification number */
    @Id
    @GeneratedValue
    private long cityId;

    /** Name of the city */
    @Column
    private String cityName;

}

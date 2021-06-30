package com.example.demo001.domain.Transport;

import lombok.Data;
import javax.persistence.*;

/**
 * Representation of connection between two cities
 * - describes the distance between connected cities.
 */
@Entity
@Data
public class Connection {

    /** Connection's autogenerated unique identification number */
    @Id
    @GeneratedValue
    private long connectionId;

    /** First city of the connection - may be both source and destination */
    @OneToOne
    private City firstCity;

    /** Second city of the connection - may be both source and destination */
    @OneToOne
    private City secondCity;

    /** Connection's length calculated manually and input to database */
    @Column
    private double connectionLength;

}
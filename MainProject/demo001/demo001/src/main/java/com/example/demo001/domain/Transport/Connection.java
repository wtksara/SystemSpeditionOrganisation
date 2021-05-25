package com.example.demo001.domain.Transport;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Connection {

    @Id
    @GeneratedValue
    private long connectionId;

    @OneToOne
    private City firstCity;

    @OneToOne
    private City secondCity;

    @Column
    private double connectionLength;

}

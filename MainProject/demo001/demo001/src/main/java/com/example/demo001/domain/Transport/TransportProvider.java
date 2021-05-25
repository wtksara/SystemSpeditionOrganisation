package com.example.demo001.domain.Transport;

import com.example.demo001.domain.Actors.Users;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
public class TransportProvider extends Users {

    @OneToMany
    private List<Connection> connections;

    @Column
    private double priceForKilometer;

    void acceptOrder(){}

    void rejectOrder(){}

    void actualizeOrderStatus(){}

}

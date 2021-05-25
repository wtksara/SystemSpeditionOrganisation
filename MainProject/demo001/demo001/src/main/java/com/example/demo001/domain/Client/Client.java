package com.example.demo001.domain.Client;

import com.example.demo001.domain.Actors.Users;
import com.example.demo001.domain.Transport.City;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToOne;


@Entity
@Data
public class Client extends Users {

    @OneToOne
    private City clientCity;

    void showProducts(){}

    void makeOrder(){}

    void editOrder(){}

    void showOrderData(){}

    void showOrderStatus(){}

    void showOrderHistory(){}

}

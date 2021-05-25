package com.example.demo001.domain.OrderManagement;

import com.example.demo001.domain.Actors.Users;
import com.example.demo001.domain.Client.Client;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
@Data
public class OrderManager extends Users {

    @OneToMany
    private List<OrderItem> orderItems;

    @OneToMany
    private List<Client> clients;

    @OneToMany
    private List<Orders> orders;

    void receiveOrder(){}

    void chooseTransportProvider(){}

    void editOrdersData(){}

    void getFactoryData(){}

    void getOrdersData(){}

    void getProductionPossibilities(){}

    void setFactory(){}

    void deleteFactory(){}

}

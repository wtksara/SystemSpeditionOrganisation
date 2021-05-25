package com.example.demo001.domain.OrderManagement;

import com.example.demo001.domain.Client.Client;
import com.example.demo001.domain.Products.Product;
import com.example.demo001.domain.Transport.TransportProvider;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Data
public class Orders {

    @Id
    @GeneratedValue
    private int orderId;

    @Column
    private OrderStatus orderStatus;

    @OneToOne
    private Client orderClient;

    @OneToOne
    private TransportProvider orderTransportProvider;

    @OneToMany
    final private List<OrderItem> orderedProducts = new ArrayList<>();

}

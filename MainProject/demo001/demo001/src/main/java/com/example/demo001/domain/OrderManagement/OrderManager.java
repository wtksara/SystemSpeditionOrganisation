package com.example.demo001.domain.OrderManagement;

import com.example.demo001.domain.Actors.BasicUser;
import com.example.demo001.domain.Client.Client;
import lombok.Data;
import javax.persistence.*;
import java.util.List;

/**
 * Representation of order manager user in the system
 * - is able to manage the orders.
 */
@Entity
@Data
public class OrderManager extends BasicUser {

    /** List of order items */
    @OneToMany
    private List<OrderItem> orderItems;

    /** List of all client users in the system */
    @OneToMany
    private List<Client> clients;

    /** List of all managed orders */
    @OneToMany
    private List<ProductOrder> orders;

}

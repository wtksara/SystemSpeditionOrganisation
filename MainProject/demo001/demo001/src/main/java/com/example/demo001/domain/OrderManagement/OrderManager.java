package com.example.demo001.domain.OrderManagement;

import com.example.demo001.domain.Actors.BasicUser;
import com.example.demo001.domain.Client.Client;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Representation of order manager user in the system
 * - is able to manage the orders.
 */

//@Entity
//@Data
//public class OrderManager extends BasicUser {
//
//    /** List of order items */
//    @OneToMany
//    @LazyCollection(LazyCollectionOption.FALSE)
//    private List<OrderItem> orderItems;
//
//    /** List of all client users in the system */
//    @OneToMany
//    @LazyCollection(LazyCollectionOption.FALSE)
//    private List<Client> clients;
//
//    /** List of all managed orders */
//    @OneToMany
//    @LazyCollection(LazyCollectionOption.FALSE)
//    private List<ProductOrder> orders;
//
//}
@Entity
@Data
public class OrderManager extends BasicUser {

    /** List of order items */
    @OneToMany(fetch = FetchType.EAGER)
    private Set<OrderItem> orderItems;

    /** List of all client users in the system */
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Client> clients;

    /** List of all managed orders */
    @OneToMany(fetch = FetchType.EAGER)
    private Set<ProductOrder> orders;

}
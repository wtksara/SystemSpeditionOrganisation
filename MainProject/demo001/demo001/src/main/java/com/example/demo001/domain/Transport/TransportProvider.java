package com.example.demo001.domain.Transport;

import com.example.demo001.domain.Actors.BasicUser;
import lombok.Data;
import javax.persistence.*;
import java.util.List;

/**
 * Representation of transport provider user in the system
 * - contains basic information about delivery services.
 */
@Entity
@Data
public class TransportProvider extends BasicUser {

    /** List of connections operated by transport provider */
    @OneToMany
    private List<Connection> connections;

    /** Price per kilometer for delivery */
    @Column
    private double priceForKilometer;

}

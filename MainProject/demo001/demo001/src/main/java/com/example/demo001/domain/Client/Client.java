package com.example.demo001.domain.Client;

import com.example.demo001.domain.Actors.BasicUser;
import com.example.demo001.domain.Transport.City;
import lombok.Data;
import javax.persistence.*;

/**
 * Representation of client user of the system
 * - is able to create, accept and browse orders.
 */
@Entity
@Data
public class Client extends BasicUser {

    /** City in which client is residing */
    @OneToOne
    private City clientCity;

}

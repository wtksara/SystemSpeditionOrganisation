package com.example.demo001.domain.Factory;

import com.example.demo001.domain.Actors.BasicUser;
import lombok.Data;
import javax.persistence.*;

/**
 * Representation of factory manager user in the system
 * - is able to manage the factory and warehouses.
 */
@Entity
@Data
public class FactoryManager extends BasicUser {

    /** Factory managed by user */
    @OneToOne
    private Factory managedFactory;

}

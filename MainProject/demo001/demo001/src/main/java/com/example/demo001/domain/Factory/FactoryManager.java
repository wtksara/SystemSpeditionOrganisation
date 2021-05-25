package com.example.demo001.domain.Factory;

import com.example.demo001.domain.Actors.Users;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.Map;

@Entity
@Data
public class FactoryManager extends Users {

    @OneToOne
    private Factory managedFactory;

    void addProduct(){}

    void deleteProduct(){}

    void editProduct(){}

    void getFactoryStatus(){}

    void acceptOrder(){}

    void rejectOrder(){}

    void getProductionStatistics(){}

}

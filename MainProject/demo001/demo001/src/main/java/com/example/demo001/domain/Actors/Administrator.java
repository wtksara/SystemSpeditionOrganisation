package com.example.demo001.domain.Actors;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
public class Administrator extends Users {

    @OneToMany
    private List<Users> users;

    Administrator(String userName) {
        super(userName);
    }

    public Administrator() {

    }

    void addUser(){}
    void editUserData(){}
    void deleteUser(){}

    void addFactory(){}
    void editFactoryData(){}
    void deleteFactory(){}

    void addTransportProvider(){}
    void editTransportProviderData(){}
    void deleteTransportProvider(){}

    void addOrderManager(){}
    void editOrderManager(){}
    void deleteOrderManager(){}

    void addAdministrator(){}
    void editAdministrator(){}
    void deleteAdministrator(){}

}

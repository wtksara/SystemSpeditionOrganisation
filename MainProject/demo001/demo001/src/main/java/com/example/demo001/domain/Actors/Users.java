package com.example.demo001.domain.Actors;


import org.hibernate.annotations.Columns;

import javax.persistence.*;
import lombok.Data;

@Entity
@Data
public abstract class Users {

    @Id
    @GeneratedValue
    protected long userId;

    @Column
    protected String userName;

    public Users() {

    }

    Users(String userName){
        this.userName = userName;
    }
}

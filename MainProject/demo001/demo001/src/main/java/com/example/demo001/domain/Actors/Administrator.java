package com.example.demo001.domain.Actors;

import lombok.Data;
import javax.persistence.*;
import java.util.List;

/**
 * Representation of administrative user of the system
 * - has access to information about users and permissions for modification user database.
 */
@Entity
@Data
public class Administrator extends BasicUser {

    /** List of all users of the system */
    @OneToMany
    private List<BasicUser> users;

    Administrator(String userName) {
        super(userName);
    }
    public Administrator() {}

}

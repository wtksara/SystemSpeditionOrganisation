package com.example.demo001.domain.Factory;


import com.example.demo001.domain.Transport.City;
import lombok.Data;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Data
public class Factory {

    @Id
    @GeneratedValue
    private Long factoryId;

    @Column
    private String factoryName;

    @OneToMany
    private List<ProductionAbility> producedProducts;

    @OneToOne
    private City factoryLocation;

    @OneToOne
    private FactoryManager factoryManager;

}

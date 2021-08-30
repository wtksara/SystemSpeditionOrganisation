package com.example.demo001.service;


import com.example.demo001.domain.Actors.BasicUser;
import com.example.demo001.domain.Client.Client;
import com.example.demo001.domain.Factory.FactoryManager;
import com.example.demo001.repository.FactoryManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class FactoryManagerServiceImpl implements FactoryManagerService{

    @Autowired
    private FactoryManagerRepository factoryManagerRepository;

    @Override
    public List<FactoryManager> findAll(){
        return factoryManagerRepository.findAll();
    }

    @Override
    public FactoryManager findByUsername(String username){
        List<FactoryManager> allBasicUsers = factoryManagerRepository.findAll();
        for (FactoryManager allBasicUser : allBasicUsers) {
            if (Objects.equals(allBasicUser.getUserName(), username))
                return allBasicUser;
        }
        return null;
    }

    @Override
    public boolean addUser(FactoryManager bUser)
    {
        FactoryManager bb = factoryManagerRepository.save(bUser);
        return bb != null;
    }

    @Override
    public boolean saveChangedUser(FactoryManager bUser)
    {
        FactoryManager bb = factoryManagerRepository.save(bUser);
        return bb != null;
    }

}

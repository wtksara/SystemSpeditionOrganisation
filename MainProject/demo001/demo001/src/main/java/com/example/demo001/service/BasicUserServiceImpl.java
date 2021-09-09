package com.example.demo001.service;


import com.example.demo001.domain.Actors.BasicUser;
import com.example.demo001.repository.BasicUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class BasicUserServiceImpl implements BasicUserService{

    @Autowired
    private BasicUserRepository basicUserRepository;

    @Override
    public List<BasicUser> findAll(){
        return basicUserRepository.findAll();
    }

    @Override
    public BasicUser findByUsername(String username){
        List<BasicUser> allBasicUsers = basicUserRepository.findAll();
        for (BasicUser allBasicUser : allBasicUsers) {
            if (Objects.equals(allBasicUser.getUserName(), username))
                return allBasicUser;
        }
        return null;
    }

    @Override
    public BasicUser deleteByUsername(String username){
        List<BasicUser> allBasicUsers = basicUserRepository.findAll();
        BasicUser bUser;
        for (BasicUser allBasicUser : allBasicUsers) {
            if (allBasicUser.getUserName().equals(username)) {
                bUser = allBasicUser;
                bUser.setActive(false);
                basicUserRepository.save(bUser);
                return bUser;
            }
        }
        return null;
    }

    @Override
    public boolean addUser(BasicUser bUser){
        BasicUser bb = basicUserRepository.save(bUser);
        return bb != null;
    }

    @Override
    public boolean saveChangedUser(BasicUser bUser){
        BasicUser bb = basicUserRepository.save(bUser);
        return bb != null;
    }

}

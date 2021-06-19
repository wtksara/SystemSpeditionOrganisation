package com.example.demo001.service;


import com.example.demo001.domain.Actors.BasicUser;
import com.example.demo001.domain.Transport.City;

import java.util.List;

public interface BasicUserService {
      List<BasicUser> findAll();
      BasicUser findByUsername(String username);
      BasicUser deleteByUsername(String username);
      boolean addUser(BasicUser bUser);
      boolean saveChangedUser(BasicUser bUser);
}

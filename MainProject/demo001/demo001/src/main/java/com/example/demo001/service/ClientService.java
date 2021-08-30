package com.example.demo001.service;

import com.example.demo001.domain.Client.Client;

import java.util.List;

public interface ClientService {
      Client findByUsername(String username);
      boolean addUser(Client bUser);
      boolean saveChangedUser(Client bUser);
}

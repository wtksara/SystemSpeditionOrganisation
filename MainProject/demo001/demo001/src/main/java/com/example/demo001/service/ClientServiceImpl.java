package com.example.demo001.service;


import com.example.demo001.domain.Actors.BasicUser;
import com.example.demo001.domain.Client.Client;
import com.example.demo001.repository.BasicUserRepository;
import com.example.demo001.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ClientServiceImpl implements ClientService{

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public Client findByUsername(String username)
    {
        List<Client> allBasicUsers = clientRepository.findAll();
        for (Client allBasicUser : allBasicUsers) {
            if (Objects.equals(allBasicUser.getUserName(), username))
                return allBasicUser;
        }
        return null;
    }

    @Override
    public boolean addUser(Client bUser)
    {
        Client bb = clientRepository.save(bUser);
        return bb != null;
    }

    @Override
    public boolean saveChangedUser(Client bUser)
    {
        Client bb = clientRepository.save(bUser);
        return bb != null;
    }

}

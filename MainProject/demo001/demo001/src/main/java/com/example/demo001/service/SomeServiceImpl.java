package com.example.demo001.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class SomeServiceImpl implements SomeService{

    /*@Autowired
    private ElementRepository elementRepository;*/

    private static final Logger LOG = LoggerFactory.getLogger(SomeServiceImpl.class) ;

    @Override
    public String Service1(Integer i) {
        LOG.info("Service1 got" + i.toString());
        return i.toString();
    }

    /*@Override
    public Element Service2(Long i) {

        Optional<Element> out = elementRepository.findById(1L);

        if (out.isEmpty())
            return null;
        else
            return  out.get();
    }*/
}

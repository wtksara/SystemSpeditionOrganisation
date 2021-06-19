package  com.example.demo001.controller;

import com.example.demo001.service.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SimpleCtrl {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleCtrl.class) ;

    @Autowired
    private SomeService someService;

    @Autowired
    private ManageFactoriesLives manageFactoriesLives;

    public void setupFactories(){
        manageFactoriesLives.createFactory("Test factory 1");
        manageFactoriesLives.createFactory("Test factory 2");
        manageFactoriesLives.createFactory("Test factory 3");
        manageFactoriesLives.createFactory("Test factory 4");
    }

    @GetMapping(value="/ping")
    public String Meth1()
    {
        LOG.info("Meth1");
        String out =  someService.Service1(3);
        return out;
     }

   /* @GetMapping(value="/element")
    public Element Meth2()
    {
        LOG.info("Meth2");
        Element out =  someService.Service2 (1L);
        return out;
    }*/
}


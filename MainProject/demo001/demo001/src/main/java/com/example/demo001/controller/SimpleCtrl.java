package  com.example.demo001.controller;

import com.example.demo001.service.SomeService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

//@RestController
public class SimpleCtrl {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleCtrl.class) ;

    @Autowired
    private SomeService someService;

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


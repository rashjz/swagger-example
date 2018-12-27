package rashjz.info.app.sw.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import rashjz.info.app.sw.respositories.PersonRepository;
import rashjz.info.app.sw.util.SecurityCredential;

import java.security.Principal;
import java.util.Map;

@Slf4j
@Controller
public class MyController {
    private final PersonRepository personRepository;

    @Autowired
    public MyController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping("/")
    public String init(Map<String, Object> model, Principal principal) {
        model.put("title", "PUBLIC AREA");
        model.put("message", "Only Authorised user can view this page");
        model.put("username", SecurityCredential.getUserName(principal));
        return "home";
    }


}
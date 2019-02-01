package rashjz.info.app.sw.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import rashjz.info.app.sw.util.SecurityCredential;

import java.security.Principal;
import java.util.Map;

@Slf4j
@Controller
public class MyController {

    @GetMapping("/")
    public String init(Map<String, Object> model, Principal principal) {
        model.put("title", "PUBLIC AREA");
        model.put("message", "Only Authorised user can view this page");
        model.put("username", SecurityCredential.getUserName(principal));
        model.put("userroles", SecurityCredential.getUserRoles(principal));
        return "home";
    }

    @GetMapping("/international")
    public String getInternationalPage() {
        return "international";
    }
}
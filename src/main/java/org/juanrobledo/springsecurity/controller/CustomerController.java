package org.juanrobledo.springsecurity.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("v1")
public class CustomerController {
    private SessionRegistry sessionRegistry;

    @GetMapping("/index")
    public String index(){
        return "HolaMundo";
    }

    @GetMapping("/index2")
    public String index2(){
        return "HolaMundo no seguro";
    }

    @GetMapping("/session")
    public ResponseEntity<?> getDetailsSession(){
        String sessionId = "";
        User userObjer = null;

        List<Object> listaUsuario = sessionRegistry.getAllPrincipals();

        for (Object session : listaUsuario) {
            if (session instanceof User) {
                userObjer = (User) session;
            }
            List<SessionInformation> sessionInformations = sessionRegistry.getAllSessions(session, false);
            for (SessionInformation sessionInformation : sessionInformations) {
                if (sessionId.equals(sessionInformation.getSessionId())) {
                    sessionId = sessionInformation.getSessionId();
                }
            }
        }

        Map<String,Object> response = new HashMap<>();
        response.put("sessionId",sessionId);
        response.put("user",userObjer);


        return ResponseEntity.ok(response);
    }
}

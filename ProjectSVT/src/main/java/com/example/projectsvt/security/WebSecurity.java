package com.example.projectsvt.security;

import com.example.projectsvt.model.User;
import com.example.projectsvt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

//** Komponenta koja moze da obavlja dodatnu proveru zahteva pre nego sto dospe na endpoint.
// Moguce je pristupiti @PathVariable podacima sa URL-a zahteva na endpoint, poput {id}.
// https://docs.spring.io/spring-security/site/docs/5.2.11.RELEASE/reference/html/authorization.html
@Component
@RequiredArgsConstructor
public class WebSecurity {

    private final UserService userService;

    public boolean checkClubId(Authentication authentication, HttpServletRequest request, int id) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userService.findByUsername(userDetails.getUsername());
            if(id == user.getId()) {
                return true;
            }
            return false;
        }
}

package org.nurfet.hotelchain.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class HomeController {

    @GetMapping
    public String home(@SessionAttribute(required = false, name = "Authentication-Name")String authenticationName,
                       HttpSession session, Model model) {

        if (authenticationName != null) {
            model.addAttribute("authenticationName", authenticationName);
            session.removeAttribute("Authentication-Name");
        }

        return "index";
    }
}

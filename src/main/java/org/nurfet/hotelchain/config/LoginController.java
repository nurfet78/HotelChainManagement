package org.nurfet.hotelchain.config;

import jakarta.servlet.http.HttpSession;
import org.nurfet.hotelchain.exception.LoginException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(@SessionAttribute(required = false, name = "Authentication-Exception") LoginException loginException,
                        @SessionAttribute(required = false, name = "Authentication-Name")String authenticationName,
                        HttpSession session, Model model) {

        if (loginException != null) {
            model.addAttribute("authenticationException", loginException);
            session.removeAttribute("Authentication-Exception");
        } else {
            model.addAttribute("authenticationException", null);
        }

        if (authenticationName != null) {
            model.addAttribute("authenticationName", authenticationName);
            session.removeAttribute("Authentication-Name");
        }

        return "login-page";
    }
}

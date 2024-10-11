package org.nurfet.hotelchain.controller;


import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/accessDenied")
public class ErrorController {

    @GetMapping
    public String error(HttpSession session, Model model) {
        String exception = session.getAttribute("exception").toString();
        String message = session.getAttribute("message").toString();
        String URL = session.getAttribute("URL").toString();
        String timestamp = session.getAttribute("timestamp").toString();

        model.addAttribute("exception", exception);
        model.addAttribute("message", message);
        model.addAttribute("URL", URL);
        model.addAttribute("timestamp", timestamp);

        return "error/access-denied";
    }
}

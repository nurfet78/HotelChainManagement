package org.nurfet.hotelchain.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        request.getSession().setAttribute("exception", accessDeniedException.getMessage());
        request.getSession().setAttribute("message", "У вас нет разрешения на доступ к" +
                request.getRequestURI() + " странице на этом сервере.");
        request.getSession().setAttribute("URL", request.getRequestURL().toString());
        request.getSession().setAttribute("timestamp", DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                .withZone(ZoneId.systemDefault()).format(Instant.now()));

        response.sendRedirect("/accessDenied");

    }
}

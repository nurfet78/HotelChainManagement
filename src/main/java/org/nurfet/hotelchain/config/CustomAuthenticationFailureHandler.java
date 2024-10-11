package org.nurfet.hotelchain.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.nurfet.hotelchain.exception.LoginException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        if (isAllowSessionCreation()) {
            LoginException loginException = new LoginException("Имя пользователя или пароль неверны");
            request.getParameterMap().forEach((key, value) -> {
                if (key.equals("username")) {
                    loginException.setUsername(value[0]);
                } else if (key.equals("password")) {
                    loginException.setPassword(value[0]);
                }
            });

            request.getSession().setAttribute("Authentication-Exception", loginException);
        }

        super.onAuthenticationFailure(request, response, exception);
    }
}

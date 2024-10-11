package org.nurfet.hotelchain.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginException {

    private String message;

    private String username;

    private String password;

    public LoginException(final String message) {
        this.message = message;
    }
}

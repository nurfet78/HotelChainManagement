package org.nurfet.hotelchain.util;

import org.springframework.stereotype.Component;

@Component
public class RoleUtil {

    public String getRoleName(String role) {
        StringBuilder res = new StringBuilder();

        res.append(role);

        return res.substring(5);
    }
}

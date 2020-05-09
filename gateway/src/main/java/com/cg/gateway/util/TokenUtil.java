package com.cg.gateway.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TokenUtil {
    private static Logger Log = LoggerFactory.getLogger(TokenUtil.class);

    public static String generateToken(String id, String password, String role) {
        String simpleToken = id + "," + password + "," + role;
        return simpleToken;
    }

    public static DecodedToken decode(String token) {
        try {
            DecodedToken decoded = new DecodedToken();
            String parts[] = token.split(",");
            decoded.setId(parts[0]);
            decoded.setPassword(parts[1]);
            decoded.setRole(parts[2]);
            return decoded;
        } catch (Exception e) {
            Log.error("exception while decoding token=" + token);
            return null;
        }
    }


}

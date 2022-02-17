package com.sayan.webclient.services;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class CookieService {

    @Value("@{env}")
    private String env;

    public String getCookieValueByName(final Cookie[] cookies, final String name) {
        if (cookies != null && cookies.length > 0) {
            return Arrays.stream(cookies).filter(c -> c.getName().equals(name)).map(c -> c.getValue())
                    .collect(Collectors.joining());
        }
        return null;
    }

    public void addCookie(final HttpServletResponse response, final String name, final String value,
                          final String path) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(path);
        cookie.setHttpOnly(true);
        cookie.setSecure(env.equals("local"));
        response.addCookie(cookie);
    }

}

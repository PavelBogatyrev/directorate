package com.luxoft.horizon.dir.controllers;

import com.luxoft.horizon.dir.Config;
import com.luxoft.horizon.dir.security.ADAuthenticationService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import java.text.MessageFormat;
import java.util.*;


@RestController
@RequestMapping("/user")
public class UserController {

    public static final Logger logger = LoggerFactory.getLogger(UserController.class);
    public static final String USER = "user";
    public static final String ADMIN = "admin";

    private final Map<String, List<String>> userDb = new HashMap<>();

    @Autowired
    private ADAuthenticationService authenticationService;

    @Autowired
    private Config config;

    @PostConstruct
    public void config() {
        for (String account : config.getAdminAccounts()) {
            userDb.put(account, Arrays.asList(ADMIN, USER));
        }
        for (String account : config.getUserAccounts()) {
            userDb.put(account, Arrays.asList(USER));
        }

    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public LoginResponse login(@RequestBody final UserLogin login) throws ServletException {

        logger.debug(MessageFormat.format("Authenticating user {0}", login));

        Set<String> roles = new HashSet<>();

        //TODO Refactor this hack
        if (userDb.containsKey(login.getName())) {
            return new LoginResponse(Jwts.builder()
                    .setSubject(login.name)
                    .claim("roles", userDb.get(login.getName()))
                    .setIssuedAt(new Date())
                    .signWith(SignatureAlgorithm.HS256, "secretkey")
                    .compact()
            );
        }

        // TODO Check entities pattern
        String domain = login.getName().split("\\\\")[0];
        String user = login.getName().split("\\\\")[1];
        String password = login.getPassword();


        // Authenticate
        if (!authenticationService.authenticate(user, domain, password)) {
            throw new ServletException("Access denied");
        }

        logger.debug(MessageFormat.format("Authentication successful, processing authorization", login.getName()));

        if (authenticationService.isUser(user)) {
            roles.add(USER);
        }

        if (authenticationService.isAdmin(user)) {
            roles.add(USER);
            roles.add(ADMIN);
        }

        return new LoginResponse(Jwts.builder()
                .setSubject(login.name)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "secretkey")
                .compact()
        );
    }

    @SuppressWarnings("unused")
    private static class UserLogin {
        public String name;
        public String password;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("UserLogin{");
            sb.append("name='").append(name).append('\'');
            sb.append(", password='").append(password).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    @SuppressWarnings("unused")
    private static class LoginResponse {
        public String token;

        public LoginResponse(final String token) {
            this.token = token;
        }
    }
}

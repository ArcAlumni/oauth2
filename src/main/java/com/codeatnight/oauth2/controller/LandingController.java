package com.codeatnight.oauth2.controller;

import com.codeatnight.oauth2.core.IdentityProvider;
import com.codeatnight.oauth2.core.UserInfo;
import com.codeatnight.oauth2.plugins.Github;
import com.codeatnight.oauth2.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.*;
import java.util.HashMap;

@RestController
public class LandingController {

    @Autowired
    private Environment environment;

    @Autowired
    IdentityProvider identityProvider;

    @GetMapping("/login")
    UserInfo landing(HttpServletRequest httpRequest, @RequestParam(value = "idp", defaultValue = "github") String idp, @RequestParam("code") String code) throws Exception {
        String token = identityProvider.getToken(code, environment.getProperty("oauth2.github.client-id"), environment.getProperty("oauth2.github.client-secret"), httpRequest.getRequestURL().toString());
        UserInfo userInfo = identityProvider.getUserInfo(token);
        return userInfo;
    }

}

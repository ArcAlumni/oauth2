package com.codeatnight.oauth2.core;

import org.springframework.stereotype.Component;

@Component
public interface IdentityProvider {

    String getToken(String code, String clientId, String clientSecret, String redirectUrl);

    UserInfo getUserInfo(String token);

}

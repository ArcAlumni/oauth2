package com.codeatnight.oauth2.plugins;

import com.codeatnight.oauth2.core.IdentityProvider;
import com.codeatnight.oauth2.core.UserInfo;
import com.codeatnight.oauth2.util.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * https://docs.github.com/en/developers/apps/building-oauth-apps/creating-an-oauth-app
 */
@Component
public class Github implements IdentityProvider {

    @Autowired
    private HttpUtil httpUtil;

    static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String getToken(String code, String clientId, String clientSecret, String redirectUrl) {
        String response = null;
        try {
            StringBuilder url = new StringBuilder("https://github.com/login/oauth/access_token");
            url.append("?client_id=").append(clientId).append("&")
                    .append("client_secret=").append(clientSecret).append("&")
                    .append("code=").append(code).append("&")
                    .append("redirect_uri=").append(redirectUrl);
            response = httpUtil.post(url.toString(), new HashMap<>());
        } catch (Exception e) {
            throw new RuntimeException("Error connecting url");
        }
        String token = null;
        String[] arr = response.split("&");
        for (String s : arr) {
            String[] sArr = s.split("=");
            if (sArr[0].equals("access_token")) {
                token = sArr[1];
                break;
            }
        }
        return token;
    }

    @Override
    public UserInfo getUserInfo(String token) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "token " + token);
        try {
            String response = httpUtil.get("https://api.github.com/user", headers);
            return objectMapper.readValue(response, UserInfo.class);
        } catch (Exception e) {
            throw new RuntimeException("Error connecting url");
        }
    }

}

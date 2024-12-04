package alatoo.edu.kg.api.service;

import alatoo.edu.kg.store.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final User user;
    private final Map<String, Object> attributes;
    @Getter
    private final String token;

    public CustomOAuth2User(User user, Map<String, Object> attributes, String token) {
        this.user = user;
        this.attributes = attributes;
        this.token = token;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }

    @Override
    public String getName() {
        return user.getEmail();
    }
}
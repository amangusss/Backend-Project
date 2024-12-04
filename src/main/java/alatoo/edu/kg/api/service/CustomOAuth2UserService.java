package alatoo.edu.kg.api.service;

import alatoo.edu.kg.api.utils.JwtUtils;
import alatoo.edu.kg.store.entity.User;
import alatoo.edu.kg.store.enums.user.Roles;
import alatoo.edu.kg.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws org.springframework.security.oauth2.core.OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        String email = oauth2User.getAttribute("email");
        if (email == null || email.isBlank()) {
            throw new org.springframework.security.oauth2.core.OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }

        Optional<User> userOptional = userRepository.findByEmail(email);

        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            if (!user.isEnabled()) {
                user.setEnabled(true);
                userRepository.save(user);
            }
        } else {
            user = User.builder()
                    .username(email)
                    .email(email)
                    .password("")
                    .enabled(true)
                    .provider(userRequest.getClientRegistration().getRegistrationId())
                    .roles(Collections.singleton(Roles.USER))
                    .build();
            userRepository.save(user);
        }

        String token = jwtUtils.generateToken(user);

        return new CustomOAuth2User(user, oauth2User.getAttributes(), token);
    }
}

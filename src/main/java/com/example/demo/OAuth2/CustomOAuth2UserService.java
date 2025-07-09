package com.example.demo.OAuth2;

import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Credentials;
import com.example.demo.repository.CredentialsRepository;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
  
    @Autowired
    private CredentialsRepository credentialsRepository;
   
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User googleUser = super.loadUser(userRequest);
        String email = googleUser.getAttribute("email");

        Optional<Credentials> optionalCred = credentialsRepository.findByEmail(email);

        if (optionalCred.isPresent()) {
            Credentials existing = optionalCred.get();         
            String springRole = existing.getRole();           
            return new org.springframework.security.oauth2.core.user.DefaultOAuth2User(
                AuthorityUtils.createAuthorityList(springRole),
                Map.of("email", email, "name", googleUser.getAttribute("name")),
                "email"
            );
        } else {
            Credentials newCred = new Credentials();
            newCred.setEmail(email);
            newCred.setRole("USER");  
            newCred.setPassword("");
            credentialsRepository.save(newCred);

            return new org.springframework.security.oauth2.core.user.DefaultOAuth2User(
                AuthorityUtils.createAuthorityList("USER"),
                Map.of("email", email, "name", googleUser.getAttribute("name")),
                "email"
            );
        }
    }
}
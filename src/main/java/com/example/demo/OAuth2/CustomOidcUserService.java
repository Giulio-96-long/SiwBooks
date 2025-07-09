package com.example.demo.OAuth2;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.*;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.*;
import org.springframework.stereotype.Service;
import com.example.demo.entity.Credentials;
import com.example.demo.repository.CredentialsRepository;


@Service
public class CustomOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final CredentialsRepository credentialsRepository;

    public CustomOidcUserService(CredentialsRepository credentialsRepository) {
        this.credentialsRepository = credentialsRepository;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
       
        OidcUserService delegate = new OidcUserService();
        OidcUser oidcUser = delegate.loadUser(userRequest);

        String email = oidcUser.getAttribute("email");
        if (email == null) {
            throw new OAuth2AuthenticationException("Google non ha restituito l'email");
        }

        Optional<Credentials> optionalCred = credentialsRepository.findByEmail(email);
        Credentials creds;
        boolean isNew = false;
        if (optionalCred.isPresent()) {
            creds = optionalCred.get();
        } else {
            creds = new Credentials();
            creds.setEmail(email);
            creds.setRole("USER");  
            creds.setPassword("");
            credentialsRepository.save(creds);
            isNew = true;
        }

        String dbRole = creds.getRole();          

        Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
        mappedAuthorities.add(new SimpleGrantedAuthority(dbRole));
       
        return new DefaultOidcUser(
            mappedAuthorities,
            oidcUser.getIdToken(),
            oidcUser.getUserInfo(),
            "email" 
        );
    }
}
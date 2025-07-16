package com.example.demo.OAuth2;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.*;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.*;
import org.springframework.stereotype.Service;
import com.example.demo.entity.Credentials;
import com.example.demo.entity.User;
import com.example.demo.repository.CredentialsRepository;
import com.example.demo.repository.UserRepository;


@Service
public class CustomOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final CredentialsRepository credentialsRepository;
    private final UserRepository userRepository;

    public CustomOidcUserService(CredentialsRepository credentialsRepository, UserRepository userRepository) {
        this.credentialsRepository = credentialsRepository;
		this.userRepository = userRepository;
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
       
        if (optionalCred.isPresent()) {
            creds = optionalCred.get();
        } else {
        	User user = new User();
        	user.setFirstName(oidcUser.getAttribute("given_name"));
        	user.setLastName(oidcUser.getAttribute("family_name"));
            creds = new Credentials();
            creds.setUser(user);
            creds.setEmail(email);
            creds.setRole("USER");  
            creds.setPassword("oauth2-user"); 
            user.setCredentials(creds);
            userRepository.save(user); 
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
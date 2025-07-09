package com.example.demo.configuration;

import javax.sql.DataSource;
import com.example.demo.OAuth2.CustomOidcUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {   

	@Autowired
	private DataSource dataSource;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
    private CustomOidcUserService customOidcUserService;
    	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
				.usersByUsernameQuery("SELECT email, password, true FROM credentials WHERE email=?")
				.authoritiesByUsernameQuery("SELECT email, role FROM credentials WHERE email=?")
				.passwordEncoder(passwordEncoder);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth					
				
				// Tutte le altre GET su /books/** (incluso /books/{id}) sono aperte a tutti
				.requestMatchers(HttpMethod.GET, "/books", "/books/*", "/books/**").permitAll()			
		          
				// Solo l'ADMIN può accedere al form di creazione o modifica libri
				.requestMatchers(HttpMethod.GET, "/books/new", "/books/edit/**").hasAuthority("ADMIN")
				.requestMatchers(HttpMethod.POST, "/books/**").hasAuthority("ADMIN")
				.requestMatchers(HttpMethod.DELETE, "/books/**").hasAuthority("ADMIN")

				// /authors …
				.requestMatchers(HttpMethod.GET, "/authors/**").permitAll()
				.requestMatchers(HttpMethod.GET, "/authors/new", "/authors/edit/**").hasAuthority("ADMIN")
				.requestMatchers(HttpMethod.POST, "/authors/**").hasAuthority("ADMIN")
				.requestMatchers(HttpMethod.DELETE, "/authors/**").hasAuthority("ADMIN")

				// recensioni: POST solo a USER|ADMIN, GET aperto a tutti, DELETE solo ADMIN
				.requestMatchers(HttpMethod.DELETE, "/reviews/**").hasAuthority("ADMIN")
				.requestMatchers(HttpMethod.POST, "/reviews/books/**").hasAnyAuthority("USER", "ADMIN")
				.requestMatchers(HttpMethod.GET, "/reviews/**").permitAll()

				// tutte le altre rotte di login/register/static…
				.requestMatchers("/", "/home", "/login", "/register", "/css/**", "/js/**", "/images/**", "/webjars/**", "/.well-known/**",
						"/oauth2/authorization/**", "/login/oauth2/code/**", "/oauth2/**")
				.permitAll()

				// infine qualsiasi altra rotta richiede autenticazione
				.anyRequest().authenticated())
		
				.formLogin(form -> form
					    .loginPage("/login")
					    .usernameParameter("email")
					    .passwordParameter("password")
					    .defaultSuccessUrl("/default", true)
					    .failureUrl("/login?error=true")
					    .permitAll())
				
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/home")
						.invalidateHttpSession(true)
						.deleteCookies("JSESSIONID")
						.permitAll())

				.oauth2Login(oauth2 -> oauth2
			              .loginPage("/login")
			              .userInfoEndpoint(userInfo -> userInfo			                
			                  .oidcUserService(customOidcUserService)
			              )
			              .defaultSuccessUrl("/books", true)
			          );
		
		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}  

}

package com.example.diploma.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class WebSecurityConfig {

	private static final String[] AUTH_WHITELIST = {
			"/swagger-resources/**",
			"/swagger-ui.html",
			"/v3/api-docs",
			"/webjars/**",
			"/login",
			"/register",
			"/ads"
	};

	@Bean
	public JdbcUserDetailsManager jdbcUserDetailsManager(AuthenticationManagerBuilder auth, DataSource dataSource) throws Exception {
		return auth.jdbcAuthentication()
				.passwordEncoder(passwordEncoder()).dataSource(dataSource)
				.usersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username = ?")
				.authoritiesByUsernameQuery("SELECT username, user_role FROM users WHERE username = ?")
				.getUserDetailsService();
	}


	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.authorizeHttpRequests(
						authorization ->
								authorization
										.mvcMatchers(AUTH_WHITELIST)
										.permitAll()
										.mvcMatchers("/ads/**", "/users/**")
										.authenticated()
				)
				.cors()
				.and()
				.httpBasic(withDefaults());
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}

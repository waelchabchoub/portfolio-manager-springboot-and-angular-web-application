package com.wael.chabchoub.portfoliomanager.security.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.wael.chabchoub.portfoliomanager.security.filter.JwtAuthenticationFilter;
import com.wael.chabchoub.portfoliomanager.security.jwt.JwtUtils;
import com.wael.chabchoub.portfoliomanager.security.ratelimit.RateLimitFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtUtils jwtUtils;

	/** Reverse proxies in front of the app that append to X-Forwarded-For (used to find the client IP). */
	@Value("${portfoliomanager.rate-limit.trusted-proxies:1}")
	private int trustedProxies;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class).build();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(withDefaults()).csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling(ex -> ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						// Admin-only: need a valid JWT
						.requestMatchers(HttpMethod.POST, "/auth/change-password", "/api/skill/save",
								"/api/userinfo/save")
						.authenticated().requestMatchers(HttpMethod.DELETE, "/api/skill/delete/**").authenticated()
						// Public API used by the website
						.requestMatchers(HttpMethod.POST, "/auth/login", "/api/emails/send").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/skill/get", "/api/userinfo/get", "/api/lang-config",
								"/api/translation-files")
						.permitAll()
						// Angular app: index.html, bundles, assets and client-side routes
						.requestMatchers(HttpMethod.GET, "/", "/*", "/assets/**").permitAll()
						.requestMatchers("/error").permitAll()
						// Everything else is closed by default
						.anyRequest().authenticated())
				.addFilterBefore(new RateLimitFilter(trustedProxies), UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(new JwtAuthenticationFilter(jwtUtils), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
	
	@Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
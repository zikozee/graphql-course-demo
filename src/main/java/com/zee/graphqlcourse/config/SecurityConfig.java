package com.zee.graphqlcourse.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 19 Sep, 2024
 */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${spring.jwks.uri}")
    private String jwksUri;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthenticationConverter jwtAuthenticationConverter,
                                                   JwtDecoder jwtDecoder) throws Exception {

        http.oauth2ResourceServer(resourceCustomizer ->
                resourceCustomizer
                        .jwt(jwtCustomizer ->
                                jwtCustomizer.jwkSetUri(jwksUri)
//                                        .jwtAuthenticationConverter(new CustomJwtAuthenticationTokenConverter())
                                        .decoder(jwtDecoder)
                                        .jwtAuthenticationConverter(jwtAuthenticationConverter)
                        )
        );

        //todo explain this as to why you should permit all: https://docs.spring.io/spring-graphql/reference/security.html
        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(jwksUri).build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> authorities = jwt.getClaim("authorities");
            return authorities.stream().map(SimpleGrantedAuthority::new).collect(toList());
        });

        return jwtAuthenticationConverter;
    }
}

package com.zee.graphqlcourse.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 16 Sep, 2024
 */

@Configuration
public class ClientConfig {

    @Bean
    public HttpGraphQlTester httpGraphQlTester() {

        WebTestClient client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8080/graphql")
                .defaultHeader(HttpHeaders.AUTHORIZATION, accessTokenResponse().tokenType() + " " + accessTokenResponse().accessToken())
                .build();

        return HttpGraphQlTester.create(client);
    }


    private AccessTokenResponse accessTokenResponse(){

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "client_credentials");
        formData.add("client_id", "client");
        formData.add("client_secret", "client");
        formData.add("scope", "profile");

        RestClient restClient = RestClient.create();

        return restClient.post().uri("http://localhost:8099/oauth2/token")
                .body(formData)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString(("client" + ":" + "secret").getBytes()))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .retrieve()
                .body(AccessTokenResponse.class);

    }


    public record AccessTokenResponse(
            @JsonProperty("access_token")
            String accessToken,
            @JsonProperty("token_type")
            String tokenType) {}
}

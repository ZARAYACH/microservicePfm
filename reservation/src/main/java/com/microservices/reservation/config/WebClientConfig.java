package com.microservices.reservation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {


    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository registrations,
            OAuth2AuthorizedClientService clientService) {

        OAuth2AuthorizedClientProvider provider = OAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials()
                .build();

        AuthorizedClientServiceOAuth2AuthorizedClientManager manager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(registrations, clientService);
        manager.setAuthorizedClientProvider(provider);

        return manager;
    }

    @Bean
    public WebClient webClient(OAuth2AuthorizedClientManager manager) {
        return WebClient.builder()
                .filter((request, next) -> {
                    OAuth2AuthorizeRequest authRequest = OAuth2AuthorizeRequest
                            .withClientRegistrationId("reservation-service-client")
                            .principal("reservationService")
                            .build();

                    OAuth2AuthorizedClient client = manager.authorize(authRequest);
                    if (client == null) {
                        throw new IllegalStateException("Cannot obtain access token");
                    }

                    return next.exchange(
                            ClientRequest.from(request)
                                    .header("Authorization", "Bearer " + client.getAccessToken().getTokenValue())
                                    .build()
                    );
                })
                .build();
    }


}

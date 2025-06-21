package com.microservices.payment.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class WebClientConfig {

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository registrations,
            OAuth2AuthorizedClientRepository repository) {

        OAuth2AuthorizedClientProvider provider = OAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials()
                .build();

        DefaultOAuth2AuthorizedClientManager manager =
                new DefaultOAuth2AuthorizedClientManager(registrations, repository);
        manager.setAuthorizedClientProvider(provider);

        return manager;
    }

    @Bean
    public WebClient webClient(OAuth2AuthorizedClientManager manager) {
        return WebClient.builder()
                .filter((request, next) -> {
                    OAuth2AuthorizeRequest authRequest = OAuth2AuthorizeRequest
                            .withClientRegistrationId("payment-service-client")
                            .principal("paymentService")
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
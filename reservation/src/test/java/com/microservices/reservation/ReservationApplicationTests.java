package com.microservices.reservation;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class ReservationApplicationTests {

    @MockitoBean
    ClientRegistrationRepository ClientRegistrationRepository;
    @MockitoBean
    OAuth2AuthorizedClientRepository OAuth2AuthorizedClientRepository;

    @MockitoBean
    OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    @Test
    void contextLoads() {
    }

}

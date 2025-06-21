package com.microservices.payment;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class PaymentApplicationTests {

	@MockitoBean
	ClientRegistrationRepository ClientRegistrationRepository;
	@MockitoBean
	OAuth2AuthorizedClientRepository OAuth2AuthorizedClientRepository;

	@Test
	void contextLoads() {
	}

}

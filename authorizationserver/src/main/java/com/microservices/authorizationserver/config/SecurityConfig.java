package com.microservices.authorizationserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.authorizationserver.repository.*;
import com.microservices.authorizationserver.service.JpaOAuth2AuthorizationConsentService;
import com.microservices.authorizationserver.service.JpaOAuth2AuthorizationService;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.CompositeLogoutHandler;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static com.microservices.authorizationserver.config.CustomClientMetadataConfig.configureCustomClientMetadataConverters;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String CUSTOM_CONSENT_PAGE_URI = "/oauth2/consent";

    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final RSAKeyPairConfigurations rsaKeyPairConfigurations;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http, RegisteredClientRepository registeredClientRepository)
            throws Exception {

        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                OAuth2AuthorizationServerConfigurer.authorizationServer();

        http.securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .with(authorizationServerConfigurer, (configurer) ->
                        configurer.authorizationEndpoint(authorizationEndpoint ->
                                        authorizationEndpoint.consentPage(CUSTOM_CONSENT_PAGE_URI))
                                .deviceAuthorizationEndpoint(deviceAuthorizationEndpoint ->
                                        deviceAuthorizationEndpoint.verificationUri("/activate"))
                                .deviceVerificationEndpoint(deviceVerificationEndpoint ->
                                        deviceVerificationEndpoint.consentPage(CUSTOM_CONSENT_PAGE_URI))
                                .tokenGenerator(tokenGenerator())
                                .clientAuthentication(clientAuthenticationConfigurer -> {
                                    clientAuthenticationConfigurer.authenticationConverter(new PublicClientRefreshTokenAuthenticationConverter());
                                    clientAuthenticationConfigurer.authenticationProvider(new PublicClientRefreshTokenProvider(registeredClientRepository));
                                })
                                .oidc(oidc ->
                                        oidc.clientRegistrationEndpoint(clientRegistrationEndpoint ->
                                                        clientRegistrationEndpoint.authenticationProviders(configureCustomClientMetadataConverters()))
                                                .userInfoEndpoint(Customizer.withDefaults()))
                )
                .exceptionHandling((exceptions) -> exceptions
                        .accessDeniedHandler(customAccessDeniedHandler)
                ).oauth2ResourceServer(oauth2ResourceServer ->
                        oauth2ResourceServer.jwt(Customizer.withDefaults()))
                .authorizeHttpRequests((authorize) ->
                        authorize.anyRequest().authenticated()
                ).exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                );

        return http.build();
    }

    //TODO : figure out how to handle roles and authorities
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
            throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .formLogin(formLoginConfigurer -> formLoginConfigurer.loginPage("/login"))
                .exceptionHandling(configurer ->
                        configurer
                                .accessDeniedHandler(customAccessDeniedHandler))
                .logout(logoutConfigurer -> logoutConfigurer.logoutUrl("/logout").addLogoutHandler(new CompositeLogoutHandler(
                        new CookieClearingLogoutHandler("JSESSIONID"),
                        new SecurityContextLogoutHandler()
                )).logoutSuccessHandler(customLogoutSuccessHandler))
                .authorizeHttpRequests(matcherRegistry -> matcherRegistry
                        .requestMatchers("/assets/**", "login","/actuator/health").permitAll()
                        .anyRequest().authenticated());
        return http.build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(ClientRepository clientRepository, @Qualifier("customObjectMapper") ObjectMapper objectMapper) {
        //TODO: this need to happen dynamically
        // This is just an example of how a client should be like
//        RegisteredClient messagingClient = RegisteredClient.withId(UUID.randomUUID().toString())
//                .clientId("chatBot-service")
//                .clientSecret(passwordEncoder().encode("123Aze456789@"))
//                .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
//                .authorizationGrantTypes(authorizationGrantTypes -> {
//                    authorizationGrantTypes.add(AuthorizationGrantType.CLIENT_CREDENTIALS);
//                    authorizationGrantTypes.add(AuthorizationGrantType.REFRESH_TOKEN);
//                    authorizationGrantTypes.add(AuthorizationGrantType.AUTHORIZATION_CODE);
//                })
//                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/chatbot-service")
//                .redirectUri("http://127.0.0.1:8080/authorized")
//                .postLogoutRedirectUri("http://127.0.0.1:8080/logged-out")
//                .scope(OidcScopes.PROFILE)
//                .scope(OidcScopes.OPENID)
//                .scope("user.read")
//                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
//                .tokenSettings(TokenSettings.builder().reuseRefreshTokens(false)
//                        .deviceCodeTimeToLive(Duration.ofDays(90)).build())
//                .build();

        //        registeredClientRepository.save(messagingClient);
        return new JpaRegisteredClientRepository(clientRepository, objectMapper);
    }

    @Bean
    public OAuth2AuthorizationService jpaOAuth2AuthorizationService(AuthorizationRepository authorizationRepository, RegisteredClientRepository registeredClientRepository, @Qualifier("customObjectMapper") ObjectMapper objectMapper) {
        return new JpaOAuth2AuthorizationService(authorizationRepository, registeredClientRepository, objectMapper);
    }

    @Bean
    public OAuth2AuthorizationConsentService OAuth2AuthorizationConsentService(AuthorizationConsentRepository authorizationConsentRepository, RegisteredClientRepository registeredClientRepository) {
        return new JpaOAuth2AuthorizationConsentService(authorizationConsentRepository, registeredClientRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // TODO: For development and testing purposes only. In a production environment, the key pair should be managed securely and provided independently.
    // TODO: A key pair rotation functionality should be implemented.
    // The private key is used to sign access tokens. Clients and resource servers can retrieve the corresponding public key from the /oauth2/jwks endpoint.
    // DO NOT SHARE OR LEAK THE PRIVATE KEY!!!!
    // During token validation, they should compare the key ID (kid) in the token's header with the key ID of the public key from /oauth2/jwks.
    // If the key IDs match, the client or resource server should then verify that the token was indeed signed by the associated private key.
    // If you don't want to fetch the public key from the auth server you can pass the public key to each client/ressource server you want.
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = rsaKeyPairConfigurations.getTokenSigningKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(rsaKeyPairConfigurations.getTokenSigningKeyPairId())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.addExposedHeader("Content-Disposition");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> userRepository.findByEmailAddress(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserRepository userRepository) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService(userRepository));
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public EmailValidator emailValidator() {
        return EmailValidator.getInstance();
    }

    OAuth2TokenCustomizer<JwtEncodingContext> customizer() {
        return context -> {
            if (context.getTokenType().getValue().equals(OidcParameterNames.ID_TOKEN) ||
                    context.getTokenType().getValue().equals(OAuth2TokenType.ACCESS_TOKEN.getValue())) {
                Set<String> authorities = context.getPrincipal().getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet());
                context.getClaims().claim("authorities", authorities);
            }
        };
    }

    OAuth2TokenGenerator<?> tokenGenerator() {
        JwtGenerator jwtGenerator = new JwtGenerator(new NimbusJwtEncoder(jwkSource()));
        jwtGenerator.setJwtCustomizer(customizer());
        return new DelegatingOAuth2TokenGenerator(jwtGenerator, new CustomOAuth2RefreshTokenGenerator());
    }

    public static final class CustomOAuth2RefreshTokenGenerator implements OAuth2TokenGenerator<OAuth2RefreshToken> {
        private final StringKeyGenerator refreshTokenGenerator = new Base64StringKeyGenerator(Base64.getUrlEncoder().withoutPadding(), 96);

        public CustomOAuth2RefreshTokenGenerator() {
        }

        @Nullable
        public OAuth2RefreshToken generate(OAuth2TokenContext context) {
            if (!OAuth2TokenType.REFRESH_TOKEN.equals(context.getTokenType())) {
                return null;
            } else {
                Instant issuedAt = Instant.now();
                Instant expiresAt = issuedAt.plus(context.getRegisteredClient().getTokenSettings().getRefreshTokenTimeToLive());
                return new OAuth2RefreshToken(this.refreshTokenGenerator.generateKey(), issuedAt, expiresAt);
            }
        }
    }

    private static final class PublicClientRefreshTokenAuthentication extends OAuth2ClientAuthenticationToken {

        public PublicClientRefreshTokenAuthentication(String clientId) {
            super(clientId, ClientAuthenticationMethod.NONE, null, null);
        }

        public PublicClientRefreshTokenAuthentication(RegisteredClient registeredClient) {
            super(registeredClient, ClientAuthenticationMethod.NONE, null);
        }
    }

    private static final class PublicClientRefreshTokenAuthenticationConverter implements AuthenticationConverter {

        @Override
        public Authentication convert(HttpServletRequest request) {
            String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
            if (!grantType.equals(AuthorizationGrantType.REFRESH_TOKEN.getValue())) {
                return null;
            }
            String clientId = request.getParameter(OAuth2ParameterNames.CLIENT_ID);
            if (!StringUtils.hasText(clientId)) {
                return null;
            }
            return new PublicClientRefreshTokenAuthentication(clientId);
        }
    }

    private static final class PublicClientRefreshTokenProvider implements AuthenticationProvider {

        private final RegisteredClientRepository registeredClientRepository;

        public PublicClientRefreshTokenProvider(RegisteredClientRepository registeredClientRepository) {
            this.registeredClientRepository = registeredClientRepository;
        }

        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            PublicClientRefreshTokenAuthentication publicClientRefreshTokenAuthentication = (PublicClientRefreshTokenAuthentication) authentication;
            if (!ClientAuthenticationMethod.NONE.equals(publicClientRefreshTokenAuthentication.getClientAuthenticationMethod())) {
                return null;
            }
            String clientId = publicClientRefreshTokenAuthentication.getPrincipal().toString();
            RegisteredClient registeredClient = registeredClientRepository.findByClientId(clientId);
            if (registeredClient == null) {
                throw new OAuth2AuthenticationException(new OAuth2Error(
                        OAuth2ErrorCodes.INVALID_CLIENT,
                        "Invalid Client",
                        null
                ));
            }
            if (!registeredClient.getClientAuthenticationMethods().contains(
                    publicClientRefreshTokenAuthentication.getClientAuthenticationMethod()
            )) {
                throw new OAuth2AuthenticationException(new OAuth2Error(
                        OAuth2ErrorCodes.INVALID_CLIENT,
                        "Authentification_method not supported by client",
                        null
                ));
            }
            return new PublicClientRefreshTokenAuthentication(registeredClient);
        }

        @Override
        public boolean supports(Class<?> authentication) {
            return PublicClientRefreshTokenAuthentication.class.isAssignableFrom(authentication);
        }
    }

}
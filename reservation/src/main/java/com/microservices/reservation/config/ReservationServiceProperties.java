package com.microservices.reservation.config;

import com.microservices.common.ServiceProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Validated
@Configuration
@EnableConfigurationProperties(ReservationServiceProperties.class)
@ConfigurationProperties(prefix = ServiceProperties.SERVICE_PROPERTIES_PREFIX)
@Setter
@Getter
public class ReservationServiceProperties extends ServiceProperties {
    @NotNull(message = "payment-service-url must not be null")
    private String paymentServiceUrl;
    @NotNull(message = "event-service-url must not be null")
    private String eventServiceUrl;

    public ReservationServiceProperties() {
        super(null);
        this.paymentServiceUrl = null;
    }

    @Override
    public void setRootUrl(String rootUrl) {
        super.setRootUrl(rootUrl);
    }
}

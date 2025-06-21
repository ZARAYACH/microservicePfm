package com.microservices.payment.config;

import com.microservices.common.ServiceProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Validated
@Configuration
@EnableConfigurationProperties({PaymentServiceProperties.class})
@ConfigurationProperties(prefix = ServiceProperties.SERVICE_PROPERTIES_PREFIX)
@Setter
@Getter
public class PaymentServiceProperties extends ServiceProperties {
    @NotNull(message = "reservation-Service-Url must not be null")
    private String reservationServiceUrl;
    @NotNull(message = "event-Service-Url must not be null")
    private String eventServiceUrl;

    public PaymentServiceProperties() {
        super(null);
    }

    @Override
    public void setRootUrl(String rootUrl) {

        super.setRootUrl(rootUrl);
    }
}

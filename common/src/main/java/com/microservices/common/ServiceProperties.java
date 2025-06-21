package com.microservices.common;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class ServiceProperties {

    public final static String SERVICE_PROPERTIES_PREFIX = "services";

    public ServiceProperties(String rootUrl) {
        this.rootUrl = rootUrl;
    }
    @NotNull(message = "root-url must not be null")
    private String rootUrl;
}

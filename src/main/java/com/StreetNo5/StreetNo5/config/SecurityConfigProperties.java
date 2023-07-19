package com.StreetNo5.StreetNo5.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class SecurityConfigProperties {

    @Value("${spring.security.matchers.permit.all}")
    public String[] PERMIT_ALL_REQUEST_MATCHERS;

/*    @Value("${spring.security.matchers.admin}")
    public String[] ADMIN_REQUEST_MATCHERS;*/

    @Value("${spring.security.origin}")
    public String ORIGIN;

    @Value("${spring.security.origin2}")
    public String ORIGIN2;
}

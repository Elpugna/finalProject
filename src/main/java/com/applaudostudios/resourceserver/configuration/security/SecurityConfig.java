package com.applaudostudios.resourceserver.configuration.security;
import java.util.Collections;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

@Slf4j
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  private static final String CHECKOUT_URL = "/api/v1/checkout/**";
  private static final String ORDER_URL = "/api/v1/order/**";
  private static final String CUSTOMER_READ = "SCOPE_Customer:read";
  private static final String CUSTOMER_WRITE = "SCOPE_Customer:write";
  private static final String ADMIN_ROLE = "Admin";
  private static final String CUSTOMER_ROLE = "Customer";

  @Bean
  public JwtDecoder jwtDecoder(final OAuth2ResourceServerProperties properties) {
    String issuerUri = properties.getJwt().getIssuerUri();
    NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder)JwtDecoders.fromIssuerLocation(issuerUri);
    OAuth2TokenValidator<Jwt> withValidatedEmail = new DelegatingOAuth2TokenValidator(new OAuth2TokenValidator[]{new SecurityConfig.VerifiedEmailValidator()});
    jwtDecoder.setJwtValidator(withValidatedEmail);
    MappedJwtClaimSetConverter converter = MappedJwtClaimSetConverter.withDefaults(Collections.singletonMap("verified_email", (custom) -> {
      return "value";
    }));
    jwtDecoder.setClaimSetConverter(converter);
    return jwtDecoder;
  }

  protected void configure(final HttpSecurity http) throws Exception {
    http.authorizeRequests()
          .mvcMatchers(HttpMethod.DELETE, new String[]{"/api/v1/checkout/**"}).hasAnyAuthority(new String[]{"SCOPE_Customer:read", "SCOPE_Customer:write"})
          .mvcMatchers(HttpMethod.GET, new String[]{"/actuator", "/actuator/health"}).permitAll().mvcMatchers(HttpMethod.GET, new String[]{"/actuator/**"})
          .hasRole("Admin").mvcMatchers(HttpMethod.GET, new String[]{"/api/v1/checkout/**"}).hasAnyAuthority(new String[]{"SCOPE_Customer:read", "SCOPE_Customer:write"})
          .mvcMatchers(HttpMethod.POST, new String[]{"/api/v1/checkout/**"}).hasAnyAuthority(new String[]{"SCOPE_Customer:read", "SCOPE_Customer:write"})
          .mvcMatchers(HttpMethod.PUT, new String[]{"/api/v1/checkout/**"}).hasAnyAuthority(new String[]{"SCOPE_Customer:read", "SCOPE_Customer:write"})
          .mvcMatchers(HttpMethod.GET, new String[]{"/api/v1/order/**"}).hasAnyAuthority(new String[]{"SCOPE_Customer:read", "SCOPE_Customer:write"})
          .mvcMatchers(HttpMethod.POST, new String[]{"/api/v1/order/**"}).hasAnyAuthority(new String[]{"SCOPE_Customer:read", "SCOPE_Customer:write"})
          .and()
            .cors()
          .and()
            .oauth2ResourceServer()
              .jwt().jwtAuthenticationConverter(jwtAuthenticationConverter());
  }

  private static JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());
    return jwtAuthenticationConverter;
  }

  static class VerifiedEmailValidator implements OAuth2TokenValidator<Jwt> {
    private final OAuth2Error error = new OAuth2Error("403", "The user must validate the email", (String)null);

    VerifiedEmailValidator() {
    }

    public OAuth2TokenValidatorResult validate(final Jwt jwt) {
      if (jwt.hasClaim("email_verified") && jwt.getClaim("email_verified").equals(true)) {
        if (SecurityConfig.log.isDebugEnabled()) {
          SecurityConfig.log.debug("The email has been validated by the auth server");
        }

        return OAuth2TokenValidatorResult.success();
      } else {
        if (SecurityConfig.log.isDebugEnabled()) {
          SecurityConfig.log.debug("The email has been validated by the auth server");
        }

        return OAuth2TokenValidatorResult.failure(new OAuth2Error[]{this.error});
      }
    }
  }
}

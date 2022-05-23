package com.seatapp.configs;

import com.seatapp.filters.AdminFilter;
import com.seatapp.filters.JwtAuthorizationFilter;
import com.seatapp.usermanagement.services.JwtService;
import com.seatapp.usermanagement.services.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;
import java.util.List;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * The service to handle the login on the Azure AD.
     */
    private final OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService;
    /**
     * The service to handle the login with the JWT token.
     */
    private final JwtUserDetailsService userDetailsService;
    /**
     * The service to authenticate the JWT token.
     */
    private final JwtService jwtServiceImpl;

    @Autowired
    WebSecurityConfig(final JwtUserDetailsService userDetailsService,
                      final OAuth2UserService<OidcUserRequest,
                              OidcUser> oidcUserService,
                      final JwtService jwtServiceImpl) {
        this.userDetailsService = userDetailsService;
        this.oidcUserService = oidcUserService;
        this.jwtServiceImpl = jwtServiceImpl;
    }

    @Override
    public void configure(
            final AuthenticationManagerBuilder authenticationManagerBuilder)
            throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the CORS of the application.
     *
     * @return a bean that configures the CORS
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> configureCors() {
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:19006",
                "http://localhost:3000",
                "http://xplore-seatapp.s3-website-eu-west-1.amazonaws.com"));
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowedHeaders(Collections.singletonList("*"));
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean =
                new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .addFilter(new JwtAuthorizationFilter(
                        authenticationManager(),
                        jwtServiceImpl,
                        userDetailsService,
                        "/api/login/",
                        "/actuator/",
                        "/api/admin/login"))
                .addFilter(new AdminFilter(authenticationManager(),
                        jwtServiceImpl,
                        "POST /api/seats",
                        "DELETE /api/seats",
                        "GET /api/admin/healthcheck"))
                .authorizeRequests()
                .antMatchers("/api/login/**",
                        "/api/admin/login/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .oidcUserService(oidcUserService);
    }

}

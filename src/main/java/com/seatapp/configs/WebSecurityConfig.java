package com.seatapp.configs;

import com.seatapp.filters.JwtAuthorizationFilter;
import com.seatapp.services.usermanagement.JwtUtils;
import com.seatapp.services.usermanagement.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
    private final JwtUtils jwtUtils;

    @Autowired
    WebSecurityConfig(final JwtUserDetailsService userDetailsService,
                      final OAuth2UserService<OidcUserRequest,
                              OidcUser> oidcUserService,
                      final JwtUtils jwtUtils) {
        this.userDetailsService = userDetailsService;
        this.oidcUserService = oidcUserService;
        this.jwtUtils = jwtUtils;
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

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .addFilter(new JwtAuthorizationFilter(authenticationManager(),
                        jwtUtils,
                        userDetailsService, "/api/login/"))
                .authorizeRequests()
                .antMatchers("/api/login/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .oidcUserService(oidcUserService);
    }

}

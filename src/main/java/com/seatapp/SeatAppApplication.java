package com.seatapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;
import java.util.List;

/**
 * Main SeatAppApplication class.
 */
@SpringBootApplication
public class SeatAppApplication {
    /**
     * Main method.
     *
     * @param args the arguments given with the application
     */
    public static void main(final String[] args) {
        SpringApplication.run(SeatAppApplication.class, args);
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
                "https://5b27-94-143-189-241.eu.ngrok.io"));
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowedHeaders(Collections.singletonList("*"));
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean =
                new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}

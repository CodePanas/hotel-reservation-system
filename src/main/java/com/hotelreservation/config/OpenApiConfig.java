package com.hotelreservation.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI hotelReservationOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hotel Reservation System API")
                        .description("API para el sistema de reservaciones de hotel")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Hotel Reservation Team")
                                .email("contact@hotelreservation.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
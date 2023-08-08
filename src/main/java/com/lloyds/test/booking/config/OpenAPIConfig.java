package com.lloyds.test.booking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI myOpenAPI() {


        Contact contact = new Contact();
        contact.setEmail("saiShanmugavel@yahoo.com");
        contact.setName("Shanmugavelmurugan");


        License license = new License().name("Lloyds License").url("https://localhost:8081/v1/booking");

        Info info = new Info()
                .title("Employee Meeting Booking Request API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to post meeting request as text.")
                .license(license);

        return new OpenAPI().info(info);
    }
}

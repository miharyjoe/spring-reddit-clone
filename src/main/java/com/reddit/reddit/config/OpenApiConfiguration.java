package com.reddit.reddit.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;



@OpenAPIDefinition(
  info = @Info(
          contact = @Contact(
            name =  "Mihary Joel",
            email = "miharyjoel@gmail.com",
            url = "https://miharyjoelportfolio.vercel.app/"
          ),
    description = "reddit clone using spring boot",
    title = "reddit Clone",
    version = "1.0",
    license = @License(
      name = "License name"
    )
  ),
  servers = {
    @Server(
      description = "localhost",
      url = "http://localhost:8080"
    )
  },
  security = {
    @SecurityRequirement(name = "bearer auth")
  }
)
@SecurityScheme(
  name = "bearer auth",
  description = "JWT auth desc",
  scheme = "bearer",
  type = SecuritySchemeType.HTTP,
  bearerFormat = "JWT",
  in = SecuritySchemeIn.HEADER
)
public class OpenApiConfiguration {
}
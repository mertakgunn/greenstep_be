package com.bbs.greenstep.Config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact=@Contact(
                        name = "GreenStep",
                        email = "greenstep",
                        url = "https://mertakgun.tr/api"
                ),
                description ="OpenAPI for GreenStep Sitesi", //Swagger UI'da başlık altında görünecek açıklama
                title = "GreenStep Sitesi API",//Swagger UI'da görünecek başlık
                version = "3.1.0",
                license = @License(
                        name = "Licence name",
                        url = "https://www.licencesome.com"
                ),
                termsOfService = "https://www.neworganization.com"
        ),

        servers = {
                @Server(
                        description = "Local Development Server",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "Prod Development Server",
                        url = "https://mertakgun.tr/api"
                )
        }
)


public class SetupOpenApiConfig {

}
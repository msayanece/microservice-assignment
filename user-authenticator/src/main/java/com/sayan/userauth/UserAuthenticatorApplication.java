package com.sayan.userauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@SpringBootApplication
@EnableSwagger2
public class UserAuthenticatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserAuthenticatorApplication.class, args);
	}

	@Bean
	public Docket swaggerConfiguration(){
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.sayan"))
				.build()
				.apiInfo(apiInfo());
	}

	private ApiInfo apiInfo(){
		return new ApiInfo(
				"Authentication Service",
				"This service is used to authenticate an user and also manupulate user details. " +
						"Some Operations: Registration, Login, Update user",
				"1.0",
				"Free to use",
				new Contact("SAYAN MUKHERJEE", "", "msayanece@gmail.com"),
				"Apache Open Source Licence",
				null,
				Collections.emptyList()
		);
	}
}

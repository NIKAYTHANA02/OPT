package com.abfl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
//		servers = {@Server(url = "http://127.0.0.1:4541/OTP-SERVICE",description = "OTP Service"),
//		@Server(url = "http://10.100.1.88:8100/OTP-Service",description = "API Gateway")
//},
		info = @Info(title = "OTP-SERVICE", version = "1.0", description = "Od Service", license = @License(name = "Apache 2.0", url = " "), contact = @Contact(url = " ", name = " ", email = "kgisl.com")

		))
@SpringBootApplication
//@EnableEurekaClient
public class OtpServicesApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(OtpServicesApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(OtpServicesApplication.class);
	}

}

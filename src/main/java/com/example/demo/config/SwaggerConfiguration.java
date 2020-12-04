package com.example.demo.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.data.dto.ErrorResponse;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicates;

import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@RequiredArgsConstructor
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

	private final TypeResolver typeResolver;

	@Getter
	private final String[] urlList = { "/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**",
			"/swagger/**" };

	@Bean
	public Docket commonApi() {

		return new Docket(DocumentationType.SWAGGER_2)//
				.securitySchemes(Collections.singletonList(new ApiKey("BearerToken", HttpHeaders.AUTHORIZATION, In.HEADER.name())))//
				.securityContexts(
						Arrays.asList(securityPostContext(), securityGetMethodContext(), securityEtcContext()))//
				.useDefaultResponseMessages(false)//
				.globalResponseMessage(RequestMethod.POST, getResponseMessages())
				.globalResponseMessage(RequestMethod.GET, getResponseMessages())
				.globalResponseMessage(RequestMethod.PUT, getResponseMessages())
				.globalResponseMessage(RequestMethod.DELETE, getResponseMessages())
				.additionalModels(typeResolver.resolve(ErrorResponse.class))//
				.select()//
				.apis(RequestHandlerSelectors.any()).paths(PathSelectors.any()).build()//
				.apiInfo(this.metaData());

	}

	private ApiInfo metaData() {
		return new ApiInfoBuilder()//
				.title("Demo")//
				.description("API EXAMPLE")//
				.version("0.0.1")//
				.contact(new Contact("gensing", "https://github.com/gensing/", "gensing@github.com"))//
				.build();
	}

	private List<ResponseMessage> getResponseMessages() {
		List<ResponseMessage> responseMessages = new ArrayList<>();
		responseMessages.add(buildResponseMessage(HttpStatus.BAD_REQUEST, "Bad Request"));
		responseMessages.add(buildResponseMessage(HttpStatus.NOT_FOUND, "Not Found"));
		responseMessages.add(buildResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"));
		return responseMessages;
	}

	private ResponseMessage buildResponseMessage(HttpStatus httpStatus, String message) {
		return new ResponseMessageBuilder().code(httpStatus.value()).message(message)
				.responseModel(new ModelRef("ErrorResponse")).build();
	}

	private SecurityContext securityPostContext() {
		return SecurityContext.builder().securityReferences(defaultAuth())
				.forHttpMethods(Predicates.in(Arrays.asList(HttpMethod.POST)))
				.forPaths(PathSelectors.regex("(?!/member.*).*"))//
				.forPaths(PathSelectors.regex("(?!/login.*).*"))//
				.build();
	}

	private SecurityContext securityGetMethodContext() {
		return SecurityContext.builder().securityReferences(defaultAuth())
				.forHttpMethods(Predicates.in(Arrays.asList(HttpMethod.GET)))//
				.forPaths(PathSelectors.ant("/member/**"))//
				.build();
	}

	private SecurityContext securityEtcContext() {
		return SecurityContext.builder().securityReferences(defaultAuth())
				.forHttpMethods(Predicates.in(Arrays.asList(HttpMethod.PUT, HttpMethod.DELETE)))
				.forPaths(PathSelectors.any()).build();
	}

	private List<SecurityReference> defaultAuth() {
		AuthorizationScope[] authorizationScopes = { new AuthorizationScope("global", "accessEverything") };
		return Arrays.asList(new SecurityReference("BearerToken", authorizationScopes));
	}
}
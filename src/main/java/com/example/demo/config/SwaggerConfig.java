package com.example.demo.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	/// swagger-ui.html
	@Bean
	public Docket commonApi() {
		return new Docket(DocumentationType.SWAGGER_2)//
				.useDefaultResponseMessages(false)//
				.select()//
				.apis(RequestHandlerSelectors.basePackage("com.example.demo.controller"))//
				.paths(PathSelectors.ant("/**"))//
				.build()//
				.groupName("example")//
				.apiInfo(this.metaData())//
				.securityContexts(Arrays.asList(this.securityContext()))//
				.securitySchemes(Arrays.asList(this.apiKey()));
	}

	private ApiInfo metaData() {
		return new ApiInfoBuilder()//
				.title("Demo")//
				.description("API EXAMPLE")//
				.version("0.0.1")//
				.termsOfServiceUrl("Terms of service")//
				.contact(new Contact("name, Da hun", "git@", "email@"))//
				.license("Apache License Version 2.0")//
				.licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")//
				.build();
	}

	private ApiKey apiKey() {
		return new ApiKey("JWT", "Authorization", "header");
	}

	private SecurityContext securityContext() {
		return springfox.documentation.spi.service.contexts.SecurityContext.builder().securityReferences(defaultAuth())
				.forPaths(PathSelectors.ant("/**")).build();
	}

	List<SecurityReference> defaultAuth() {
		AuthorizationScope[] authorizationScopes = { new AuthorizationScope("global", "accessEverything") };
		return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
	}
//https://m.blog.naver.com/ndskr/222040308809
//https://blog.naver.com/PostView.nhn?blogId=cutesboy3&logNo=221510400386&parentCategoryNo=97&categoryNo=&viewDate=&isShowPopularPosts=true&from=search
}
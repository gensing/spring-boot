package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.demo.data.domain.RoleCode;
import com.example.demo.filter.AuthorizationFilter;
import com.example.demo.service.SecurityService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final SwaggerConfiguration swaggerConfig;

	private final SecurityService securityServiceImpl;
	private final PasswordEncoder passwordEncoder;

	private static final String[] SIGNUP_SIGNIN_WHITELIST = { "/member", "/login", "/refresh" };
	private static final String[] AUTH_WHITELIST = { "/", "/article/**" };
	private static final String[] ADMIN_LIST = { "/admin" };

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http//
				.httpBasic().disable() // HTTP 기본 인증 비활성화
				.formLogin().disable() // 폼 기반 인증 비활성화
				.headers().frameOptions().disable().and()// rest 서버라 불필요
				.csrf().disable()// CSRF 프로텍션을 비활성화
				.cors().disable()// CORS 프로텍션을 비활성화
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()// 세션 정책 설정
				.authorizeRequests()//
				.antMatchers(HttpMethod.GET, AUTH_WHITELIST).permitAll()//
				.antMatchers(HttpMethod.POST, SIGNUP_SIGNIN_WHITELIST).permitAll()//
				.antMatchers(ADMIN_LIST).hasRole(RoleCode.ADMIN.name())//
				.anyRequest().authenticated().and()//
				.exceptionHandling()//
				// 권한이 필요한 리소스 접근시 -> 권한 정보가 없을 시 수행 ( accessToken == null )
				.accessDeniedHandler(securityServiceImpl)
				// 권한이 필요한 리소스 접근시 -> 권한 레벨이 부족할 시 수행 ( role_admin != role_user )
				.authenticationEntryPoint(securityServiceImpl)//
				.and()//
				.addFilterBefore(new AuthorizationFilter(securityServiceImpl),
						UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers(swaggerConfig.getUrlList());
	}

	@Override
	protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(securityServiceImpl).passwordEncoder(passwordEncoder);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedOrigin("*");
		configuration.addAllowedHeader("*");
		configuration.addAllowedMethod("*");
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
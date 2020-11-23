package com.example.demo.config;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.filter.AuthorizationFilter;
import com.example.demo.service.impl.TokenServiceImpl;
import com.example.demo.service.impl.UserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final UserDetailsServiceImpl authenticationService;
	private final PasswordEncoder passwordEncoder;
	private final TokenServiceImpl tokenProvider;

	private static final String[] AUTH_WHITELIST = { "/**"
			// other public endpoints of your API may be appended to this array
	};

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http//
				.httpBasic().disable() // HTTP 기본 인증 비활성화
				.formLogin().disable() // 폼 기반 인증 비활성화
				.headers().frameOptions().disable().and()// rest 서버라 불필요
				.csrf().disable()// CSRF 프로텍션을 비활성화
				.cors().disable()// CORS 프로텍션을 비활성화
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()// 세션 정책 설정
				.addFilterBefore(new AuthorizationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)// 인증
				.authorizeRequests().antMatchers(AUTH_WHITELIST).permitAll().anyRequest().authenticated().and()//
				.exceptionHandling().authenticationEntryPoint((request, response, e) -> {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
				});
	}

	@Override
	public void configure(WebSecurity web) {

		// 정적 리소스 url
		List<String> pahts = new ArrayList<String>();
		pahts.add("/v2/api-docs");
		pahts.add("/swagger-resources/**");
		pahts.add("/swagger-ui.html");
		pahts.add("/webjars/**");
		pahts.add("/swagger/**");

		pahts.stream().map(path -> web.ignoring().antMatchers(path));
	}

	@Override
	protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(authenticationService).passwordEncoder(passwordEncoder);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

}
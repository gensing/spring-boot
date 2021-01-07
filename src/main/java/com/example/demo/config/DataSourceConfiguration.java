package com.example.demo.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceConfiguration {

	@Bean
	@ConfigurationProperties("spring.datasource.hikari")
	public DataSource dataSource() {
		return new HikariDataSource();
	}

}
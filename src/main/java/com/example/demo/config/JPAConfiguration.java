package com.example.demo.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.demo.StoryApplication;
import com.zaxxer.hikari.HikariDataSource;

//@EnableTransactionManagement
//entityManagerFactoryRef = transactionManagerRef =

@EnableJpaRepositories(basePackageClasses = { StoryApplication.class })
@EntityScan(basePackageClasses = { StoryApplication.class })
@Configuration
public class JPAConfiguration<containerEntityManagerFactoryBean> {

	@Bean
	@ConfigurationProperties(prefix = "datasource.mysql")
	public DataSource dataSource() {
		return new HikariDataSource();
	}

	@Bean
	public JpaVendorAdapter hibernateJpaVendorAdapter() {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);
		vendorAdapter.setShowSql(true);
		vendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQL5InnoDBDialect");
		vendorAdapter.setDatabase(Database.MYSQL);
		return vendorAdapter;
	}

	@Bean
	public Properties hibernateProperties() {
		return new Properties();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(JpaVendorAdapter hibernateJpaVendorAdapter,
			Properties hibernateProperties, DataSource dataSource) {

		LocalContainerEntityManagerFactoryBean containerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();

		containerEntityManagerFactoryBean.setJpaVendorAdapter(hibernateJpaVendorAdapter);
		containerEntityManagerFactoryBean.setJpaProperties(hibernateProperties);
		containerEntityManagerFactoryBean.setDataSource(dataSource);
		// containerEntityManagerFactoryBean.setPersistenceUnitName("default");
		containerEntityManagerFactoryBean.setPackagesToScan("com.example.demo.*");

		return containerEntityManagerFactoryBean;
	}

	@Bean
	public PlatformTransactionManager jpaTransactionManager(EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

}
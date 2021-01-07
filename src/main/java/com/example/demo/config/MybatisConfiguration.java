package com.example.demo.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.Data;

// @MapperScan 미사용 : SqlSession을 이용한 daoImpl 직접 구현
// @MapperScan 사용 : @Mapper가 선언된 mapper interface를 찾아 { mapper.xml | @Select | @Insert | @Update | @Delete } 설정에 따라 자동 구현
@MapperScan("com.example.demo.mapper")
@Configuration
public class MybatisConfiguration<containerEntityManagerFactoryBean> {

	@Data
	public static class MybatisPropreties{
		private String mapperLocations;
		private String configLocation;
	}

	@Autowired
	ApplicationContext applicationContext;
	
	@Bean
	@ConfigurationProperties(prefix = "mybatis")
	public MybatisPropreties getMybatisPropreties() {
		return new MybatisPropreties();
	}

	@Bean
	public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource, MybatisPropreties mybatisPropreties) throws IOException {

		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource);
		factoryBean.setConfigLocation(applicationContext.getResource(mybatisPropreties.getConfigLocation()));
		factoryBean.setMapperLocations(applicationContext.getResources(mybatisPropreties.getMapperLocations()));

		return factoryBean;
	}

	@Bean
	public SqlSession sqlSession(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

	// mybatis는 직접 트랜잭션을 관리하지 않기 때문에, jpa 트랜잭션 매니저를 사용할 수 있다.
	//@Bean("mybatisTransactionManager") 
	public PlatformTransactionManager mybatisTransactionManager(DataSource dataSource) {
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
		transactionManager.setGlobalRollbackOnParticipationFailure(false);
		return transactionManager;
	}

}
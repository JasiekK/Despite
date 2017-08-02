package com.despite.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource(value = "classpath:/database.properties")
@EnableJpaRepositories("com.despite.repository")
@EntityScan("com.despite.entities")
public class DaoConfig {

    @Autowired
    Environment environment;

    @Bean(name = "dataSource") //destroyMethod = "close"
    public DataSource dataSource() {
        HikariConfig poolConfig = new HikariConfig();
        poolConfig.setJdbcUrl(environment.getProperty("database.url"));
        poolConfig.setUsername(environment.getProperty("database.user"));
        poolConfig.setPassword(environment.getProperty("database.password"));
        poolConfig.setDriverClassName(environment.getProperty("database.driver"));
        // poolConfig.setMaximumPoolSize(Integer.parseInt(environment.getProperty("database.max.pool.size")));
        // poolConfig.setMinimumIdle(1);
        return new HikariDataSource(poolConfig);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan(new String[]{"com.despite.entities"});
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(additionalProperties());
        return em;
    }

    @Bean
    JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    final Properties additionalProperties() {
        final Properties hibernateProperties = new Properties();

        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", environment.getProperty("hibernate.hbm2ddl.auto"));
        hibernateProperties.setProperty("hibernate.dialect", environment.getProperty("hibernate.dialect"));
        hibernateProperties.setProperty("hibernate.show_sql", environment.getProperty("hibernate.show_sql"));
        //hibernateProperties.setProperty("hibernate.cache.use_second_level_cache", environment.getProperty("hibernate.cache.use_second_level_cache"));
        //hibernateProperties.setProperty("hibernate.cache.use_query_cache", environment.getProperty("hibernate.cache.use_query_cache"));

        return hibernateProperties;
    }
}

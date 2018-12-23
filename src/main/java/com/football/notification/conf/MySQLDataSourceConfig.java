package com.football.notification.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Created by IntelliJ IDEA.
 * User: Truong Nguyen
 * Date: 06-Dec-18
 * Time: 2:09 PM
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@EnableTransactionManagement
public class MySQLDataSourceConfig {
    @Autowired
    private Environment environment;

    @Bean(name = "dataSource")
    public DataSource getDataSource() {
        DataSource dataSource = DataSourceBuilder
                .create()
                .username(environment.getRequiredProperty("spring.datasource.username"))
                .password(environment.getRequiredProperty("spring.datasource.password"))
                .url(environment.getRequiredProperty("spring.datasource.url"))
                .driverClassName(environment.getRequiredProperty("spring.datasource.driver-class-name"))
                .build();
        return dataSource;
    }
}

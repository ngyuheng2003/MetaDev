package com.metadev.connect.Controller;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.Serializable;

@Component
public class DataSourceConfig implements Serializable {

    private static String url;
    private static String username;
    private static String password;

    public DataSourceConfig(@Value("${spring.datasource.url}") String url, @Value("${spring.datasource.username}") String username, @Value("${spring.datasource.password}")String password) {
        this.url = url;
        this.username = username;
        this.password = password;

    }

    public DataSourceConfig(){
        this(url, username, password);
    }

    public DataSource getDataSource(){
        return new DriverManagerDataSource(url, username, password);
    }

    public String getUrl(){
        return url;
    }
}

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
    // Static variables to hold the database connection details
    private static String url;
    private static String username;
    private static String password;

    /* Constructor to initialize DataSourceConfig with database connection details
     Values are injected from the Spring application properties */
    public DataSourceConfig(@Value("${spring.datasource.url}") String url, @Value("${spring.datasource.username}") String username, @Value("${spring.datasource.password}")String password) {
        this.url = url;
        this.username = username;
        this.password = password;

    }

    // Default constructor that reuse the parameterized constructor
    public DataSourceConfig(){
        this(url, username, password);
    }

    // Creates and returns a DataSource object configured with the database connection details
    public DataSource getDataSource(){
        return new DriverManagerDataSource(url, username, password);
    }

    // Getter method for the database URL
    public String getUrl(){
        return url;
    }
}

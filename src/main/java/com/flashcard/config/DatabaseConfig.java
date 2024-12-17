package com.flashcard.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
public class DatabaseConfig {


    private final DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }
}
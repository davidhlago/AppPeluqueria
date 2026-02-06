package com.peluqueria.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Configuration
public class DatabaseConfig {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void setMaxAllowedPacket() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute("SET GLOBAL max_allowed_packet = 16777216");

            try {
                statement.execute("SET SESSION max_allowed_packet = 16777216");
            } catch (Exception sessionEx) {
            }

        } catch (Exception e) {
            System.err.println("Error al configurar almacenamiento: " + e.getMessage());
        }
    }
}
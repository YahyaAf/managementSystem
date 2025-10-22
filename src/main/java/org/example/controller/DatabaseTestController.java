package org.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class DatabaseTestController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/connection")
    public Map<String, Object> testConnection() {
        Map<String, Object> response = new HashMap<>();

        try (Connection connection = dataSource.getConnection()) {
            if (connection != null && !connection.isClosed()) {
                response.put("status", "success");
                response.put("message", "Database connection is working!");
                response.put("database", connection.getCatalog());
                response.put("url", connection.getMetaData().getURL());
            } else {
                response.put("status", "error");
                response.put("message", "Connection is closed");
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Connection failed: " + e.getMessage());
            response.put("error", e.getClass().getName());
        }

        return response;
    }
}
package com.pratik.jobtracker.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String URL = "jdbc:sqlite:jobtracker.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initialiseDatabase() {
        String sql = """
                CREATE TABLE IF NOT EXISTS applications (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    company TEXT NOT NULL,
                    role TEXT NOT NULL,
                    salary REAL,
                    location TEXT,
                    application_date TEXT,
                    status TEXT NOT NULL,
                    job_description TEXT
                )
                """;

        try (
                Connection connection = getConnection();
                Statement statement = connection.createStatement()
        ) {
            statement.execute(sql);
            System.out.println("Database initialised successfully.");

        } catch (SQLException e) {
            System.err.println("Failed to initialise database.");
            e.printStackTrace();
        }
    }
}
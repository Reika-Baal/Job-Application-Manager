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

        String applicationSql = """
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

        String interviewSql = """
                CREATE TABLE IF NOT EXISTS interviews (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    application_id INTEGER NOT NULL,
                    interview_date_time TEXT NOT NULL,
                    type TEXT,
                    location TEXT,
                    notes TEXT,
                    FOREIGN KEY (application_id)
                        REFERENCES applications(id)
                        ON DELETE CASCADE
                )
                """;

        try (
                Connection connection = getConnection();
                Statement statement = connection.createStatement()
        ) {
            statement.execute(applicationSql);
            statement.execute(interviewSql);

            System.out.println("Database initialised successfully.");

        } catch (SQLException e) {
            System.err.println("Failed to initialise database.");
            e.printStackTrace();
        }
    }
}
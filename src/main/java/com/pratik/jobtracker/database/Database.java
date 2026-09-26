package com.pratik.jobtracker.database;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String APP_FOLDER = "JobApplicationManager";

    private static final Path DATABASE_DIRECTORY =
            Paths.get(
                    System.getenv("APPDATA"),
                    APP_FOLDER
            );

    private static final Path DATABASE_PATH =
            DATABASE_DIRECTORY.resolve(
                    "jobtracker.db"
            );

    private static final String URL =
            "jdbc:sqlite:" + DATABASE_PATH;


    public static Connection getConnection() throws SQLException {

        try {
            Files.createDirectories(
                    DATABASE_DIRECTORY
            );
        } catch (Exception e) {
            throw new SQLException(
                    "Could not create application data directory.",
                    e
            );
        }

        Connection connection =
                DriverManager.getConnection(URL);

        try (
                Statement statement =
                        connection.createStatement()
        ) {
            statement.execute(
                    "PRAGMA foreign_keys = ON"
            );
        }

        return connection;
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
                        job_description TEXT,
                        notes TEXT,
                        deleted INTEGER NOT NULL DEFAULT 0
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

        String statusHistorySql = """
                CREATE TABLE IF NOT EXISTS application_status_history (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    application_id INTEGER NOT NULL,
                    status TEXT NOT NULL,
                    reached_at TEXT NOT NULL,
                    FOREIGN KEY (application_id)
                        REFERENCES applications(id)
                        ON DELETE CASCADE
                )
                """;

        try (
                Connection connection = getConnection();
                Statement statement =
                        connection.createStatement()
        ) {

            statement.execute(applicationSql);

            statement.execute(interviewSql);

            statement.execute(statusHistorySql);


            try {
                statement.execute(
                        "ALTER TABLE applications ADD COLUMN notes TEXT"
                );
            } catch (SQLException ignored) {
            }


            statement.execute(
                    """
                    DELETE FROM interviews
                    WHERE application_id NOT IN (
                        SELECT id FROM applications
                    )
                    """
            );

            try {
                statement.execute(
                        "ALTER TABLE applications ADD COLUMN deleted INTEGER NOT NULL DEFAULT 0"
                );
            } catch (SQLException ignored) {
            }

            System.out.println(
                    "Database initialised successfully."
            );

        } catch (SQLException e) {

            System.err.println(
                    "Failed to initialise database."
            );

            e.printStackTrace();
        }
    }
}
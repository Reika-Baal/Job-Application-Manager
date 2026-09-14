package com.pratik.jobtracker.database;

import com.pratik.jobtracker.model.ApplicationStatus;
import com.pratik.jobtracker.model.JobApplication;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JobApplicationSNL {

    public void insert(JobApplication application) {
        String sql = """
                INSERT INTO applications
                (company, role, salary, location, application_date, status, job_description)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )

        ) {
            statement.setString(1, application.getCompany());
            statement.setString(2, application.getRole());
            statement.setDouble(3, application.getSalary());
            statement.setString(4, application.getLocation());
            statement.setString(5, application.getApplicationDate().toString());
            statement.setString(6, application.getStatus().name());
            statement.setString(7, application.getJobDescription());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    application.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<JobApplication> findAll() {
        List<JobApplication> applications = new ArrayList<>();

        String sql = """
                SELECT *
                FROM applications
                ORDER BY application_date DESC
                """;

        try (
                Connection connection = Database.getConnection();
                Statement statement = connection.createStatement();
                ResultSet results = statement.executeQuery(sql)
        ) {
            while (results.next()) {
                JobApplication application = new JobApplication(
                        results.getString("company"),
                        results.getString("role"),
                        results.getDouble("salary"),
                        results.getString("location"),
                        LocalDate.parse(results.getString("application_date")),
                        ApplicationStatus.valueOf(results.getString("status")),
                        results.getString("job_description")
                );

                application.setId(results.getInt("id"));

                applications.add(application);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return applications;
    }
}

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
                (company, role, salary, location, application_date, status, job_description, notes)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
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
            statement.setString(8, application.getNotes());

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
                        results.getString("job_description"),
                        results.getString("notes")
                );

                application.setId(results.getInt("id"));

                applications.add(application);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return applications;
    }

    public boolean update(JobApplication application) {
        String sql = """
                UPDATE applications
                SET company = ?,
                    role = ?,
                    salary = ?,
                    location = ?,
                    application_date = ?,
                    status = ?,
                    job_description = ?,
                    notes = ?
                WHERE id = ?
                """;

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, application.getCompany());
            statement.setString(2, application.getRole());
            statement.setDouble(3, application.getSalary());
            statement.setString(4, application.getLocation());
            statement.setString(5, application.getApplicationDate().toString());
            statement.setString(6, application.getStatus().name());
            statement.setString(7, application.getJobDescription());
            statement.setString(8, application.getNotes());
            statement.setInt(9, application.getId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public JobApplication findById(int id) {
        String sql = """
                SELECT *
                FROM applications
                WHERE id = ?
                """;

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, id);

            ResultSet results = statement.executeQuery();

            if (results.next()) {
                JobApplication application = new JobApplication(
                        results.getString("company"),
                        results.getString("role"),
                        results.getDouble("salary"),
                        results.getString("location"),
                        LocalDate.parse(results.getString("application_date")),
                        ApplicationStatus.valueOf(results.getString("status")),
                        results.getString("job_description"),
                        results.getString("notes")
                );

                application.setId(results.getInt("id"));

                return application;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return  null;
    }
    public boolean delete(int id) {
        String sql = """
                DELETE FROM applications
                WHERE id = ?
                """;

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1,id);

            return statement.executeUpdate() >0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

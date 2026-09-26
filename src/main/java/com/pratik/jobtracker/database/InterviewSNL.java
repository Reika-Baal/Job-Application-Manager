package com.pratik.jobtracker.database;

import com.pratik.jobtracker.model.Interview;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InterviewSNL {

    public void insert(Interview interview) {
        String sql = """
                INSERT INTO interviews
                (application_id, interview_date_time, type, location, notes)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {
            statement.setInt(1, interview.getApplicationId());
            statement.setString(2, interview.getInterviewDateTime().toString());
            statement.setString(3, interview.getType());
            statement.setString(4, interview.getLocation());
            statement.setString(5, interview.getNotes());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    interview.setId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Interview> findByApplicationId(int applicationId) {
        List<Interview> interviews = new ArrayList<>();

        String sql = """
                SELECT *
                FROM interviews
                WHERE application_id = ?
                ORDER BY interview_date_time ASC
                """;

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, applicationId);

            ResultSet results = statement.executeQuery();

            while (results.next()) {
                Interview interview = new Interview(
                        results.getInt("application_id"),
                        LocalDateTime.parse(
                                results.getString("interview_date_time")
                        ),
                        results.getString("type"),
                        results.getString("location"),
                        results.getString("notes")
                );

                interview.setId(results.getInt("id"));

                interviews.add(interview);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return interviews;
    }

    public boolean update(Interview interview) {
        String sql = """
                UPDATE interviews
                SET interview_date_time = ?,
                    type = ?,
                    location = ?,
                    notes = ?
                WHERE id = ?
                """;

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(
                    1,
                    interview.getInterviewDateTime().toString()
            );
            statement.setString(2, interview.getType());
            statement.setString(3, interview.getLocation());
            statement.setString(4, interview.getNotes());
            statement.setInt(5, interview.getId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = """
                DELETE FROM interviews
                WHERE id = ?
                """;

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Interview> findAll() {
        List<Interview> interviews = new ArrayList<>();

        String sql = """
            SELECT *
            FROM interviews
            ORDER BY interview_date_time ASC
            """;

        try (
                Connection connection = Database.getConnection();
                Statement statement = connection.createStatement();
                ResultSet results = statement.executeQuery(sql)
        ) {
            while (results.next()) {
                Interview interview = new Interview(
                        results.getInt("application_id"),
                        LocalDateTime.parse(
                                results.getString("interview_date_time")
                        ),
                        results.getString("type"),
                        results.getString("location"),
                        results.getString("notes")
                );

                interview.setId(results.getInt("id"));

                interviews.add(interview);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return interviews;
    }

    public boolean deleteByApplicationId(int applicationId) {

        String sql = """
            DELETE FROM interviews
            WHERE application_id = ?
            """;

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, applicationId);

            statement.executeUpdate();

            return true;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }
}
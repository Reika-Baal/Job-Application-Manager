package com.pratik.jobtracker.database;

import com.pratik.jobtracker.model.ApplicationStatus;
import com.pratik.jobtracker.model.ApplicationStatusHistory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ApplicationStatusHistorySNL {

    public void insert(ApplicationStatusHistory history) {
        String sql = """
                INSERT INTO application_status_history
                (application_id, status, reached_at)
                VALUES (?, ?, ?)
                """;

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {
            statement.setInt(1, history.getApplicationId());
            statement.setString(2, history.getStatus().name());
            statement.setString(3, history.getReachedAt().toString());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    history.setId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ApplicationStatusHistory> findByApplicationId(int applicationId) {
        List<ApplicationStatusHistory> historyEntries = new ArrayList<>();

        String sql = """
                SELECT *
                FROM application_status_history
                WHERE application_id = ?
                ORDER BY reached_at ASC
                """;

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, applicationId);

            ResultSet results = statement.executeQuery();

            while (results.next()) {
                ApplicationStatusHistory history =
                        new ApplicationStatusHistory(
                                results.getInt("application_id"),
                                ApplicationStatus.valueOf(
                                        results.getString("status")
                                ),
                                LocalDateTime.parse(
                                        results.getString("reached_at")
                                )
                        );

                history.setId(results.getInt("id"));

                historyEntries.add(history);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return historyEntries;
    }

    public boolean hasReachedStatus(
            int applicationId,
            ApplicationStatus status
    ) {
        String sql = """
                SELECT 1
                FROM application_status_history
                WHERE application_id = ?
                  AND status = ?
                LIMIT 1
                """;

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, applicationId);
            statement.setString(2, status.name());

            ResultSet results = statement.executeQuery();

            return results.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int countApplicationsThatReachedStatus(
            ApplicationStatus status
    ) {
        String sql = """
                SELECT COUNT(DISTINCT application_id)
                FROM application_status_history
                WHERE status = ?
                """;

        try (
                Connection connection = Database.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, status.name());

            ResultSet results = statement.executeQuery();

            if (results.next()) {
                return results.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
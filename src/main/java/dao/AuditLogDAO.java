package dao;

import model.AuditLog;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class AuditLogDAO {

    /**
     * Creates a new audit log entry.
     */
    public void create(AuditLog log) throws SQLException {
        String sql = "INSERT INTO audit_logs (user_id, action, details) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, log.getUserId());
            stmt.setString(2, log.getAction());
            stmt.setString(3, log.getDetails());
            stmt.executeUpdate();
        }
    }

    /**
     * Retrieves all audit logs for a specific user.
     */
    public List<AuditLog> findByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM audit_logs WHERE user_id = ? ORDER BY created_at DESC";
        List<AuditLog> logs = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    logs.add(mapLog(rs));
                }
            }
        }
        return logs;
    }

    /**
     * Retrieves all audit logs.
     */
    public List<AuditLog> findAll() throws SQLException {
        String sql = "SELECT * FROM audit_logs ORDER BY created_at DESC";
        List<AuditLog> logs = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                logs.add(mapLog(rs));
            }
        }
        return logs;
    }

    /**
     * Retrieves audit logs with filters, pagination, and optional limit.
     */
    public List<AuditLog> getAuditLogs(int page, int pageSize, Integer userId, String action, String startDate,
                                       String endDate, Integer limit) throws SQLException {
        List<AuditLog> logs = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM audit_logs WHERE 1=1");
        List<Object> params = new ArrayList<>();

        // Apply filters
        if (userId != null) {
            sql.append(" AND user_id = ?");
            params.add(userId);
        }
        if (action != null && !action.trim().isEmpty()) {
            sql.append(" AND action LIKE ?");
            params.add("%" + action + "%");
        }
        if (startDate != null && !startDate.trim().isEmpty()) {
            sql.append(" AND created_at >= ?");
            params.add(Timestamp.valueOf(startDate + " 00:00:00"));
        }
        if (endDate != null && !endDate.trim().isEmpty()) {
            sql.append(" AND created_at <= ?");
            params.add(Timestamp.valueOf(endDate + " 23:59:59"));
        }

        sql.append(" ORDER BY created_at DESC");
        if (limit != null) {
            sql.append(" LIMIT ?");
            params.add(limit);
        } else {
            sql.append(" LIMIT ? OFFSET ?");
            params.add(pageSize);
            params.add((page - 1) * pageSize);
        }

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    logs.add(mapLog(rs));
                }
            }
        }
        return logs;
    }

    /**
     * Counts audit logs for pagination with filters.
     */
    public int getTotalAuditLogs(Integer userId, String action, String startDate, String endDate) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM audit_logs WHERE 1=1");
        List<Object> params = new ArrayList<>();

        // Apply filters
        if (userId != null) {
            sql.append(" AND user_id = ?");
            params.add(userId);
        }
        if (action != null && !action.trim().isEmpty()) {
            sql.append(" AND action LIKE ?");
            params.add("%" + action + "%");
        }
        if (startDate != null && !startDate.trim().isEmpty()) {
            sql.append(" AND created_at >= ?");
            params.add(Timestamp.valueOf(startDate + " 00:00:00"));
        }
        if (endDate != null && !endDate.trim().isEmpty()) {
            sql.append(" AND created_at <= ?");
            params.add(Timestamp.valueOf(endDate + " 23:59:59"));
        }

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    /**
     * Maps a ResultSet row to an AuditLog object.
     */
    private AuditLog mapLog(ResultSet rs) throws SQLException {
        AuditLog log = new AuditLog();
        log.setLogId(rs.getInt("log_id"));
        log.setUserId(rs.getInt("user_id"));
        log.setAction(rs.getString("action"));
        log.setDetails(rs.getString("details"));
        log.setCreatedAt(rs.getTimestamp("created_at"));
        return log;
    }
}
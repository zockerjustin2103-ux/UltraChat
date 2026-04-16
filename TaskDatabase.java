import java.sql.*;

public class TaskDatabase {
    private Connection connection;

    public TaskDatabase(String dbUrl) throws SQLException {
        connection = DriverManager.getConnection(dbUrl);
        createTableIfNotExists();
    }

    private void createTableIfNotExists() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS tasks (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, description TEXT, status TEXT)";
        Statement stmt = connection.createStatement();
        stmt.execute(sql);
    }

    public void addTask(String name, String description, String status) throws SQLException {
        String sql = "INSERT INTO tasks (name, description, status) VALUES (?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, name);
        pstmt.setString(2, description);
        pstmt.setString(3, status);
        pstmt.executeUpdate();
    }

    public Task getTask(int id) throws SQLException {
        String sql = "SELECT * FROM tasks WHERE id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return new Task(rs.getInt("id"), rs.getString("name"), rs.getString("description"), rs.getString("status"));
        }
        return null;
    }

    public void updateTask(int id, String name, String description, String status) throws SQLException {
        String sql = "UPDATE tasks SET name = ?, description = ?, status = ? WHERE id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, name);
        pstmt.setString(2, description);
        pstmt.setString(3, status);
        pstmt.setInt(4, id);
        pstmt.executeUpdate();
    }

    public void deleteTask(int id) throws SQLException {
        String sql = "DELETE FROM tasks WHERE id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, id);
        pstmt.executeUpdate();
    }

    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    // Inner class to represent a task
    public class Task {
        private int id;
        private String name;
        private String description;
        private String status;

        public Task(int id, String name, String description, String status) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.status = status;
        }

        // Getters and Setters
        public int getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public String getStatus() { return status; }
    }
}
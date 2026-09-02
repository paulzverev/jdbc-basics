import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcPractice {

    public static final String URL = "";
    public static final String USERNAME = "";
    public static final String PASSWORD = "";

    public static void main(String[] args) {
        printAllStudents();
    }

    public static void normalQuery() {
        String normalQuery = """
                SELECT * FROM students;
                """;

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(normalQuery)) {

            System.out.println("Connection established!");

            while (resultSet.next()) {
                long id = resultSet.getLong("student_id");
                String name = resultSet.getString("student_name");
                int age = resultSet.getInt("student_age");
                String email = resultSet.getString("student_email");

                Student student = new Student(
                        id,
                        name,
                        age,
                        email
                );

                System.out.println(student);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void preparedQuery() {
        String preparedQuery = """
                SELECT * FROM students
                WHERE student_id > ?;
                """;

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(preparedQuery)) {

            System.out.println("Connection established!");

            long searchedId = 2;

            preparedStatement.setLong(1, searchedId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    long id = resultSet.getLong("student_id");
                    String name = resultSet.getString("student_name");
                    int age = resultSet.getInt("student_age");
                    String email = resultSet.getString("student_email");

                    Student student = new Student(
                            id,
                            name,
                            age,
                            email
                    );

                    System.out.println(student);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printAllStudents() {
        String query = "SELECT * FROM students;";

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                long id = resultSet.getLong("student_id");
                String name = resultSet.getString("student_name");
                int age = resultSet.getInt("student_age");
                String email = resultSet.getString("student_email");

                Student student = new Student(id, name, age, email);
                System.out.println(student);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void fillTheTable() {
        String query = """
                INSERT INTO students (student_name, student_age, student_email)
                VALUES (?, ?, ?);
                """;

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, "Alice");
            preparedStatement.setInt(2, 22);
            preparedStatement.setString(3, "alice@example.com");
            preparedStatement.executeUpdate();

            preparedStatement.setString(1, "Bob");
            preparedStatement.setInt(2, 23);
            preparedStatement.setString(3, "bob@example.com");
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

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
        normalQuery();
        preparedQuery();
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
}

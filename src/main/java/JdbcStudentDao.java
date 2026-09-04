import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcStudentDao {

    public List<Student> findAllStudents() {
        List<Student> students = new ArrayList<>();

        String query = "SELECT * FROM students";

        try (Connection connection = DatabaseConnectionPool.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                students.add(
                        mapToStudent(
                                resultSet
                        )
                );
            }

            return students;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Student> findStudentById(long id) {
        String query = "SELECT * FROM students WHERE student_id = ?";

        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }

                return Optional.of(
                        mapToStudent(
                                resultSet
                        )
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Student mapToStudent(ResultSet resultSet) throws SQLException {
        long studentId = resultSet.getLong("student_id");
        String studentName = resultSet.getString("student_name");
        int studentAge = resultSet.getInt("student_age");
        String studentEmail = resultSet.getString("student_email");

        return new Student(
                studentId,
                studentName,
                studentAge,
                studentEmail
        );
    }

    public void saveStudent(Student student) {
        String query = """
            INSERT INTO students (student_name, student_age, student_email)
            VALUES (?, ?, ?)
            """;

        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, student.getName());
            preparedStatement.setInt(2, student.getAge());
            preparedStatement.setString(3, student.getEmail());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateStudent(Student student) {
        String query = """
            UPDATE students
            SET student_name = ?, student_age = ?, student_email = ?
            WHERE student_id = ?
            """;

        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, student.getName());
            preparedStatement.setInt(2, student.getAge());
            preparedStatement.setString(3, student.getEmail());
            preparedStatement.setLong(4, student.getId());

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteStudent(long id) {
        String query = "DELETE FROM students WHERE student_id = ?";

        try (Connection connection = DatabaseConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, id);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

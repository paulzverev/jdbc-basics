import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionPool {

    public static final String URL = "";
    public static final String USERNAME = "";
    public static final String PASSWORD = "";

    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(URL);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        config.setMaximumPoolSize(10);        // Максимум соединений в пуле
        config.setMinimumIdle(5);             // Минимум простаивающих соединений
        config.setConnectionTimeout(30000);   // Таймаут ожидания соединения (мс)
        config.setIdleTimeout(600000);        // Время жизни простаивающего соединения (мс)
        config.setMaxLifetime(1800000);       // Максимальное время жизни соединения (мс)

        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBMSConnection {

    public Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:ucanaccess://WeldLog.accdb");
        return conn;
    }
}

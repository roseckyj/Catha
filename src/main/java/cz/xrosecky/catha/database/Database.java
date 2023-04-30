package cz.xrosecky.catha.database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private final Connection connection;

    public Database(File path) throws SQLException {
        if (!path.getParentFile().exists()){
            try {
                path.getParentFile().createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        connection = DriverManager.getConnection("jdbc:sqlite:" + path.getAbsolutePath());
    }

    public Statement createStatement() {
        Statement s = null;
        try {
            s = connection.createStatement();
            s.setQueryTimeout(30);
            return s;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

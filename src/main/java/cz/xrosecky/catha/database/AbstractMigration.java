package cz.xrosecky.catha.database;

import java.sql.SQLException;

public abstract class AbstractMigration {
    protected final Runtime plugin;
    protected final Database database;
    public final int version;

    protected AbstractMigration(Runtime plugin, int version) {
        this.plugin = plugin;
        this.database = plugin.getDatabase();
        this.version = version;
    }

    public boolean apply(int version) {
        if (version >= this.version) {
            return true;
        }

        try {
            return migrate();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    protected abstract boolean migrate() throws Exception;

    protected int executeUpdate(String sql) throws SQLException {
        return database.createStatement().executeUpdate(sql);
    }
}

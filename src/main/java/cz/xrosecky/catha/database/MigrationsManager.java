package cz.xrosecky.catha.database;

import cz.xrosecky.catha.Catha;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MigrationsManager {
    private final Catha plugin;
    private final Database db;
    private final AbstractMigration[] migrations;

    public MigrationsManager(Catha plugin, AbstractMigration[] migrations) {
        this.plugin = plugin;
        this.db = plugin.getDatabase();
        this.migrations = migrations;
    }

    public boolean applyMigrations() {
        int version = 0;
        try {
            ResultSet result = db.createStatement().executeQuery("SELECT version FROM _migration");

            if (result.next()) {
                version = result.getInt(1);
            }
        } catch (Exception e) {
            // Version table does not exist. Let's create it
            try {
                db.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS _migration (version INTEGER PRIMARY KEY);");
                db.createStatement().executeUpdate("INSERT INTO _migration (version) VALUES (0);");
            } catch (SQLException ex) {
                plugin.getLogger().severe("Unable to create migrations info table!");
                ex.printStackTrace();
                return false;
            }
        }

        int initialVersion = version;

        List<AbstractMigration> sorted = Arrays.stream(migrations).sorted(Comparator.comparingInt(a -> a.version)).collect(Collectors.toList());

        for (AbstractMigration migration : sorted) {
            if (migration.version > version) {
                boolean result = migration.apply(version);
                if (!result) {
                    plugin.getLogger().severe("Migrations did not complete due to an error in step " + migration.version);
                    return false;
                }
                version = migration.version;

                plugin.getLogger().info("Successfully migrated the database to version " + version);

                try {
                    db.createStatement().executeUpdate("UPDATE _migration SET version = " + version);
                } catch (SQLException e) {
                    plugin.getLogger().severe("Unable to update migrations version!");
                    e.printStackTrace();
                    return false;
                }
            }
        }

        if (initialVersion != version) {
            plugin.getLogger().info("Migration from version " + initialVersion + " to version " + version + " successfull");
        } else {
            plugin.getLogger().info("Database is already on the newest version");
        }

        return true;
    }
}

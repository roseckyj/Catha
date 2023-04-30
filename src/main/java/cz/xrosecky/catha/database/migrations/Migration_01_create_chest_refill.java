package cz.emcify.runtime.database.migrations;

import cz.emcify.runtime.Runtime;
import cz.emcify.runtime.database.AbstractMigration;

public class Migration_01_create_chest_refill extends AbstractMigration {
    public Migration_01_create_chest_refill(Runtime plugin) {
        super(plugin, 1);
    }

    @Override
    protected boolean migrate() throws Exception {
        executeUpdate("CREATE TABLE IF NOT EXISTS chest_refill (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "x INTEGER NOT NULL," +
            "y INTEGER NOT NULL," +
            "z INTEGER NOT NULL," +
            "brokenAt INTEGER NOT NULL," +
            "material VARCHAR(256) NOT NULL," +
            "orientation VARCHAR(256) NOT NULL," +
            "lootTable VARCHAR(256) NOT NULL," +
            "customName VARCHAR(256) NOT NULL," +
            "refilled INTEGER NOT NULL" +
            ");");

        return true;
    }
}

package cz.emcify.runtime.database.migrations;

import cz.emcify.runtime.Runtime;
import cz.emcify.runtime.database.AbstractMigration;

public class Migration_02_add_looter_to_chest_refill extends AbstractMigration {
    public Migration_02_add_looter_to_chest_refill(Runtime plugin) {
        super(plugin, 2);
    }

    @Override
    protected boolean migrate() throws Exception {
        executeUpdate("ALTER TABLE chest_refill ADD looter VARCHAR(256);");

        return true;
    }
}

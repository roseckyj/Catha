package cz.xrosecky.catha;

import cz.xrosecky.catha.config.CathaConfig;
import cz.xrosecky.catha.config.ConfigManager;
import cz.xrosecky.catha.database.Database;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.metadata.PackMeta;
import team.unnamed.creative.server.ResourcePackServer;
import team.unnamed.creative.texture.Texture;
import team.unnamed.hephaestus.Model;
import team.unnamed.hephaestus.bukkit.BukkitModelEngine;
import team.unnamed.hephaestus.bukkit.v1_18_R2.BukkitModelEngine_v1_18_R2;
import team.unnamed.hephaestus.reader.ModelReader;
import team.unnamed.hephaestus.reader.blockbench.BBModelReader;
import team.unnamed.hephaestus.writer.ModelWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

// https://en.wikipedia.org/wiki/Catha_(mythology)

public final class Catha extends JavaPlugin implements Listener, Runnable {
    private CathaConfig config;
    private ResourcePackServer server;
    private ResourcePack resourcePack;
    public final ArrayList<Model> models = new ArrayList<>();
    public BukkitModelEngine modelEngine;
    public Font font;

    // Plugin startup logic
    @Override
    public void onEnable() {
        ConfigManager<CathaConfig> configManager = new ConfigManager<>(
                this,
                new File(getDataFolder(), "configuration.json"),
                CathaConfig.class
        );
        config = configManager.getConfiguration();

        modelEngine = BukkitModelEngine_v1_18_R2.create(this);

        this.getCommand("test").setExecutor(new TestCommand(this));

        getServer().getPluginManager().registerEvents(this, this);
        getServer().getScheduler().scheduleSyncDelayedTask(this, this, 20);
    }

    // Plugin shutdown logic
    @Override
    public void onDisable() {
        if (server != null) server.stop(0);
    }

    // On worlds load (20 ticks after)
    @Override
    public void run() {
        try {
            // ModelReader reader = BBModelReader.blockbench();
            // models.add(reader.read(new File("assets/models/villager.bbmodel")));

            resourcePack = ResourcePack.build(tree -> {
                tree.write(Metadata.builder()
                        .add(PackMeta.of(9, config.getResourcepackName()))
                        .build());

                Texture white = Texture.builder()
                        .key(Key.key("minimap", "white"))
                        .data(Writable.file(new File("assets/textures/white.png")))
                        .build();

                Texture transparent = Texture.builder()
                        .key(Key.key("minimap", "transparent"))
                        .data(Writable.file(new File("assets/textures/transparent.png")))
                        .build();

                tree.write(white);
                tree.write(transparent);

                ArrayList<FontProvider> providers = new ArrayList<>();

                int pixelSize = 1;

                providers.add(FontProvider.bitMap()
                        .file(transparent.key())
                        .height(-3)
                        .ascent(-32768)
                        .characters(
                                "!"
                        )
                        .build());

                providers.add(FontProvider.bitMap()
                        .file(transparent.key())
                        .height(-pixelSize - 3)
                        .ascent(-32768)
                        .characters(
                                "$"
                        )
                        .build());

                for (int i = 0; i < 200; i++) {
                    providers.add(FontProvider.bitMap()
                            .file(white.key())
                            .height(pixelSize)
                            .ascent(-i * pixelSize)
                            .characters(
                                    "" + (char)('0' + i)
                            )
                            .build());
                }

                font = Font.of(
                        Key.key("minimap", "pixels"),
                        providers);

                tree.write(font);

                // ModelWriter.resource("emcify").write(tree, models);
            });

            server = ResourcePackServer.builder()
                    .address("127.0.0.1", config.getServerPort())
                    .pack(resourcePack)
                    .build();

            server.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String hash = resourcePack.hash();
        String path = hash + ".zip";
        String url = config.getServerUrl() + path;
        // event.getPlayer().setResourcePack(url, hexStringToByteArray(hash), Component.text(config.getResourcepackPrompt()), true);
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public Database getDatabase() {
    }
}

package cz.xrosecky.catha;

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

public final class Catha extends JavaPlugin implements Listener {
    private ResourcePackServer server;
    private ResourcePack resourcePack;
    public final ArrayList<Model> models = new ArrayList<>();
    public BukkitModelEngine modelEngine;
    public Font font;

    @Override
    public void onEnable() {
        // Plugin startup logic

        try {
            ModelReader reader = BBModelReader.blockbench();
            models.add(reader.read(new File("assets/models/villager.bbmodel")));

            resourcePack = ResourcePack.build(tree -> {
                tree.write(Metadata.builder()
                        .add(PackMeta.of(9, "Description!"))
                        .build());

                Texture texture = Texture.builder()
                        .key(Key.key("emcify", "bubble"))
                        .data(Writable.file(new File("assets/textures/bubble.png")))
                        .build();

                tree.write(texture);

                FontProvider provider = FontProvider.bitMap()
                        .file(texture.key())
                        .height(19)
                        .ascent(10)
                        .characters(
                                "A"
                        )
                        .build();

                font = Font.of(
                        Key.key("emcify", "font"),
                        provider);

                tree.write(font);

                ModelWriter.resource("emcify").write(tree, models);
            });

            server = ResourcePackServer.builder()
                    .address("127.0.0.1", 7270)
                    .pack(resourcePack)
                    .build();

            server.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        modelEngine = BukkitModelEngine_v1_18_R2.create(this);

        this.getCommand("test").setExecutor(new TestCommand(this));

        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        server.stop(0);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String hash = resourcePack.hash();
        String path = hash + ".zip";
        String url = "http://127.0.0.1:7270/" + path;
        event.getPlayer().setResourcePack(url, hexStringToByteArray(hash), Component.text("Resource pack is required"), true);
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
}

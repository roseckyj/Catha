package cz.xrosecky.catha;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minecraft.world.level.material.MaterialColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_18_R2.util.CraftMagicNumbers;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.*;
import team.unnamed.hephaestus.Model;
import team.unnamed.hephaestus.bukkit.ModelEntity;

import java.nio.ByteBuffer;
import java.util.stream.Collectors;

public class TestCommand implements CommandExecutor {
    private final Catha plugin;

    public TestCommand(Catha plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

//            Model model = plugin.models.get(0);
//            ModelEntity entity = plugin.modelEngine.spawn(model, player.getLocation());
//
//            entity.setCollidable(false);
//            entity.setGravity(false);
//            entity.setInvulnerable(true);
//
//            entity.animationController().queue(model.animations().get("animation.model.farm"));
//
//            ArmorStand armorstand = player.getWorld().spawn(player.getLocation().add(0, 2.5, 0), ArmorStand.class);
//
//            armorstand.customName(Component.text().content("A").font(plugin.font.key()).build());
//            armorstand.setCustomNameVisible(true);
//            armorstand.setVisible(false);
//            armorstand.setMarker(true);
//
//            player.sendMessage(model.animations().keySet().stream().map(Object::toString).collect(Collectors.joining(", ")));

            int scale = 1;

            MapView view = Bukkit.createMap(player.getWorld());
            view.getRenderers().clear();
            view.addRenderer(new MapRenderer(){
                public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
                    for (int y = 0; y < 128; y++) {
                        for (int x = 0; x < 128; x++) {
                            double yaw = player.getLocation().getYaw() / 180.0 * Math.PI + Math.PI;
                            double cos = Math.cos(yaw);
                            double sin = Math.sin(yaw);
                            int xs = (x - 64) * scale;
                            int ys = (y - 64) * scale;
                            Location newPoint = player.getLocation().add(cos * xs - sin * ys, 0, sin * xs + cos * ys);
                            Block original = player.getWorld().getHighestBlockAt(newPoint);
                            Block shifted = player.getWorld().getHighestBlockAt(newPoint.add(-1, 0, -1));
                            net.minecraft.world.level.block.Block block = CraftMagicNumbers.getBlock(original.getType());
                            MaterialColor color = block.defaultMaterialColor();

                            MaterialColor.Brightness brightness = MaterialColor.Brightness.NORMAL;
                            if (original.getY() > shifted.getY()) {
                                brightness = MaterialColor.Brightness.HIGH;
                            } else if (original.getY() < shifted.getY() - 5) {
                                brightness = MaterialColor.Brightness.LOWEST;
                            } else if (original.getY() < shifted.getY()) {
                                brightness = MaterialColor.Brightness.LOW;
                            }

                            int rgb = color.calculateRGBColor(brightness);
                            byte[] bytes = ByteBuffer.allocate(4).putInt(rgb).array();
                            mapCanvas.setPixel(x, y, MapPalette.matchColor(Byte.toUnsignedInt(bytes[3]), Byte.toUnsignedInt(bytes[2]), Byte.toUnsignedInt(bytes[1])));
                            MapCursorCollection cursors = new MapCursorCollection();
                            cursors.addCursor(new MapCursor((byte) 0, (byte) 0, (byte)8, MapCursor.Type.WHITE_POINTER, true));
                            mapCanvas.setCursors(cursors);
                        }
                    }
                }
            });
            ItemStack map = new ItemStack(Material.FILLED_MAP, 1);
            MapMeta meta = (MapMeta) map.getItemMeta();
            meta.setMapView(view);
            map.setItemMeta((ItemMeta) meta);

            player.getInventory().setItemInOffHand(map);

//            int numpixels = 32;
//            Component result = Component.text("");
//            for (int x = 0; x < numpixels; x++) {
//                for (int y = 0; y < numpixels; y++) {
//                    double yaw = player.getLocation().getYaw() / 180.0 * Math.PI + Math.PI;
//                    double cos = Math.cos(yaw);
//                    double sin = Math.sin(yaw);
//                    int xs = x - numpixels / 2;
//                    int ys = y - numpixels / 2;
//                    int scale = 2;
//                    Location newPoint = player.getLocation().add(cos * xs * scale - sin * ys * scale, 0, sin * xs * scale + cos * ys * scale);
//                    net.minecraft.world.level.block.Block block = CraftMagicNumbers.getBlock(player.getWorld().getHighestBlockAt(newPoint).getType());
//                    MaterialColor color = block.defaultMaterialColor();
//
//                    int rgb = color.calculateRGBColor(MaterialColor.Brightness.NORMAL);
//                    byte[] bytes = ByteBuffer.allocate(4).putInt(rgb).array();
//                    result = result.append(Component.text("" + (char)('0' + y) + (y < numpixels - 1 ? "$" : "!"), TextColor.color(Byte.toUnsignedInt(bytes[3]), Byte.toUnsignedInt(bytes[2]), Byte.toUnsignedInt(bytes[1]))));
//                }
//            }
//
//            BossBar map = BossBar.bossBar(result.font(plugin.font.key()), 1, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS);
//            player.showBossBar(map);

        }
        return true;
    }
}

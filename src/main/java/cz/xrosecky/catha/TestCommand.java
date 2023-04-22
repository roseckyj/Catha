package cz.xrosecky.catha;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import team.unnamed.hephaestus.Model;
import team.unnamed.hephaestus.bukkit.ModelEntity;

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

            Model model = plugin.models.get(0);
            ModelEntity entity = plugin.modelEngine.spawn(model, player.getLocation());

            entity.setCollidable(false);
            entity.setGravity(false);
            entity.setInvulnerable(true);

            entity.animationController().queue(model.animations().get("animation.model.farm"));

            ArmorStand armorstand = player.getWorld().spawn(player.getLocation().add(0, 2.5, 0), ArmorStand.class);

            armorstand.customName(Component.text().content("A").font(plugin.font.key()).build());
            armorstand.setCustomNameVisible(true);
            armorstand.setVisible(false);
            armorstand.setMarker(true);

            player.sendMessage(model.animations().keySet().stream().map(Object::toString).collect(Collectors.joining(", ")));
        }
        return true;
    }
}

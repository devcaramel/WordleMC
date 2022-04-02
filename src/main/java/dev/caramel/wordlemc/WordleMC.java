package dev.caramel.wordlemc;

import dev.jorel.commandapi.CommandAPICommand;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.regex.Pattern;

public class WordleMC extends JavaPlugin {

    public static final Pattern ALPHABETIC = Pattern.compile("[a-zA-Z]+");

    @Getter private static WordleMC plugin;

    @Getter private final WordleManager manager = new WordleManager(this);

    public WordleMC() {

        super();

        plugin = this;

    }

    @Override
    public void onEnable() {

        saveResource("words.json", false);
        saveResource("solutions.json", false);

        manager.setup();

        registerCommand();

    }

    private void registerCommand() {

        new CommandAPICommand("wordle")
                .withSubcommand(new CommandAPICommand("play")
                        .executesPlayer((player, args)->{

                            manager.startWordleGame(player);

                        }))
                .withSubcommand(new CommandAPICommand("pause")
                        .executesPlayer((player, args)-> {

                            player.sendMessage("This feature is not yet supported.");

                        }))
                .withSubcommand(new CommandAPICommand("end")
                        .executesPlayer((player, args)-> {

                            manager.endWordleGame(player);

                        })).register();

    }

}

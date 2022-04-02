package dev.caramel.wordlemc;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.caramel.wordlemc.wordle.WordleGame;
import io.papermc.paper.event.player.AsyncChatEvent;
import io.papermc.paper.text.PaperComponents;
import lombok.Getter;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class WordleManager implements Listener {

    @Getter private final JavaPlugin plugin;
    @Getter private final Map<Player, WordleGame> games = new HashMap<>();

    @Getter private final Set<String> words;
    @Getter private final List<String> solutions;

    public WordleManager(@NotNull JavaPlugin plugin) {

        this.plugin = plugin;

        words = new HashSet<>();
        solutions = new ArrayList<>();

    }

    public @NotNull String getRandomSolution() {

        return solutions.get(RandomUtils.nextInt(solutions.size()));

    }

    public void setup() {

        Bukkit.getPluginManager().registerEvents(this, plugin);

        try {

            File wordsFile = new File(plugin.getDataFolder(), "words.json");
            File solutionsFile = new File(plugin.getDataFolder(), "solutions.json");

            if (!wordsFile.exists()) {

                wordsFile.createNewFile();

            }

            JsonArray words = JsonParser.parseReader(new FileReader(wordsFile)).getAsJsonObject().get("words").getAsJsonArray();
            JsonArray solutions = JsonParser.parseReader(new FileReader(solutionsFile)).getAsJsonObject().get("solutions").getAsJsonArray();

            words.forEach(element-> this.words.add(element.getAsString()));
            solutions.forEach(element-> {
                this.solutions.add(element.getAsString());
                this.words.add(element.getAsString());
            });

        } catch (IOException ioe) {

            ioe.printStackTrace();

        }

    }

    public void startWordleGame(@NotNull Player player) {

        if (games.containsKey(player)) {

            player.sendMessage("You are already in a game! Type \"/wordle end\" to end the game and reveal the word.");
            return;

        }

        WordleGame game = new WordleGame(player, getRandomSolution());
        games.put(player, game);

        player.sendMessage("-----------------------------");
        player.sendMessage("You have started a new Wordle game! You have 6 tries to guess a 5-letter word.");
        player.sendMessage("-----------------------------");

    }

    public void endWordleGame(@NotNull Player player) {

        WordleGame game = games.get(player);

        if (game == null) {

            player.sendMessage("You are not in a Wordle game!");
            return;

        }

        player.sendMessage("Your word was \"" + game.getSolution() + "\". Type \"/wordle play\" to start a new game!");

    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {

        WordleGame game = games.get(event.getPlayer());

        if (game == null) {

            return;

        }

        event.setCancelled(true);

        String message = PaperComponents.plainTextSerializer().serialize(event.message());

        if (message.length() != 5) {

            event.getPlayer().sendMessage("Your guess must be 5 letters in length.");
            return;

        }

        if (!WordleMC.ALPHABETIC.matcher(message).matches()) {

            event.getPlayer().sendMessage("You guess must only contain letters.");
            return;

        }

        if (!words.contains(message)) {

            event.getPlayer().sendMessage("Your guess is not a valid word!");
            return;

        }

        game.onGuess(message);

    }

}

package dev.caramel.wordlemc.wordle;

import dev.caramel.wordlemc.WordleMC;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class WordleGame {

    @Getter @NotNull private final Player player;
    @Getter @NotNull private final String solution;
    @Getter @NotNull private final List<WordleGuess> guesses;

    public WordleGame(@NotNull Player player, @NotNull String solution) {

        if (solution.length() != 5) {

            throw new IllegalArgumentException("Wordle solution must be 5 letters long! Given: " + solution);

        }
        if (!WordleMC.ALPHABETIC.matcher(solution).matches()) {

            throw new IllegalArgumentException("Wordle solution must be alphabetic!");

        }

        this.player = player;
        this.solution = solution;
        this.guesses = new LinkedList<>();

    }

    public void onGuess(@NotNull String guess) {

        WordleGuess newGuess = new WordleGuess(guess, solution);
        guesses.add(newGuess);

        for (WordleGuess wg : guesses) {

            player.sendMessage(wg.toPrettyString());

        }

        if (newGuess.isCorrect()) {

            player.sendMessage("That was correct, great job!");
            player.sendMessage("-----------------");
            WordleMC.getPlugin().getManager().getGames().remove(player);
            return;

        }

        if (guesses.size() == 6) {

            player.sendMessage("So close. Better luck next time!");
            player.sendMessage("-----------------");
            WordleMC.getPlugin().getManager().getGames().remove(player);
            return;

        }

        player.sendMessage("You have " + (6-guesses.size()) + " guesses left. Think!");
        player.sendMessage("-----------------");
        return;

    }

}

package dev.caramel.wordlemc.wordle;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public class WordleGuess {

    @Getter private final String guess;
    @Getter private final Accuracy[] accuracy;

    public WordleGuess(@NotNull String guess, @NotNull String solution) {

        if (guess.length() != solution.length()) {

            throw new IllegalArgumentException("Wordle guess and solution must be of equal length!");

        }

        this.guess = guess;
        this.accuracy = new Accuracy[guess.length()];

        calculate(solution);

    }

    /**
     * Rough algorithm based on Wordle.
     * Can probably be optimized/not 100% accurate.
     *
     * @param word the word being guessed
     */
    private void calculate(@NotNull String word) {

        char[] solution = word.toCharArray();
        char[] guess = this.guess.toCharArray();

        for (int i = 0; i != accuracy.length; i++) {

            if (guess[i] == solution[i]) {

                solution[i] = ' ';
                accuracy[i] = Accuracy.GREEN;

            }

        }

        for (int i = 0; i != accuracy.length; i++) {

            if (accuracy[i] == Accuracy.GREEN) continue;

            int positionInSolution = contains(guess[i], solution);

            if (positionInSolution == -1) {

                accuracy[i] = Accuracy.RED;

            } else {

                solution[i] = ' ';
                accuracy[i] = Accuracy.YELLOW;

            }

        }

    }

    public boolean isCorrect() {

        for (Accuracy a : accuracy) {

            if (a != Accuracy.GREEN) return false;

        }

        return true;

    }

    private int contains(char character, char[] array) {

        for (int i = 0; i != array.length; i++) {

            if (array[i] == character) return i;

        }

        return -1;

    }

    @Override
    public @NotNull String toString() {

        StringBuilder builder = new StringBuilder("[guess: " + guess + ", (");

        for (Accuracy a : this.accuracy) {

            builder.append(a.code);

        }

        builder.append(")]");

        return builder.toString();

    }

    public @NotNull String toPrettyString() {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i != guess.length(); i++) {

            builder.append("&").append(accuracy[i].color).append(guess.charAt(i)).append(' ');

        }

        builder.trimToSize();

        return ChatColor.translateAlternateColorCodes('&', builder.toString());

    }

    public enum Accuracy {

        RED('R', 'c'),
        YELLOW('Y', 'e'),
        GREEN('G', 'a');

        @Getter private final char code;
        @Getter private final char color;

        Accuracy(char code, char color) {

            this.code = code;
            this.color = color;

        }

    }

}

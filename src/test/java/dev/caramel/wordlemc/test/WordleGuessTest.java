package dev.caramel.wordlemc.test;

import dev.caramel.wordlemc.wordle.WordleGame;
import dev.caramel.wordlemc.wordle.WordleGuess;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

public class WordleGuessTest {

    @Test
    public void test() {

        String solution = "apple";

        String[] guessStrings = {"beast", "clams", "eagle", "timid", "blank", "apple"};

        List<WordleGuess> guesses = new LinkedList<>();

        for (String guess : guessStrings) {

            guesses.add(new WordleGuess(guess, solution));

        }

        for (WordleGuess wordleGuess : guesses) {

            System.out.println(wordleGuess.toString());

        }

    }

}

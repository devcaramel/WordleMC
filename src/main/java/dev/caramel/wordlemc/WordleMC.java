package dev.caramel.wordlemc;

import lombok.Getter;

public class WordleMC {

    @Getter private static WordleMC plugin;

    public WordleMC() {

        plugin = this;

    }

}

package com.example.moodmixer;



public enum Mood {
    HAPPY('\uD83D', "Happy"),
    SAD('s',"Sad"),
    NEUTRAL('n',"Neutral");
    public final char emoji;
    public final String name;

    Mood(char emoji, String name) {
        this.emoji = emoji;
        this.name = name;
    }


}

package com.example.moodmixer;



public enum Mood {
    HAPPY("\uD83D\uDE03", "Happy"),
    SAD("\uD83D\uDE22","Sad"),
    STRESSED("\uD83E\uDD2F","Stressed"),
    ANGRY("😡","Angry"),
    CALM("\uD83D\uDE0E","Calm"),
    OPTIMISTIC("\uD83D\uDE01","Optimistic");

    public final String emoji;
    public final String name;

    Mood(String emoji, String name) {
        this.emoji = emoji;
        this.name = name;
    }


}

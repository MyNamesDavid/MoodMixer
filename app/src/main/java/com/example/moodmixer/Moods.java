package com.example.moodmixer;


import java.util.Arrays;

public class Moods {

    private static final int WEEK_MOODS = 10;
    private static int counter;
    private Mood[] mood;
    private Mood desiredMood;

    public Moods (){
        mood = new Mood[WEEK_MOODS];
    }

    public void putMoodToCollection(Mood currentMood){
        if(mood.length == WEEK_MOODS){
            for (int i = 0; i < WEEK_MOODS - 1; i++) {
                mood[i] = mood[i + 1];
            }
            this.mood[counter] = currentMood;
        } else {
            this.mood[counter] = currentMood;
            counter++;
        }
    }

    public void setDesiredMood(Mood desiredMood){
        this.desiredMood = desiredMood;
    }

    public String getDesiredMood(){return desiredMood.name;}

    public String getCurrentEmoji(){
        return mood[counter].emoji;
    }

    public String getCurrentMoodName(){
        return mood[counter].name;
    }

    public void clearPastMoods(){
        mood = null;
        counter = 0;
    }


    public String showMoods(){
        return Arrays.toString(mood);
    }
}

package pl.hexlin.task;

import pl.hexlin.Instance;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;

public class QuestTask extends TimerTask {
    public final Instance instance;

    public QuestTask(Instance instance) {
        this.instance = instance;
    }

    @Override
    public void run() {
        LocalTime time = LocalTime.now();

        if (time.getMinute() == 0 && time.getSecond() == 0) {
            switch (time.getHour()) {
                case 12, 16, 20 -> {
                    instance.getQuestHandler().guessCountryFlag();
                }
                case 14, 18, 23 -> {
                    instance.getQuestHandler().guessCountryCapital();
                }
            }
        }
    }
}

/**
 * Created by Maciek on 02.03.2018
 */

package saper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.prefs.Preferences;

import static saper.Saper.logger;

class HighScores {
    private Preferences prefs;
    private TreeSet<Integer> scoresList;
    private int number;
    private static HighScores ourInstance = new HighScores();
    private static final String N_KEY = "scores_number";
    private static final int LIST_LENGTH = 5;

    public static HighScores getInstance() {
        return ourInstance;
    }

    public static void main(String[] args) {
        logger.debug("Executing Highscores.main");
//        ourInstance.eraseScores();

        int randomNum = ThreadLocalRandom.current().nextInt(1, 1001);

        ourInstance.addScore(randomNum);
    }

    private HighScores() {
        logger.debug("Highscores constructor");
        prefs = Preferences.userRoot().node(this.getClass().getName());
        loadHighScores();
    }

    private void loadHighScores() {
        logger.debug("Loading High Scores");
        number = prefs.getInt("scores_number", 0);
        scoresList = new TreeSet<>();
        for (int i = 0; i < number; i++) {
            scoresList.add(prefs.getInt(Integer.toString(i), 0));
            logger.trace("Load: {}", prefs.getInt(Integer.toString(i), 0));
        }
    }

    void addScore(int value) {
        logger.debug("Checking if highscore, time: {}", value);
        logger.info("Highscores (number: {}):", number);
        if (scoresList.isEmpty() || number < LIST_LENGTH || value < scoresList.last()) {
            logger.info("Highscore!");

            scoresList.add(value);

            logger.debug("Recording list");
            Iterator<Integer> iter = scoresList.iterator();
            for (int i = 0; iter.hasNext(); i++) {
                int val = iter.next();
                if (i >= LIST_LENGTH) {
                    scoresList.remove(val);
                    logger.debug("Trimming to {}", LIST_LENGTH);
                } else {
                    prefs.putInt(Integer.toString(i), val);
                    logger.trace("Adding: {}", val);
                }
            }
            number = scoresList.size();
            prefs.putInt(N_KEY, number);
            loadHighScores();
        } else {
            logger.debug("Not a highscore");
        }

        Iterator<Integer> iter = scoresList.iterator();
        for (int i = 0; iter.hasNext(); i++) {
            int val = iter.next();
            logger.info("{} : {} {}", i, val, (val == value ? "***" : ""));
        }

    }

    private void eraseScores() {
        logger.debug("Erasing list");
        for (int i = 0; i < 5; i++) {
            prefs.remove(Integer.toString(i));
        }
        number = 0;
        prefs.putInt(N_KEY, 0);
        loadHighScores();
    }
}

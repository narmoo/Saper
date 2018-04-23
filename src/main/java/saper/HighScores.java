/**
 * Created by Maciek on 02.03.2018
 */

package saper;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.prefs.Preferences;

import static saper.Saper.logger;

class HighScores {
    private Preferences prefs;
    private ArrayList<Integer> scoresList;
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
        scoresList = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            scoresList.add(prefs.getInt(Integer.toString(i), 0));
            logger.trace("Load: {}", prefs.getInt(Integer.toString(i), 0));
        }
    }

    void addScore(int value) {
        logger.debug("Checking if highscore, time: {}", value);
        logger.info("Highscores (number: {}):", number);
        if (scoresList.isEmpty() || number < LIST_LENGTH || value < scoresList.get(number - 1)) {
            logger.info("Highscore!");

            int insertionPoint = Collections.binarySearch(scoresList, value);
            if (insertionPoint < 0) insertionPoint = -insertionPoint - 1;
            logger.debug("InsertionPoint: {}", insertionPoint);
            scoresList.add(insertionPoint, value);

            logger.debug("Recording list");
            for (int i = 0; i <= scoresList.size(); i++) {
                int val = scoresList.get(i);
                if (i >= LIST_LENGTH) {
                    scoresList.remove(i);
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

/**
 * Created by Maciek on 02.03.2018
 */

package saper;

import java.util.ArrayList;
import java.util.Comparator;
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
            scoresList.add(i, prefs.getInt(Integer.toString(i), 0));
        }
    }

    void addScore(int value) {
        logger.debug("Checking if highscore, time: {}", value);
        logger.info("Highscores (number: {}):", number);
        if (scoresList.isEmpty() || number < LIST_LENGTH || value < scoresList.get(number - 1)) {
            logger.debug("Highscore!");

            scoresList.add(value);
            scoresList.sort(Comparator.naturalOrder());

            if (scoresList.size() == LIST_LENGTH + 1) {
                scoresList.remove(LIST_LENGTH);
                logger.debug("Trimming to {}", LIST_LENGTH);
            }
            logger.debug("Recording list");
            for (int i = 0; i < scoresList.size(); i++) {
                prefs.putInt(Integer.toString(i), scoresList.get(i));
            }
            number = scoresList.size();
            prefs.putInt(N_KEY, number);
            loadHighScores();
        } else {
            logger.debug("Not a highscore");
        }

        for (int i = 0; i < scoresList.size(); i++) {
            logger.info("{} : {} {}", i, scoresList.get(i), (scoresList.get(i) == value ? "***" : ""));
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

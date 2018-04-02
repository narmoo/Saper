/**
 * Created by Maciek on 06.02.2018
 */

package saper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class Saper {
    public static final int X_SMALL = 8;
    public static final int Y_SMALL = 8;
    public static final int X_MEDIUM = 16;
    public static final int Y_MEDIUM = 16;
    public static final int X_LARGE = 32;
    public static final int Y_LARGE = 32;
    static final double PROBABILITY = 0.2;
    static Logger logger = LoggerFactory.getLogger(Saper.class);

    public static void main(String[] args) {
        logger.info("Starting app. Date: {}", new Date().toString());
        logger.debug("Probability: {}, Size X: {}, Size Y: {}", PROBABILITY, X_SMALL, Y_SMALL);
        new Board(X_SMALL, Y_SMALL);
    }
}

/**
 * Created by Maciek on 06.02.2018
 */

package saper;

import java.awt.*;
import java.util.ArrayList;

import static saper.Saper.PROBABILITY;
import static saper.Saper.logger;

public class Board {
    private Field[][] field;
    private int sizeX;
    private int sizeY;

    private int flaggedCount;
    private int unrevealedCount;
    private int bombsNumber;
    private BoardPanel buttonPanel;
    static final int DEFAULT_WIDTH = 400;
    static final int DEFAULT_HEIGHT = 400;

    Board(int rows, int columns) {
        logger.debug("Board constructor");
        flaggedCount = 0;
        CommandTransfer.getInstance().setListener(this);
        EventQueue.invokeLater(() -> createFields(rows, columns));
    }

    /**
     * Tworzy tablicę przycisków z polami
     * @param columns liczba rzędów pól
     * @param rows liczba szeregów pól
     */
    private void createFields(int columns, int rows) {
        int xLosowa;
        int yLosowa;

        bombsNumber = (int) (PROBABILITY * columns * rows);
        logger.info("Commence Board generation. Bombsnumber: {}", bombsNumber);

        unrevealedCount = columns * rows - bombsNumber;

        sizeX = columns;
        sizeY = rows;

        logger.info("Creating buttonPanel, size X: {}, Y: {}", sizeX, sizeY);
        buttonPanel = new BoardPanel(sizeX, sizeY);
        buttonPanel.updateFlagCounter(bombsNumber);

        // Tworzy przyciski
        logger.info("Creating buttons");
        field = new Field[columns][rows];


        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                field[x][y] = new Field(x, y, this);
            }
        }

        // Przypisuje bomby
        logger.info("Setting up bombs");
        int z = 0;
        while (z < bombsNumber) {
            xLosowa = (int) (Math.random() * columns);
            yLosowa = (int) (Math.random() * rows);
            if (!field[xLosowa][yLosowa].getWithBomb()) {
                field[xLosowa][yLosowa].setWithBomb(true);
                z++;

            }
        }

        logger.info("Setting up nearby bombs numbers");
        // Ustawia ilość bomb w pobliżu
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                logger.trace("Checking X: {}, Y: {}", x, y);
                if(!field[x][y].getWithBomb()) {
                    // Na górze
                    if (y > 0 && field[x][y - 1].getWithBomb()) {
                        field[x][y].increaseNeighbours();
                        logger.trace("Found bomb X: {}, Y: {}", x, y - 1);
                    }
                    // Na dole
                    if (y < rows - 1 && field[x][y + 1].getWithBomb()) {
                        field[x][y].increaseNeighbours();
                        logger.trace("Found bomb X: {}, Y: {}", x, y + 1);
                    }
                      // Po lewej
                    if (x > 0 && field[x - 1][y].getWithBomb()) {
                        field[x][y].increaseNeighbours();
                        logger.trace("Found bomb X: {}, Y: {}", x - 1, y);
                    }
                    // Po prawej
                    if (x < columns - 1 && field[x + 1][y].getWithBomb()) {
                        field[x][y].increaseNeighbours();
                        logger.trace("Found bomb X: {}, Y: {}", x + 1, y);
                    }
                    // Po lewej góra
                    if (x > 0 && y > 0 && field[x - 1][y - 1].getWithBomb()) {
                        field[x][y].increaseNeighbours();
                        logger.trace("Found bomb X: {}, Y: {}", x - 1, y - 1);
                    }
                    // Po lewej dół
                    if (x > 0 && y < rows - 1 && field[x - 1][y + 1].getWithBomb()) {
                        field[x][y].increaseNeighbours();
                        logger.trace("Found bomb X: {}, Y: {}", x - 1, y + 1);
                    }
                    // Po prawej góra
                    if (x < columns - 1 && y > 0 && field[x + 1][y - 1].getWithBomb()) {
                        field[x][y].increaseNeighbours();
                        logger.trace("Found bomb X: {}, Y: {}", x + 1, y - 1);
                    }
                    // Po prawej dół
                    if (x < columns - 1 && y < rows - 1 && field[x + 1][y + 1].getWithBomb()) {
                        field[x][y].increaseNeighbours();
                        logger.trace("Found bomb X: {}, Y: {}", x + 1, y + 1);
                    }
                }
            }
        }
    }

    /**
     * Sprawdza po kliknięciu, czy jest bomba.
     * @param posX Pozycja x testowanego pola
     * @param posY POzycja y testowanego pola
     */
    private boolean testField(int posX, int posY) {
        return  field[posX][posY].getWithBomb();
    }

    private void updateCounter(int amount) {
        unrevealedCount += amount;
    }

    private void findZeroes(int posX, int posY) {
        logger.debug("Find zeroes X: {}, Y: {}", posX, posY);
        // Dwie listy - aktualna i następna
        ArrayList<Field> workingField = new ArrayList<>();
        ArrayList<Field> nextWorkingField = new ArrayList<>();

        // Dodaje pierwsze pole - to które zostało kliknięte
        workingField.add(field[posX][posY]);

        // Skanuje pola dookoła. Pomijając pole odkryte wcześniej odkrywa wszystkie inne.
        while (!workingField.isEmpty()) {
            for (Field f:workingField) {
                int x = f.getPosX();
                int y = f.getPosY();
                for (Direction direction : Direction.returnAll()) {
                    int newX = x;
                    int newY = y;
                    switch (direction) {
                        // W prawo
                        case RIGHT:
                            newX++;
                            break;
                        // W lewo
                        case LEFt:
                            newX--;
                            break;
                        // W górę
                        case UP:
                            newY--;
                            break;
                        // W dół
                        case DOWN:
                            newY++;
                            break;
                        // W górę lewo
                        case UP_LEFT:
                            newY--;
                            newX--;
                            break;
                        // W górę prawo
                        case UP_RIGHT:
                            newY--;
                            newX++;
                            break;
                        // W dół lewo
                        case DOWN_LEFT:
                            newY++;
                            newX--;
                            break;
                        // W dół prawo
                        case DOWN_RIGHT:
                            newY++;
                            newX++;
                            break;
                    }
                    if (newX >= 0 && newY >= 0 && newX < sizeX  && newY < sizeY && !field[newX][newY].isClicked()) {
                        // Zerowe pola są dodawane do następnej listy
                        if (field[newX][newY].getNeighbours() == 0) {
                            nextWorkingField.add(field[newX][newY]);
                        }
                        field[newX][newY].displayContent();
                        updateCounter(-1);
                    }
                }
            }

            // Następna lista staje się aktualną listą.
            workingField.clear();
            workingField.addAll(nextWorkingField);
            nextWorkingField.clear();
        }
    }

    public void clickField(int posX, int posY) {
        logger.debug("Left click X: {}, Y: {}", posX, posY);
        if (!field[posX][posY].getFlag()) {
            // Przegrana
            if (testField(posX,posY)) {
                for (int y = 0; y < sizeY; y++) {
                    for (int x = 0; x < sizeX; x++) {
                        if(testField(x, y)) {
                            field[x][y].displayContent();
                        }
                    }
                }
                finishGame(false);
            } else {
                field[posX][posY].displayContent();
                if (field[posX][posY].getNeighbours() == 0) {
                    findZeroes(posX, posY);
                }
                updateCounter(-1);

                // Wygrana
                if (unrevealedCount == 0) {
                    finishGame(true);
                }
            }

        }
    }

    public void altClickField(int posX, int posY) {
        logger.debug("Right click X: {}, Y: {}", posX, posY);
        Field f = field[posX][posY];
        f.setFlag(!f.getFlag());
        if (f.getFlag()) {
            flaggedCount++;
        } else {
            flaggedCount--;
        }
        buttonPanel.updateFlagCounter(bombsNumber - flaggedCount);
    }

    public void add(FieldBtn button) {
        try {
            buttonPanel.addBtn(button);
        } catch (NullPointerException e) {
            logger.error("Board.add NullPointerException");
            if (buttonPanel == null) {
                logger.error("buttonPanel not initialised");
            }
            if (button == null) {
                logger.error("button not initialised");
            }
        }
    }

    enum Direction {
        LEFt, RIGHT, UP, DOWN, UP_LEFT, UP_RIGHT, DOWN_RIGHT, DOWN_LEFT;
        public static Direction[] returnAll() {
            return new Direction[]{LEFt, RIGHT, UP, DOWN, UP_LEFT, UP_RIGHT, DOWN_RIGHT, DOWN_LEFT};
        }
    }

    public void finishGame(boolean isVictory) {
        if (isVictory) {
            logger.info("Finish: Win");
            buttonPanel.doWin();
            HighScores.getInstance().addScore(buttonPanel.getTime());
        } else {
            logger.info("Finish: Lost");
            buttonPanel.doGameOver();
        }
        System.exit(0);
    }
}
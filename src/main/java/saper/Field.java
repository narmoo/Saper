/**
 * Created by Maciek on 21.02.2018
 */

package saper;

import static saper.Field.ContentType.*;

public class Field {
    private boolean withBomb;
    private int neighbours;
    private boolean flag;
    private int posX;
    private int posY;
    private FieldBtn button;
    private boolean clicked;

    public enum ContentType {BOMB, BLANK, NEIGHBOUR}

    Field (int x, int y, Board board) {
        super();
        posX = x;
        posY = y;
        flag = false;
        button = new FieldBtn(posX, posY, board);
        board.add(button);
    }

    public void setFlag (boolean newFlag) {
        flag = newFlag;
        button.setFlag(flag);
    }

    public boolean getFlag() {
        return flag;
    }

    public boolean getWithBomb() {
        return withBomb;
    }

    public void setWithBomb(boolean b) {
        withBomb = b;
    }

    public int getNeighbours() {
        return neighbours;
    }

    public void increaseNeighbours() {
        neighbours++;
    }

    public void displayContent(){
        if (this.getWithBomb()) {
            button.displayContent(BOMB);
        } else if (neighbours > 0){
            button.displayContent(NEIGHBOUR, neighbours);
        } else {
            button.displayContent(BLANK);
        }
        this.clicked = true;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public boolean isClicked() {
        return clicked;
    }
}
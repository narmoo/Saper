/**
 * Created by Maciek on 07.02.2018
 */

package saper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FieldBtn extends JButton {
    private Board board;

    FieldBtn(int posX, int posY, Board board) {
        this.board = board;
        addActionListener(event -> {
            board.clickField(posX, posY);
        });
        addMouseListener(new FieldMouseAction(posX, posY));
    }

    public void setFlag (boolean newFlag) {
        if (newFlag) {
            setText("F");
        } else {
            setText("");
        }
    }

    public String getFlag () {
        return getText();
    }

    /**
     * Jesli flaga content to BOMB, to wyswietli bombe w przycisku. Nastepnie na chwile zmieni kolor z dezaktywuje pole.
     * @param content flaga rodzaju zawartosci pola
     */
    public void displayContent(Field.ContentType content){
        if (content == Field.ContentType.BOMB) {
            this.setText("B");
        }
        // Na chwilę zmienia kolor tekstu
        this.setForeground(this.getBackground());
        new Timer(100, e -> this.setForeground(Color.BLACK)).start();
        this.setEnabled(false);
    }

    /**
     * Wyswietla numer oznaczajacy ilosc sasiadow, a nastepnie wykonuje druga wersje tej metody z flaga BLANK
     * @param content Rodzaj zawartosci obiektu. Ta metoda jest wykonywana tylko jesli to NEIGHBOUR
     * @param number ilosc sasiadow
     */
    public void displayContent(Field.ContentType content, int number) {
        if (content == Field.ContentType.NEIGHBOUR) {
            this.setText(Integer.toString(number));
        }
        this.displayContent(Field.ContentType.BLANK);
    }

    private class FieldMouseAction extends MouseAdapter {
        private int posX;
        private int posY;

        FieldMouseAction(int posX, int posY) {
            this.posX = posX;
            this.posY = posY;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if ((e.getModifiersEx() & InputEvent.BUTTON3_DOWN_MASK) != 0) {
                board.altClickField(posX, posY);
            }
        }
    }
}
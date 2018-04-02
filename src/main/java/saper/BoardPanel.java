/**
 * Created by Maciek on 22.02.2018
 */

package saper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static saper.Board.DEFAULT_HEIGHT;
import static saper.Board.DEFAULT_WIDTH;
import static saper.Saper.logger;

public class BoardPanel extends JFrame {

    private JTextField toRevealTxt;
    private JTextField timerTxt;
    private JPanel buttonPanel;
    private int time;
    private boolean timerRunning;

    public BoardPanel(int columns, int rows) {
        logger.debug("Creating Board Panel");
        logger.debug("Setting LookAndFeel");
        UIManager.LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
        try {
            UIManager.setLookAndFeel(infos[1].getClassName());
        } catch (Exception e) {
            logger.error("BoardPanel.createBoard Exception: " + e.toString());
        }

        setTitle("Board");
        logger.debug("Setting exit");
//        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        logger.debug("Creating panels");
        // Tworzy panele
        JPanel controlPanel = new JPanel();
        buttonPanel = new JPanel();
        toRevealTxt = new JTextField("", 3);
        toRevealTxt.setEditable(false);
        timerTxt = new JTextField("0",3);
        timerTxt.setEditable(false);
        JButton winBtn = new JButton("Win!");
        winBtn.addActionListener(event -> CommandTransfer.getInstance().doFinish(true));
        controlPanel.add(new JLabel("Bomb zostalo: "));
        controlPanel.add(toRevealTxt);
        controlPanel.add(new JLabel("Czas: "));
        controlPanel.add(timerTxt);
        controlPanel.add(winBtn);

        logger.debug("Creating timer");
        time = 0;
        timerRunning = true;
        new Timer(1000, evt -> {
            if (timerRunning) {
                time++;
            }
            timerTxt.setText(Integer.toString(time));
        }).start();

        add(controlPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        setVisible(true);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT + controlPanel.getHeight());

        buttonPanel.setLayout(new GridLayout(columns, rows));

        logger.debug("Centering window");
        // Centrowanie okienka
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        int frameWidth = getWidth();
        int frameHeight = getWidth();
        setLocation(screenWidth / 2 - frameWidth / 2, screenHeight / 2 - frameHeight / 2);
    }

    public void doGameOver() {
        timerRunning = false;
        JOptionPane.showMessageDialog(this, "GAME OVER!");
    }

    public void doWin() {
        timerRunning = false;
        JOptionPane.showMessageDialog(this, "WIN!");
    }

    public void updateFlagCounter(int number) {
        toRevealTxt.setText(Integer.toString(number));
    }

    public void addBtn(FieldBtn button) {
        try {
            buttonPanel.add(button);
        } catch (NullPointerException e){
            logger.error("FieldBtn.addBtn NullPointerException");
            if (buttonPanel == null) {
                logger.error("buttonPanel not initialised");
            }
        }
    }

    public int getTime() {
        return time;
    }
}
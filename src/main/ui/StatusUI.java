package ui;

import javax.swing.*;
import java.awt.*;

//This class generates a JInternalFrame that holds all the user information.
public class StatusUI extends JInternalFrame {
    protected static final int WIDTH = 400;
    protected static final int HEIGHT = 150;
    private GameUI gameUI;
    protected PrintUserStatus status;

    //construct the frame with customized size and location, and a PrintUserStatus.
    public StatusUI(GameUI gameUI) {
        super("Player Status", false, false, false, false);
        this.gameUI = gameUI;
        status = new PrintUserStatus(this);
        add(status);
        pack();
        setSize(new Dimension(WIDTH, HEIGHT));
        setLocation((int) (gameUI.getSize().getWidth() - WIDTH - 13),
                    (int) (gameUI.getSize().getHeight() - HEIGHT - 34));
        setVisible(true);

    }

    public GameUI getGameUI() {
        return gameUI;
    }
}

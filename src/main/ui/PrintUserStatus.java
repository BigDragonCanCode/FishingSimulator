package ui;

import javax.swing.*;
import java.awt.*;

//This class manages the individual messages that are displayed in the StatusUI.
public class PrintUserStatus extends JPanel {
    protected static final int WIDTH = 400;
    protected static final int HEIGHT = 50;

    private StatusUI statusUI;

    private JLabel personalInfo;
    private JLabel amountInfo;
    private JLabel quotaInfo;
    private JLabel regInfo;
    private JLabel fineInfo;


    //construct the panel with empty infos.
    public PrintUserStatus(StatusUI statusUI) {
        this.statusUI = statusUI;
        setSize(WIDTH, HEIGHT);
        setLayout(new GridLayout(5, 1));

        personalInfo = new JLabel("Game not started.");
        regInfo = new JLabel();
        amountInfo = new JLabel();
        quotaInfo = new JLabel();
        fineInfo = new JLabel();

        add(personalInfo);
        add(amountInfo);
        add(quotaInfo);
        add(regInfo);
        add(fineInfo);
        setVisible(true);
    }

    //MODIFIES: this
    //EFFECTS:  update the infos with the newest values so the correct information is displayed.
    public void update() {
        personalInfo.setText("Name: " + statusUI.getGameUI().getFisher().getName()
                + " | Current balance: $" + statusUI.getGameUI().getFisher().getBalance());
        amountInfo.setText("# fish captured today: " + statusUI.getGameUI().getReg().getCapturedAmount());
        quotaInfo.setText("Is the player violating the daily quota?: " + statusUI.getGameUI().getReg().isOverLimit());
        regInfo.setText("Current violation times: " + statusUI.getGameUI().getReg().getViolationTimes());
        fineInfo.setText("Unpaid fine: " + statusUI.getGameUI().getReg().getUnPaidFine());
    }
}

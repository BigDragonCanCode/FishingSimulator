package ui;

import model.*;
import model.Event;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;


//the main UI holds the stored information and controls the load & save.
public class GameUI extends JFrame {
    protected static final int WIDTH = 1000;
    protected static final int HEIGHT = 600;
    private JDesktopPane desktop;

    private StatusUI statusUI;
    private FishingUI fishingUI;
    private MarketUI marketUI;

    private JButton startButton;
    private JButton quitButton;
    private JButton startNewDayButton;
    private JPanel buttonPanel;

    private Fisher fisher;
    private Inventory inventory;
    private Market market;
    private Regulation reg;

    //creates the GUI window, instantiate the other GUIs, and load & save buttons.
    public GameUI() {
        desktop = new JDesktopPane();
        setContentPane(desktop);
        setTitle("CPSC 210: Fisher Simulator");
        setSize(WIDTH, HEIGHT);

        statusUI = new StatusUI(this);
        fishingUI = new FishingUI(this);
        marketUI = new MarketUI(this);

        addButtonPanel();
        desktop.add(statusUI);
        desktop.add(fishingUI);
        desktop.add(marketUI);

        centreOnScreen();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    //MODIFIES: this
    //EFFECTS: add the buttons to a panel and disable the buttons.
    private void addButtonPanel() {
        startButton = new JButton(new StartGameAction());
        startNewDayButton = new JButton(new NewDayAction());
        quitButton = new JButton(new QuitGameAction());

        startNewDayButton.setEnabled(false);

        buttonPanel = new JPanel();
        buttonPanel.setSize(WIDTH,30);

        buttonPanel.add(startButton);
        buttonPanel.add(startNewDayButton);
        buttonPanel.add(quitButton);

        add(buttonPanel, BorderLayout.PAGE_END);
    }

    //https://github.students.cs.ubc.ca/CPSC210/B02-SpaceInvadersBase/blob/main/src/main/ca/ubc/cpsc210/spaceinvaders
    // Centres frame on desktop
    // modifies: this
    // effects:  location of frame is set so frame is centred on desktop
    private void centreOnScreen() {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        setLocation((width - getWidth()) / 2, (height - getHeight()) / 2);
    }

    //the callback class for startButton
    private class StartGameAction extends AbstractAction {

        StartGameAction() {
            super("Start Game");
        }

        //MODIFIES: this
        //EFFECTS: use a popup window to ask if the player want to start a new game or load.
        //         Display the fish storage and enable the buttons.
        //         Messages are popped up to notify players.
        @Override
        public void actionPerformed(ActionEvent evt) {
            int choice = JOptionPane.showConfirmDialog(null,
                    "Do you want to load the previous stored game?");
            if (choice == 0) {
                startButton.setEnabled(false);
                load();
                statusUI.status.update();
                fishingUI.getInventoryUI().displayList();
                JOptionPane.showMessageDialog(null, "Game loaded");
            } else if (choice == 1) {
                startButton.setEnabled(false);
                createNewGame();
                statusUI.status.update();
                fishingUI.getInventoryUI().displayList();
                JOptionPane.showMessageDialog(null, "A new game started");
            }
        }

        //MODIFIES: this
        //EFFECTS: start a new game by initializing Fisher, Inventory, Regulation, and Market objects.
        //         enable the buttons.
        private void createNewGame() {
            fisher = new Fisher("s");
            inventory = new Inventory();
            reg = new Regulation(fisher);
            market = new Market(fisher, inventory, reg);
            buttonsEnable();
        }

        //MODIFIES: this
        //EFFECTS: load the game by reading from the saved json files.
        //         enable the buttons.
        private void load() {
            try {
                fisher = JsonReader.readFisher("./data/fisher.json");
                inventory = JsonReader.readInventory("./data/inventory.json");
                reg = JsonReader.readRegulation("./data/regulation.json");
                reg.addFisher(fisher);
                market = new Market(fisher, inventory, reg);
                buttonsEnable();

            } catch (IOException e) {
                System.out.println("Unable to read from file.");
            }
        }

        //MODIFIES: this
        //EFFECTS: enable all the buttons.
        private void buttonsEnable() {
            fishingUI.setAddFishButton(true);
            fishingUI.setReleaseFishButton(true);
            fishingUI.setSellFishButton(true);
            marketUI.setFineButton();
            startNewDayButton.setEnabled(true);

            if (fisher.hasFishingRod()) {
                marketUI.setRodButton(false);
            } else {
                marketUI.setRodButton(true);
            }
            if (fisher.hasFishingLicense()) {
                marketUI.setLicenseButton(false);
            } else {
                marketUI.setLicenseButton(true);
            }
        }
    }

    //the callback class for quitButton
    private class QuitGameAction extends AbstractAction {

        //construct the object with name
        public QuitGameAction() {
            super("Quit game");
        }

        //EFFECTS: asking if the user want to save the game before quitting or not.
        //         if yes, write the saved json file, otherwise quit immediately.
        @Override
        public void actionPerformed(ActionEvent evt) {
            int choice = JOptionPane.showConfirmDialog(desktop,
                    "Do you want to save the game before quitting?");
            if (choice == 0) {
                try {
                    saveGame();
                    JOptionPane.showMessageDialog(desktop, "Game saved!");
                    printLog(EventLog.getInstance());
                } catch (NullPointerException e) {
                    JOptionPane.showMessageDialog(desktop, "Game not started, nothing is saved, bye!");
                }
                System.exit(0);
            } else if (choice == 1) {
                JOptionPane.showMessageDialog(desktop, "You chose to not save the progress, goodbye!");
                printLog(EventLog.getInstance());
                System.exit(0);
            }
        }

        public void printLog(EventLog el) {
            Iterator<Event> iterator = el.iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next().toString());
            }
        }

        //EFFECTS: save the file. if no game was started, do not try to write the file and quit immediately.
        private void saveGame() throws NullPointerException {
            if (startButton.isEnabled()) {
                throw new NullPointerException();
            }
            try {
                JsonWriter.open("./data/fisher.json",
                        "./data/inventory.json",
                        "./data/regulation.json");
                JsonWriter.writeInventory(inventory);
                JsonWriter.writeFisher(fisher);
                JsonWriter.writeRegulation(reg);
                JsonWriter.close();
            } catch (FileNotFoundException e) {
                System.out.println("Unable to write to file");
            }
        }
    }

    //the callback class for NewDayButton.
    private class NewDayAction extends AbstractAction {

        //construct the object with name
        public NewDayAction() {
            super("Start a New Day");
        }

        //MODIFIES: this
        //EFFECTS: start a new day by reset the capturedAmount to 0, and update the displayed information.
        @Override
        public void actionPerformed(ActionEvent evt) {
            reg.startNewDay();
            statusUI.status.update();
        }
    }

    public Fisher getFisher() {
        return fisher;
    }

    public Regulation getReg() {
        return reg;
    }

    public Market getMarket() {
        return market;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public StatusUI getStatusUI() {
        return statusUI;
    }

    public MarketUI getMarketUI() {
        return marketUI;
    }
}

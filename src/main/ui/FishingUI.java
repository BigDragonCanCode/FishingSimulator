package ui;

import model.Fish;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

//the UI controls everything in the fishing spot frame.
public class FishingUI extends JInternalFrame {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 300;

    protected JButton addFishButton;
    protected JButton releaseFishButton;
    protected JButton sellFishButton;
    private ImageIcon fishImage;

    private InventoryUI inventoryUI;
    private GameUI gameUI;

    //construct the frame with buttons, and fish list.
    public FishingUI(GameUI gameUI) {
        super("Fishing spot", false, false, false, false);
        this.gameUI = gameUI;

        setLayout(new BorderLayout());

        inventoryUI = new InventoryUI(this);
        inventoryUI.setBackground(Color.BLUE);

        addInteractionButtons();
        add(inventoryUI);
        pack();

        setSize(new Dimension(WIDTH, HEIGHT));

        setLocation(0,30);
        setVisible(true);
    }

    //MODIFIES: this
    //EFFECTS: initialize and add all the buttons to the panel and disable all the buttons.
    //         finally add the panel to the fishing spot frame.
    private void addInteractionButtons() {
        JPanel buttonPanel = new JPanel();
        addFishButton = new JButton(new AddFishAction());
        releaseFishButton = new JButton(new ReleaseFishAction());
        sellFishButton = new JButton(new SellFishAction());

        addFishButton.setEnabled(false);
        releaseFishButton.setEnabled(false);
        sellFishButton.setEnabled(false);

        buttonPanel.add(addFishButton);
        buttonPanel.add(releaseFishButton);
        buttonPanel.add(sellFishButton);
        this.add(buttonPanel, BorderLayout.PAGE_START);
    }

    //MODIFIES: this
    //EFFECTS: set the button to be disabled or enabled based on the boolean parameter.
    public void setSellFishButton(boolean b) {
        sellFishButton.setEnabled(b);
    }

    //MODIFIES: this
    //EFFECTS: set the button to be disabled or enabled based on the boolean parameter.
    public void setAddFishButton(boolean b) {
        addFishButton.setEnabled(b);
    }

    //MODIFIES: this
    //EFFECTS: set the button to be disabled or enabled based on the boolean parameter.
    public void setReleaseFishButton(boolean b) {
        releaseFishButton.setEnabled(b);
    }

    //the callback class for addFishButton
    private class AddFishAction extends AbstractAction {

        //construct the object with name
        public AddFishAction() {
            super("add fish");
        }

        //MODIFIES: this
        //EFFECTS: if the player doesn't have a fishing license or fishing rod, pop a message to identify failing.
        //         Otherwise, the player can choose to keep the fish or not keep the fish.
        //         If keep, add the fish to inventory, update the displayed information, and pop a fish image.
        //         If not, pop a message.
        @Override
        public void actionPerformed(ActionEvent e) {
            fishImage = gameUI.getMarketUI().scaleImages(new ImageIcon("./data/images/fish_1.png"));
            if (!gameUI.getFisher().hasFishingLicense()) {
                JOptionPane.showMessageDialog(gameUI, "please buy a fishing license before proceed!");
            } else if (!gameUI.getFisher().hasFishingRod()) {
                JOptionPane.showMessageDialog(gameUI, "You don't have a fishing rod for fishing!");
            } else {
                int choice = JOptionPane.showConfirmDialog(gameUI,
                        "You just captured a fish, keep or not?");
                if (choice == 0) {
                    inventoryUI.addFishToInventory(new Fish());
                    JOptionPane.showMessageDialog(gameUI, fishImage);
                    gameUI.getReg().addCaptureAmount(1);
                    checkViolation();
                    gameUI.getStatusUI().status.update();
                } else if (choice == 1) {
                    JOptionPane.showMessageDialog(gameUI, "You returned the fish to the water!");
                }
            }
        }

        //MODIFIES: this
        //EFFECTS: pop a notification of: whether the fisher violated the daily quota,
        //whether the license is revoked, how much fine is added, and how much unpaid fine does the fisher has in total.
        private void checkViolation() {
            if (gameUI.getReg().addFine()) {
                JOptionPane.showMessageDialog(gameUI, "You have exceeded the daily quota,"
                        + " $200 fine has been added. Unpaid fine is now: " + gameUI.getReg().getUnPaidFine());
            }
            if (gameUI.getReg().revokeLicense()) {
                gameUI.getFisher().setFishingLicense(false);
                gameUI.getMarketUI().setLicenseButton(true);
                JOptionPane.showMessageDialog(gameUI, "There are 5 violation record under the current license,"
                        + " and the limit is reached. License is revoked.");
            }
        }
    }

    //the callback class for releaseFishButton
    private class ReleaseFishAction extends AbstractAction {

        //construct the object with name
        public ReleaseFishAction() {
            super("release fish");
        }

        //MODIFIES: this
        //EFFECTS: delete the selected fish from the inventory.
        //         If no fish was selected, pop a message and do not call the deletion method.
        //         Finally, update the displayed information.
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = inventoryUI.getFishJList().getSelectedIndex();
            if (index == -1) {
                JOptionPane.showMessageDialog(gameUI, "please select the fish you want to release.");
            } else {
                inventoryUI.removeFishFromInventory(index);
                JOptionPane.showMessageDialog(gameUI, "You returned the fish to the water!");
            }
        }
    }

    //the callback class for sellFishButton
    private class SellFishAction extends AbstractAction {

        //construct the object with name
        public SellFishAction() {
            super("sell fish");
        }

        //MODIFIES: this
        //EFFECTS: delete the fish from inventory, and add the sold amount to fisher's balance.
        //         If no fish was selected, pop a message and do not call the deletion & add balance methods.
        //         Finally, update the displayed information.
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = inventoryUI.getFishJList().getSelectedIndex();
            if (index == -1) {
                JOptionPane.showMessageDialog(gameUI, "please select the fish you want to sell.");
            } else {
                inventoryUI.sellFishFromInventory(index);
                gameUI.getStatusUI().status.update();
            }
        }
    }

    public InventoryUI getInventoryUI() {
        return inventoryUI;
    }

    public GameUI getGameUI() {
        return gameUI;
    }
}

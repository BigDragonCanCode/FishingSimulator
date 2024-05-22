package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

// the class holds everything in the marketplace frame.
public class MarketUI extends JInternalFrame {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 150;

    private static final int IMGSIZE = 55;

    private GameUI gameUI;

    private JButton rodButton;
    private JButton fineButton;
    private JButton licenseButton;

    //construct the frame with images and buying buttons.
    public MarketUI(GameUI gameUI) {
        super("Marketplace", false, false, false, false);
        this.gameUI = gameUI;

        setSize(new Dimension(WIDTH, HEIGHT));

        setLocation(gameUI.getStatusUI().getLocation().x - WIDTH,
                    (int) (gameUI.getSize().getHeight() - HEIGHT - 34));
        setLayout(new GridLayout(2,3));

        addImages();
        addButtonPanel();

        setVisible(true);
    }

    //MODIFIES: this
    //EFFECTS: add the images to the frame.
    private void addImages() {
        ImageIcon rodImage = scaleImages(new ImageIcon("./data/images/fishing_rod.png"));
        ImageIcon licenseImage = scaleImages(new ImageIcon("./data/images/license.jpg"));
        ImageIcon fineImage = scaleImages(new ImageIcon("./data/images/coin.png"));

        JLabel rodImageHolder = new JLabel(rodImage);
        JLabel licenseImageHolder = new JLabel(licenseImage);
        JLabel fineImageHolder = new JLabel(fineImage);

        add(rodImageHolder);
        add(licenseImageHolder);
        add(fineImageHolder);
    }

    //EFFECTS: scale the image to the size of IMGSIZE.
    protected ImageIcon scaleImages(ImageIcon imageIcon) {
        Image img = imageIcon.getImage().getScaledInstance(IMGSIZE, IMGSIZE, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    //MODIFIES: this
    //EFFECTS: instantiate and add all the buttons to the frame.
    private void addButtonPanel() {
        rodButton = new JButton(new ButtonAction("rod"));
        licenseButton = new JButton(new ButtonAction("license"));
        fineButton = new JButton(new ButtonAction("pay fine"));

        rodButton.setEnabled(false);
        licenseButton.setEnabled(false);
        fineButton.setEnabled(false);

        add(rodButton);
        add(licenseButton);
        add(fineButton);
    }

    //MODIFIES: this
    //EFFECTS: when input is true, set the button to be enabled and change the label to "rod"
    //         Otherwise, disable the button and set the label to "bought"
    public void setRodButton(boolean b) {
        if (b) {
            rodButton.setEnabled(true);
            licenseButton.setLabel("rod");
        } else {
            setButtonBought(rodButton);
        }
    }

    //MODIFIES: this
    //EFFECTS: when input is true, set the button to be enabled and change the label to "license"
    //         Otherwise, disable the button and set the label to "bought"
    public void setLicenseButton(boolean b) {
        if (b) {
            licenseButton.setEnabled(true);
            licenseButton.setLabel("license");
        } else {
            setButtonBought(licenseButton);
        }
    }

    //MODIFIES: this
    //EFFECTS: enable the fineButton.
    public void setFineButton() {
        fineButton.setEnabled(true);
    }

    //MODIFIES: this
    //EFFECTS: set the button label to "Bought" and disable the button.
    public void setButtonBought(JButton button) {
        button.setLabel("Bought");
        button.setEnabled(false);
    }


    // the only class controls the callback for all the buttons in this frame.
    private class ButtonAction extends AbstractAction {

        //construct the object with name
        ButtonAction(String name) {
            super(name);
        }

        //MODIFIES: this
        //EFFECTS: buy the tools. if purchase was sucessful, disable the button to prevent buying again.
        // Otherwise, do not buy and pop a message to notify the player the reason of failing.
        // and finally update the displayed information.
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("rod")) {
                String message = gameUI.getMarket().buy("fishing rod");
                if (canBuy(message)) {
                    setButtonBought(rodButton);
                }
            } else if (e.getActionCommand().equals("license")) {
                String message = gameUI.getMarket().buy("license");
                if (canBuy(message)) {
                    setButtonBought(licenseButton);
                }
            } else {
                double unpaid = gameUI.getReg().getUnPaidFine();
                gameUI.getReg().payFine(unpaid);
            }
            gameUI.getStatusUI().status.update();
        }

        //EFFECTS: check if the purchase was successful, if yes, return true.
        //         Otherwise, pop the failing message and return false.
        public boolean canBuy(String message) {
            if (message.equals("You don't have enough money")
                    || message.equals("Clear the fine before you buy a new one.\n")) {
                JOptionPane.showMessageDialog(gameUI, message);
                return false;
            }
            return true;
        }
    }
}

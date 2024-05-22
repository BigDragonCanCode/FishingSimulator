package ui;

import javax.swing.*;

import model.Fish;
import java.util.List;

//the panel for displaying the fish list.
public class InventoryUI extends JPanel {
    private DefaultListModel listModel;
    private JList<Fish> fishJList;
    private JScrollPane scrollPane;

    private FishingUI fishingUI;

    //construct the panel with empty list.
    public InventoryUI(FishingUI fishingUI) {
        this.fishingUI = fishingUI;
        listModel = new DefaultListModel();
        fishJList = new JList<>(listModel);
        scrollPane = new JScrollPane(fishJList);
        add(scrollPane);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    }

    //MODIFIES: this
    //EFFECTS: update the fishJlist with stored list information.
    public void displayList() {
        List<Fish> fishList = fishingUI.getGameUI().getInventory().getFishStorage();
        for (Fish fish: fishList) {
            addFishString(fish);
        }
    }

    //MODIFIES: this
    //EFFECTS: add a single fish to the Inventory list and fishJList
    public void addFishToInventory(Fish fish) {
        fishingUI.getGameUI().getInventory().addFish(fish);
        addFishString(fish);
    }

    //MODIFIES: this
    //EFFECTS: remove the fish from fishJList and Inventory list by the given index.
    public void removeFishFromInventory(int index) {
        listModel.remove(index);
        fishingUI.getGameUI().getInventory().removeFish(index);
    }

    //MODIFIES: this
    //EFFECTS: sell the fish by the given index
    //         Pop a message to show the action is completed.
    public void sellFishFromInventory(int index) {
        String message = fishingUI.getGameUI().getMarket().sell(index);
        JOptionPane.showMessageDialog(fishingUI.getGameUI(), message);
        removeFishFromInventory(index);
    }

    //MODIFIES: this
    //EFFECTS: use the given Fish object, turn it into a String and store in the listModel,
    //         so to customize the display information of the list.
    private void addFishString(Fish fish) {
        String temp = " Fish breed: " + fish.getBreed()
                + " | Size: " + fish.getSize() + " | Unit price:" + fish.getUnitPrice();
        listModel.add(listModel.size(), temp);
    }

    public JList<Fish> getFishJList() {
        return fishJList;
    }
}
